package com.revup.time_tracking_service.repository;

import com.revup.time_tracking_service.entity.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeLogRepository extends JpaRepository<TimeLog, Long> {
    Optional<TimeLog> findByAppointmentServiceIdAndUserId(Long appointmentServiceId, Long userId);
}