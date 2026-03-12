package com.star.recommender.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.star.recommender.repository",
        entityManagerFactoryRef = "rulesEntityManagerFactory",
        transactionManagerRef = "rulesTransactionManager"
)
public class RulesDataSourceConfiguration {

    // Настройки подключения к БД из application.properties
    @Primary
    @Bean(name = "rulesDataSourceProperties")
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties rulesDataSourceProperties() {
        return new DataSourceProperties();
    }

    // Сам источник данных (пул соединений)
    @Primary
    @Bean(name = "rulesDataSource")
    public DataSource rulesDataSource() {
        return rulesDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    // EntityManagerFactory - фабрика для работы с JPA
    @Primary
    @Bean(name = "rulesEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean rulesEntityManagerFactory(
        @Qualifier("rulesDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.star.recommender.model"); // где искать JPA сущности

        // Настройки Hibernate
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        em.setJpaPropertyMap(properties);

        return em;
    }

    // Менеджер транзакций для JPA
    @Primary
    @Bean(name = "rulesTransactionManager")
    public PlatformTransactionManager rulesTransactionManager(
        @Qualifier("rulesEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }
}
