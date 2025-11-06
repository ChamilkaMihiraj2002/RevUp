package com.revup.vehicleservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicles", indexes = {
    @Index(name = "idx_vehicle_user_id", columnList = "user_id"),
    @Index(name = "idx_vehicle_registration_no", columnList = "registration_no")
})
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "model")
    private String model;
    
    @Column(name = "registration_no", unique = true)
    private String registrationNo;
    
    @Column(name = "year")
    private Integer year;
    
    @Column(name = "color")
    private String color;
    
    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Getters and setters
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getRegistrationNo() { return registrationNo; }
    public void setRegistrationNo(String registrationNo) { this.registrationNo = registrationNo; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
