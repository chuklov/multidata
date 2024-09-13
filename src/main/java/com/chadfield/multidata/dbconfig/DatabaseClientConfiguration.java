package com.chadfield.multidata.dbconfig;


import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class of dynamic reader of the databases configuration and bean creation
 */
@Slf4j
@Component
public class DatabaseClientConfiguration implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Map<String, ConnectionFactory> connectionFactories = new HashMap<>();

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        log.info("Starting postProcessBeanDefinitionRegistry");

        // Get the environment from the context
        Environment environment = applicationContext.getEnvironment();

        // Load the properties manually
        Binder binder = Binder.get(environment);
        List<DatabaseClient> clients = binder.bind("databases.movies", Bindable.listOf(DatabaseClient.class))
                .orElseThrow(() -> new IllegalStateException("No databases configured under 'databases.movies'"));

        clients.forEach(client -> {
            ConnectionFactory connectionFactory = new PostgresqlConnectionFactory(
                    PostgresqlConnectionConfiguration.builder()
                            .host(client.getHost())
                            .port(client.getPort())
                            .database(client.getDatabase())
                            .username(client.getUsername())
                            .password(client.getPassword())
                            .build()
            );

            String beanName = client.getName() + "ConnectionFactory";
            connectionFactories.put(client.getDatabase(), connectionFactory);
            registry.registerBeanDefinition(beanName,
                    BeanDefinitionBuilder.genericBeanDefinition(ConnectionFactory.class, () -> connectionFactory)
                            .getBeanDefinition());
            log.info("Registered ConnectionFactory bean for database: {}", client.getName());
        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // No operation needed here
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Map<String, ConnectionFactory> registeredConnectionFactories() {
        return connectionFactories;
    }

    public ConnectionFactory getConnectionFactory(String dbKey) {
        ConnectionFactory connectionFactory = connectionFactories.get(dbKey);
        return connectionFactory;
    }
}