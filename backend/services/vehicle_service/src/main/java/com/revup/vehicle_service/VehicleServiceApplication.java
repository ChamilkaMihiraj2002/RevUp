package com.revup.vehicle_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableR2dbcRepositories
public class VehicleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleServiceApplication.class, args);
	}

}
