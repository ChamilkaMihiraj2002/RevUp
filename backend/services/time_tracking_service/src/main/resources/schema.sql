CREATE TABLE time_logs (
                           timelog_id BIGSERIAL PRIMARY KEY,
                           appointment_service_id BIGINT NOT NULL,
                           user_id BIGINT NOT NULL,
                           start_time TIMESTAMP WITH TIME ZONE NOT NULL,
                           end_time TIMESTAMP WITH TIME ZONE,
                           description TEXT,
                           log_date DATE NOT NULL DEFAULT CURRENT_DATE,
                           created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
                           updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

                           CONSTRAINT fk_appointment_service
                               FOREIGN KEY (appointment_service_id)
                                   REFERENCES appointment_services(appointment_service_id),
                           CONSTRAINT fk_user
                               FOREIGN KEY (user_id)
                                   REFERENCES users(user_id)
);

CREATE INDEX idx_time_logs_appointment_service ON time_logs(appointment_service_id);
CREATE INDEX idx_time_logs_user ON time_logs(user_id);
CREATE INDEX idx_time_logs_date ON time_logs(log_date);