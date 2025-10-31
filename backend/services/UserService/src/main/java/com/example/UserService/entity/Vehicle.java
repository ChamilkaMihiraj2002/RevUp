package com.example.UserService.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    
    @Column(name = "registration_no")
    private String registrationNo;
    
    @Column(name = "year")
    private int year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    // Getters and setters
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getRegistrationNo() { return registrationNo; }
    public void setRegistrationNo(String registrationNo) { this.registrationNo = registrationNo; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
