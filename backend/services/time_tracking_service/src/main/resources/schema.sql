-- Schema for Time Tracking Service
CREATE SCHEMA IF NOT EXISTS time_tracking;

CREATE TABLE IF NOT EXISTS time_tracking.time_log (
                                                      timelog_id BIGSERIAL PRIMARY KEY,
                                                      appointment_service_id BIGINT NOT NULL,
                                                      technician_id BIGINT NOT NULL,
                                                      start_time TIMESTAMP WITH TIME ZONE NOT NULL,
                                                      end_time TIMESTAMP WITH TIME ZONE,
                                                      description TEXT,
                                                      log_date DATE NOT NULL DEFAULT CURRENT_DATE,
                                                      created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS time_tracking.appointment_service_status (
                                                                        appointment_service_id BIGINT PRIMARY KEY,
                                                                        status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
    CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    actual_minutes INTEGER,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
    );

-- Index for performance
CREATE INDEX idx_time_log_technician ON time_tracking.time_log(technician_id);
CREATE INDEX idx_time_log_date ON time_tracking.time_log(log_date);