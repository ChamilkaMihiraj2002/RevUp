package com.example.UserService.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.UserService.Enum.Role;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_role", columnList = "role")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "firebase_uid", unique = true)
    private String firebaseUID;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_vehicles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "vehicle_id")
    private List<Long> vehicleIds = new ArrayList<>();

    // ====== Getters and Setters ======
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getFirebaseUID() { return firebaseUID; }
    public void setFirebaseUID(String firebaseUID) { this.firebaseUID = firebaseUID; }

    public List<Long> getVehicleIds() { return vehicleIds; }
    public void setVehicleIds(List<Long> vehicleIds) { this.vehicleIds = vehicleIds; }
}

