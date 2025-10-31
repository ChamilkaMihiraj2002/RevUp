package com.revup.user_service.model.entity;

import com.revup.user_service.model.enums.UserRole;
import com.revup.user_service.model.enums.UserStatus;
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
@Table(name = "users", schema = "user_schema")
public class User {
    
    @Id
    private Long id;
    
    @Column("firebase_uid")
    private String firebaseUid;
    
    @Column("email")
    private String email;
    
    @Column("first_name")
    private String firstName;
    
    @Column("last_name")
    private String lastName;
    
    @Column("phone")
    private String phone;
    
    @Column("role")
    private UserRole role;
    
    @Column("status")
    private UserStatus status;
    
    @Column("profile_image_url")
    private String profileImageUrl;
    
    @Column("address")
    private String address;
    
    @Column("city")
    private String city;
    
    @Column("state")
    private String state;
    
    @Column("zip_code")
    private String zipCode;
    
    @Column("country")
    private String country;
    
    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
