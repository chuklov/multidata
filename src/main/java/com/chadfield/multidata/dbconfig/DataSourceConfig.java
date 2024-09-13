package com.chadfield.multidata.dbconfig;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


/**
 * Database configuration responsible for database config bean registration and determine current DB.
 */
@Slf4j
@Configuration
public class DataSourceConfig {

    public static final String DB_KEY = "DB_KEY";

    private final DatabaseClientConfiguration databaseClientConfiguration;
    public DataSourceConfig(DatabaseClientConfiguration databaseClientConfiguration) {
        this.databaseClientConfiguration = databaseClientConfiguration;
    }

    @Bean
    @Primary
    public AbstractRoutingConnectionFactory routingConnectionFactory() {
        Map<String, ConnectionFactory> connectionFactories = new HashMap<>(databaseClientConfiguration.registeredConnectionFactories());

        AbstractRoutingConnectionFactory routingConnectionFactory = new AbstractRoutingConnectionFactory() {
            @Override
            protected Mono<Object> determineCurrentLookupKey() {
                return Mono.deferContextual(ctx -> {
                    if (ctx.hasKey(DB_KEY)) {
                        String dbKey = ctx.get(DB_KEY);
                        log.info("Switching to database: {}", dbKey);
                        return Mono.just(dbKey);
                    } else {
                        String defaultDbKey = connectionFactories.keySet().iterator().next();
                        log.warn("DB_KEY not found in context. Using default database key: {}", defaultDbKey);
                        return Mono.just(defaultDbKey);
                    }
                });
            }
        };

        routingConnectionFactory.setTargetConnectionFactories(connectionFactories);
        routingConnectionFactory.setDefaultTargetConnectionFactory(connectionFactories.get(connectionFactories.keySet().iterator().next()));

        return routingConnectionFactory;
    }

    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(AbstractRoutingConnectionFactory routingConnectionFactory) {
        ConnectionFactory connectionFactory = routingConnectionFactory;

        ReactiveDataAccessStrategy strategy = new DefaultReactiveDataAccessStrategy(
                DialectResolver.getDialect(connectionFactory));

        DatabaseClient databaseClient = DatabaseClient.create(connectionFactory);

        return new R2dbcEntityTemplate(databaseClient, strategy);
    }

    @Bean
    public DatabaseClient databaseClient(AbstractRoutingConnectionFactory routingConnectionFactory) {
        return DatabaseClient.create(routingConnectionFactory);
    }
}