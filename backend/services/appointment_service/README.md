# Appointment Service

## Overview
The Appointment Service handles appointment booking, scheduling, slot management, and conflict resolution for the RevUp automobile service management system.

## Port
**8084**

## Features
- Appointment booking and management
- Scheduling with conflict detection
- Employee assignment with availability checks
- Status tracking (PENDING, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED)
- Schedule queries and reports
- Slot availability checking

## Technologies
- Spring Boot 3.5.6
- Spring WebFlux (Reactive)
- Spring Data R2DBC
- PostgreSQL (Supabase)
- Netflix Eureka Client
- MapStruct
- Lombok
- Flyway Migration
- SpringDoc OpenAPI (Swagger)

## Database Schema
**Schema:** `appointment_schema`

**Table:** `appointments`
- id (BIGSERIAL, PRIMARY KEY)
- customer_id (BIGINT, NOT NULL) - FK to customer_schema.customers
- vehicle_id (BIGINT, NOT NULL) - FK to vehicle_schema.vehicles
- employee_id (BIGINT, NULLABLE) - FK to employee_schema.employees
- service_type (VARCHAR, NOT NULL)
- scheduled_date (TIMESTAMP, NOT NULL)
- status (VARCHAR, NOT NULL) - PENDING, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED
- estimated_duration (INTEGER) - Duration in minutes
- notes (TEXT)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)

## API Endpoints

### Appointment Management
- `POST /api/appointments` - Create new appointment
- `GET /api/appointments/{id}` - Get appointment by ID
- `GET /api/appointments` - Get all appointments
- `PUT /api/appointments/{id}` - Update appointment
- `DELETE /api/appointments/{id}` - Delete appointment

### Queries
- `GET /api/appointments/customer/{customerId}` - Get customer's appointments
- `GET /api/appointments/vehicle/{vehicleId}` - Get vehicle's appointments
- `GET /api/appointments/employee/{employeeId}` - Get employee's appointments
- `GET /api/appointments/status/{status}` - Get appointments by status
- `GET /api/appointments/upcoming?daysAhead=7` - Get upcoming appointments
- `GET /api/appointments/pending?limit=50` - Get pending appointments
- `GET /api/appointments/employee/{employeeId}/schedule?startDate=...&endDate=...` - Get employee schedule

### Operations
- `PATCH /api/appointments/{id}/status?status=CONFIRMED` - Update status
- `PATCH /api/appointments/{id}/assign?employeeId=123` - Assign employee
- `POST /api/appointments/{id}/confirm` - Confirm appointment
- `POST /api/appointments/{id}/cancel` - Cancel appointment

## Business Logic

### Conflict Detection
- Checks employee availability before booking
- Prevents double-booking
- Validates time slots

### Automatic Features
- Default 60-minute duration
- PENDING status on creation
- Timestamp tracking

## Swagger UI
http://localhost:8084/swagger-ui.html

## Configuration
See `application.yaml` for database and Eureka configuration.

## Running the Service

### Prerequisites
- Java 21+
- PostgreSQL (Supabase)
- Eureka Server running on port 8761

### Build
```bash
./mvnw clean install
```

### Run
```bash
./mvnw spring-boot:run
```

## Service Registration
Automatically registers with Eureka Server on startup.
