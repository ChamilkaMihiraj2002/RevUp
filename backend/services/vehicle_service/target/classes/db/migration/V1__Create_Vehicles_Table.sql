-- Create schema
CREATE SCHEMA IF NOT EXISTS vehicle_schema;

-- Create vehicles table
CREATE TABLE IF NOT EXISTS vehicle_schema.vehicles (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    make VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    year INT NOT NULL,
    vin VARCHAR(17) UNIQUE NOT NULL,
    license_plate VARCHAR(20) UNIQUE NOT NULL,
    color VARCHAR(50),
    mileage BIGINT,
    fuel_type VARCHAR(20),
    transmission_type VARCHAR(20),
    engine_size VARCHAR(20),
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_vehicles_customer_id ON vehicle_schema.vehicles(customer_id);
CREATE INDEX idx_vehicles_vin ON vehicle_schema.vehicles(vin);
CREATE INDEX idx_vehicles_license_plate ON vehicle_schema.vehicles(license_plate);

-- Create trigger for updated_at
CREATE OR REPLACE FUNCTION vehicle_schema.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_vehicles_updated_at BEFORE UPDATE ON vehicle_schema.vehicles
    FOR EACH ROW EXECUTE FUNCTION vehicle_schema.update_updated_at_column();
