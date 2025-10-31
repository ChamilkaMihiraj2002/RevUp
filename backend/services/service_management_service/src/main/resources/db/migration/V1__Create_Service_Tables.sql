-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS service_schema;

-- Create service_catalog table
CREATE TABLE IF NOT EXISTS service_schema.service_catalog (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    estimated_duration INTEGER DEFAULT 60, -- in minutes
    base_price DECIMAL(10, 2) DEFAULT 0.00,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create service_instances table
CREATE TABLE IF NOT EXISTS service_schema.service_instances (
    id BIGSERIAL PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    service_catalog_id BIGINT NOT NULL,
    employee_id BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    progress_percentage INTEGER DEFAULT 0,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    actual_price DECIMAL(10, 2),
    actual_duration INTEGER, -- in minutes
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_progress_range CHECK (progress_percentage >= 0 AND progress_percentage <= 100),
    CONSTRAINT chk_price_positive CHECK (actual_price IS NULL OR actual_price >= 0)
);

-- Create indexes for service_catalog
CREATE INDEX idx_service_catalog_category ON service_schema.service_catalog(category);
CREATE INDEX idx_service_catalog_active ON service_schema.service_catalog(is_active);
CREATE INDEX idx_service_catalog_name ON service_schema.service_catalog(name);

-- Create indexes for service_instances
CREATE INDEX idx_service_instances_appointment ON service_schema.service_instances(appointment_id);
CREATE INDEX idx_service_instances_catalog ON service_schema.service_instances(service_catalog_id);
CREATE INDEX idx_service_instances_employee ON service_schema.service_instances(employee_id);
CREATE INDEX idx_service_instances_status ON service_schema.service_instances(status);
CREATE INDEX idx_service_instances_dates ON service_schema.service_instances(start_date, end_date);

-- Create triggers for updated_at
CREATE OR REPLACE FUNCTION service_schema.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_service_catalog_updated_at
    BEFORE UPDATE ON service_schema.service_catalog
    FOR EACH ROW
    EXECUTE FUNCTION service_schema.update_updated_at_column();

CREATE TRIGGER update_service_instances_updated_at
    BEFORE UPDATE ON service_schema.service_instances
    FOR EACH ROW
    EXECUTE FUNCTION service_schema.update_updated_at_column();

-- Add comments
COMMENT ON TABLE service_schema.service_catalog IS 'Stores service catalog/menu items';
COMMENT ON TABLE service_schema.service_instances IS 'Stores actual service instances performed';
COMMENT ON COLUMN service_schema.service_instances.appointment_id IS 'Foreign key to appointment_schema.appointments';
COMMENT ON COLUMN service_schema.service_instances.service_catalog_id IS 'Foreign key to service_catalog';
COMMENT ON COLUMN service_schema.service_instances.employee_id IS 'Foreign key to employee_schema.employees';
COMMENT ON COLUMN service_schema.service_instances.status IS 'SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED, ON_HOLD';
