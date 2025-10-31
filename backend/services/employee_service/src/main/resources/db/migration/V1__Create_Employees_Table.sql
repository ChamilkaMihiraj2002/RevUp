-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS employee_schema;

-- Create employees table
CREATE TABLE IF NOT EXISTS employee_schema.employees (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    employee_code VARCHAR(50) UNIQUE NOT NULL,
    specialization VARCHAR(100),
    hourly_rate DECIMAL(10, 2) DEFAULT 0.00,
    availability_status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    total_services_completed INTEGER DEFAULT 0,
    average_rating DECIMAL(3, 2) DEFAULT 0.00,
    hire_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_employees_user_id ON employee_schema.employees(user_id);
CREATE INDEX idx_employees_employee_code ON employee_schema.employees(employee_code);
CREATE INDEX idx_employees_availability ON employee_schema.employees(availability_status);
CREATE INDEX idx_employees_specialization ON employee_schema.employees(specialization);

-- Create trigger for updated_at
CREATE OR REPLACE FUNCTION employee_schema.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_employees_updated_at
    BEFORE UPDATE ON employee_schema.employees
    FOR EACH ROW
    EXECUTE FUNCTION employee_schema.update_updated_at_column();

-- Add comments
COMMENT ON TABLE employee_schema.employees IS 'Stores employee profile information';
COMMENT ON COLUMN employee_schema.employees.user_id IS 'Foreign key to user_management_schema.users';
COMMENT ON COLUMN employee_schema.employees.employee_code IS 'Unique employee identifier (e.g., EMP-00000001)';
COMMENT ON COLUMN employee_schema.employees.availability_status IS 'AVAILABLE, BUSY, ON_LEAVE, INACTIVE';
