package com.chadfield.multidata.dbconfig;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ContextSettingWebFilter implements WebFilter {

    private final DatabaseClientConfiguration databaseClientConfiguration;

    public ContextSettingWebFilter(DatabaseClientConfiguration databaseClientConfiguration) {
        this.databaseClientConfiguration = databaseClientConfiguration;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("ContextSettingWebFilter filter");

        return exchange.getSession()
                .flatMap(session -> {
                    String dbKey = session.getAttribute(DataSourceConfig.DB_KEY);
                    if (dbKey == null) {
                        dbKey = "comedy";
                        session.getAttributes().put(DataSourceConfig.DB_KEY, dbKey);
                    }
                    log.info("DB_KEY set in session: " + dbKey);

                    // Retrieve the ConnectionFactory from the dynamically created beans
                    ConnectionFactory connectionFactory = databaseClientConfiguration.getConnectionFactory(dbKey);

                    if (connectionFactory != null) {
                        log.info("ConnectionFactory details for DB_KEY '{}': {}", dbKey, connectionFactory.getMetadata());
                    } else {
                        log.warn("No ConnectionFactory found for DB_KEY '{}'", dbKey);
                    }

                    String finalDbKey = dbKey;
                    return chain.filter(exchange)
                            .contextWrite(ctx -> ctx.put(DataSourceConfig.DB_KEY, finalDbKey).put(WebSession.class, session));
                });
    }
}