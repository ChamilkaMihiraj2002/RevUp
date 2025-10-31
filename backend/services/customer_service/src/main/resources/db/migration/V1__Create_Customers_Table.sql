-- Create schema
CREATE SCHEMA IF NOT EXISTS customer_schema;

-- Create customers table
CREATE TABLE IF NOT EXISTS customer_schema.customers (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    loyalty_points INTEGER DEFAULT 0,
    total_services_count INTEGER DEFAULT 0,
    total_spent DECIMAL(10, 2) DEFAULT 0.00,
    preferred_contact_method VARCHAR(20) DEFAULT 'EMAIL' CHECK (preferred_contact_method IN ('EMAIL', 'PHONE', 'SMS')),
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user_schema.users(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_customer_user_id ON customer_schema.customers(user_id);
CREATE INDEX idx_customer_loyalty_points ON customer_schema.customers(loyalty_points);

-- Create trigger for updated_at
CREATE OR REPLACE FUNCTION customer_schema.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_customers_updated_at BEFORE UPDATE ON customer_schema.customers
    FOR EACH ROW EXECUTE FUNCTION customer_schema.update_updated_at_column();
