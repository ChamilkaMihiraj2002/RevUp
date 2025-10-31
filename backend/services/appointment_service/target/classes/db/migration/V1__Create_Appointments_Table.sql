-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS appointment_schema;

-- Create appointments table
CREATE TABLE IF NOT EXISTS appointment_schema.appointments (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    employee_id BIGINT,
    service_type VARCHAR(100) NOT NULL,
    scheduled_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    estimated_duration INTEGER DEFAULT 60, -- in minutes
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_scheduled_date CHECK (scheduled_date >= CURRENT_TIMESTAMP)
);

-- Create indexes
CREATE INDEX idx_appointments_customer_id ON appointment_schema.appointments(customer_id);
CREATE INDEX idx_appointments_vehicle_id ON appointment_schema.appointments(vehicle_id);
CREATE INDEX idx_appointments_employee_id ON appointment_schema.appointments(employee_id);
CREATE INDEX idx_appointments_status ON appointment_schema.appointments(status);
CREATE INDEX idx_appointments_scheduled_date ON appointment_schema.appointments(scheduled_date);
CREATE INDEX idx_appointments_customer_date ON appointment_schema.appointments(customer_id, scheduled_date);

-- Create trigger for updated_at
CREATE OR REPLACE FUNCTION appointment_schema.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_appointments_updated_at
    BEFORE UPDATE ON appointment_schema.appointments
    FOR EACH ROW
    EXECUTE FUNCTION appointment_schema.update_updated_at_column();

-- Add comments
COMMENT ON TABLE appointment_schema.appointments IS 'Stores appointment booking information';
COMMENT ON COLUMN appointment_schema.appointments.customer_id IS 'Foreign key to customer_schema.customers';
COMMENT ON COLUMN appointment_schema.appointments.vehicle_id IS 'Foreign key to vehicle_schema.vehicles';
COMMENT ON COLUMN appointment_schema.appointments.employee_id IS 'Foreign key to employee_schema.employees (assigned mechanic)';
COMMENT ON COLUMN appointment_schema.appointments.status IS 'PENDING, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED';
