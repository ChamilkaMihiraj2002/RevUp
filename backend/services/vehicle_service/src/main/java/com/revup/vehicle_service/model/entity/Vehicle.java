package com.revup.vehicle_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicles", schema = "vehicle_schema")
public class Vehicle {
    
    @Id
    private Long id;
    
    @Column("customer_id")
    private Long customerId;
    
    @Column("make")
    private String make;
    
    @Column("model")
    private String model;
    
    @Column("year")
    private Integer year;
    
    @Column("vin")
    private String vin;
    
    @Column("license_plate")
    private String licensePlate;
    
    @Column("color")
    private String color;
    
    @Column("mileage")
    private Long mileage;
    
    @Column("fuel_type")
    private String fuelType;
    
    @Column("transmission_type")
    private String transmissionType;
    
    @Column("engine_size")
    private String engineSize;
    
    @Column("notes")
    private String notes;
    
    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
