package com.revup.time_tracking_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "time_logs")
@EntityListeners(AuditingEntityListener.class) // Enable auditing
@Getter // Lombok
@Setter // Lombok
@NoArgsConstructor // Lombok
@AllArgsConstructor // Lombok
public class TimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timelogId;

    @Column(name = "appointment_service_id", nullable = false)
    private Long appointmentServiceId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "log_date")
    private LocalDate logDate;

    @CreatedDate // Managed by Auditing
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // Managed by Auditing
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void updateLogDate() {
        if (this.startTime != null) {
            // Extracts the "date" part from the "date-time"
            this.logDate = this.startTime.toLocalDate();
        }
    }

    /**
     * Calculates the duration in minutes.
     * This is a "getter" that will be picked up by mapstruct/jackson
     * but will not be a column in the database.
     */
    public Long getActualMinutes() {
        if (this.startTime == null || this.endTime == null) {
            return null;
        }
        // Use java.time.Duration to find the difference
        return java.time.Duration.between(this.startTime, this.endTime).toMinutes();
    }
}