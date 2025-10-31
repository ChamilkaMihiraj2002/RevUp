-- Create timelog schema if not exists
CREATE SCHEMA IF NOT EXISTS timelog_schema;

-- Create time_logs table
CREATE TABLE timelog_schema.time_logs (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    service_instance_id BIGINT,
    project_id BIGINT,
    log_date DATE NOT NULL,
    hours_worked DECIMAL(5,2) NOT NULL CHECK (hours_worked > 0 AND hours_worked <= 24),
    description TEXT,
    billable BOOLEAN DEFAULT true NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Create indexes
CREATE INDEX idx_time_logs_employee ON timelog_schema.time_logs(employee_id);
CREATE INDEX idx_time_logs_service ON timelog_schema.time_logs(service_instance_id);
CREATE INDEX idx_time_logs_project ON timelog_schema.time_logs(project_id);
CREATE INDEX idx_time_logs_date ON timelog_schema.time_logs(log_date);
CREATE INDEX idx_time_logs_billable ON timelog_schema.time_logs(billable);

-- Create trigger for updated_at
CREATE OR REPLACE FUNCTION timelog_schema.update_time_logs_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_time_logs_updated_at
    BEFORE UPDATE ON timelog_schema.time_logs
    FOR EACH ROW
    EXECUTE FUNCTION timelog_schema.update_time_logs_updated_at();
