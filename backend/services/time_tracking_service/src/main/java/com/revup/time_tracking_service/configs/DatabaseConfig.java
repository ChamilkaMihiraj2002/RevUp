package com.revup.time_tracking_service.configs;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

import static org.springframework.jmx.support.RegistrationPolicy.IGNORE_EXISTING;


@Configuration
@EnableMBeanExport(registration=IGNORE_EXISTING)
public class DatabaseConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    @Profile({"dev"})
    public DataSource devDataSource(
            @Value("${spring.ds-dev.jdbc-url}") String url,
            @Value("${spring.ds-dev.username}") String username,
            @Value("${spring.ds-dev.password}") String password) {

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
