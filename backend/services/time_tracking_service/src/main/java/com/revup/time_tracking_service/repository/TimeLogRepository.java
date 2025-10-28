package com.revup.time_tracking_service.repository;

import com.revup.time_tracking_service.entity.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeLogRepository extends JpaRepository<TimeLog, Long> {
    // JpaRepository provides:
    // - save() (Create/Update)
    // - findById() (Read)
    // - findAll() (Read)
    // - deleteById() (Delete)
    // ...and more
}