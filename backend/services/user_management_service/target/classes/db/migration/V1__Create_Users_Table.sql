-- Create schema
CREATE SCHEMA IF NOT EXISTS user_schema;

-- Create users table
CREATE TABLE IF NOT EXISTS user_schema.users (
    id BIGSERIAL PRIMARY KEY,
    firebase_uid VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL CHECK (role IN ('CUSTOMER', 'EMPLOYEE', 'ADMIN')),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'DELETED')),
    profile_image_url TEXT,
    address TEXT,
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20),
    country VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_users_firebase_uid ON user_schema.users(firebase_uid);
CREATE INDEX idx_users_email ON user_schema.users(email);
CREATE INDEX idx_users_role ON user_schema.users(role);
CREATE INDEX idx_users_status ON user_schema.users(status);
CREATE INDEX idx_users_role_status ON user_schema.users(role, status);

-- Create trigger for updated_at
CREATE OR REPLACE FUNCTION user_schema.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON user_schema.users
    FOR EACH ROW EXECUTE FUNCTION user_schema.update_updated_at_column();
