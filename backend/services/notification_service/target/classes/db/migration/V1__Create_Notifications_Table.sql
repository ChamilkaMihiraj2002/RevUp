-- Create notification schema if not exists
CREATE SCHEMA IF NOT EXISTS notification_schema;

-- Create notifications table
CREATE TABLE notification_schema.notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(200),
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT false NOT NULL,
    related_entity_type VARCHAR(50),
    related_entity_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT chk_notification_type CHECK (type IN ('INFO', 'SUCCESS', 'WARNING', 'ERROR', 'APPOINTMENT', 'SERVICE', 'PROJECT', 'SYSTEM'))
);

-- Create indexes
CREATE INDEX idx_notifications_user ON notification_schema.notifications(user_id);
CREATE INDEX idx_notifications_is_read ON notification_schema.notifications(is_read);
CREATE INDEX idx_notifications_type ON notification_schema.notifications(type);
CREATE INDEX idx_notifications_created_at ON notification_schema.notifications(created_at);
CREATE INDEX idx_notifications_user_read ON notification_schema.notifications(user_id, is_read);
