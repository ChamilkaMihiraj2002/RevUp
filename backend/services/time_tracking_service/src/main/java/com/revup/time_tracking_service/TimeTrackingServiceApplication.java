package com.revup.time_tracking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing

public class TimeTrackingServiceApplication {

	public static void main(String[] args) {
        SpringApplication.run(TimeTrackingServiceApplication.class, args);
	}

}
