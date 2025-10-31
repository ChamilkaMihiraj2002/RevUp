package com.revup.customer_service.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableR2dbcAuditing
@EnableConfigurationProperties({R2dbcProperties.class, FlywayProperties.class})
public class R2dbcConfig {

    @Bean(initMethod = "migrate")
    public Flyway flyway(FlywayProperties flywayProperties) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(flywayProperties.getUrl());
        hikariConfig.setUsername(flywayProperties.getUser());
        hikariConfig.setPassword(flywayProperties.getPassword());
        hikariConfig.setMaximumPoolSize(5);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setConnectionTimeout(30000);
        hikariConfig.setIdleTimeout(600000);
        hikariConfig.setMaxLifetime(1800000);
        // Important: Disable prepared statement caching to avoid conflicts
        hikariConfig.addDataSourceProperty("cachePrepStmts", "false");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "0");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "0");

        DataSource dataSource = new HikariDataSource(hikariConfig);

        return Flyway.configure()
                .dataSource(dataSource)
                .locations(flywayProperties.getLocations().toArray(new String[0]))
                .baselineOnMigrate(flywayProperties.isBaselineOnMigrate())
                .schemas(flywayProperties.getSchemas().toArray(new String[0]))
                .load();
    }
}
