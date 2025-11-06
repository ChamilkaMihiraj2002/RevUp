package com.example.appointment_service.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class SchedulerConfig {

    @Value("${spring.datasource.hikari.maximum-pool-size:10}")
    private Integer hikariThreadPoolSize;

    @Bean
    public Scheduler jdbcScheduler() {
        // Creating a separate thread pool for blocking db io operations
        return Schedulers.newBoundedElastic(
                hikariThreadPoolSize,
                Integer.MAX_VALUE,
                "jdbc-pool",
                60,
                true
        );
    }
}