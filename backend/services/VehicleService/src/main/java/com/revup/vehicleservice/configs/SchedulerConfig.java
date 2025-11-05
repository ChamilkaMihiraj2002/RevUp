package com.revup.vehicleservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * Configuration for JDBC operations with Reactor
 * Since JPA is blocking, we need to use a separate scheduler
 */
@Configuration
public class SchedulerConfig {

    @Bean
    public Scheduler jdbcScheduler() {
        return Schedulers.boundedElastic();
    }
}
