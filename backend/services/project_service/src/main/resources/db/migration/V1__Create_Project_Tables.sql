-- Create project schema if not exists
CREATE SCHEMA IF NOT EXISTS project_schema;

-- Create projects table
CREATE TABLE project_schema.projects (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    project_code VARCHAR(50) UNIQUE NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    project_type VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    estimated_cost DECIMAL(12,2),
    actual_cost DECIMAL(12,2) DEFAULT 0,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT chk_project_status CHECK (status IN ('DRAFT', 'PENDING', 'APPROVED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'))
);

-- Create project_milestones table
CREATE TABLE project_schema.project_milestones (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    due_date TIMESTAMP,
    completed_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_milestone_project FOREIGN KEY (project_id) REFERENCES project_schema.projects(id) ON DELETE CASCADE,
    CONSTRAINT chk_milestone_status CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'))
);

-- Create indexes
CREATE INDEX idx_projects_customer ON project_schema.projects(customer_id);
CREATE INDEX idx_projects_vehicle ON project_schema.projects(vehicle_id);
CREATE INDEX idx_projects_status ON project_schema.projects(status);
CREATE INDEX idx_projects_code ON project_schema.projects(project_code);
CREATE INDEX idx_milestones_project ON project_schema.project_milestones(project_id);
CREATE INDEX idx_milestones_status ON project_schema.project_milestones(status);

-- Create trigger for projects updated_at
CREATE OR REPLACE FUNCTION project_schema.update_projects_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_projects_updated_at
    BEFORE UPDATE ON project_schema.projects
    FOR EACH ROW
    EXECUTE FUNCTION project_schema.update_projects_updated_at();

-- Create trigger for milestones updated_at
CREATE OR REPLACE FUNCTION project_schema.update_milestones_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_milestones_updated_at
    BEFORE UPDATE ON project_schema.project_milestones
    FOR EACH ROW
    EXECUTE FUNCTION project_schema.update_milestones_updated_at();
