# Employee Service

## Overview
The Employee Service manages employee profiles, specializations, availability status, and performance metrics for the RevUp automobile service management system.

## Port
**8087**

## Features
- Employee profile management
- Specialization tracking
- Availability status management (AVAILABLE, BUSY, ON_LEAVE, INACTIVE)
- Service completion tracking
- Rating management
- Employee code generation (EMP-XXXXXXXX)

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
**Schema:** `employee_schema`

**Table:** `employees`
- id (BIGSERIAL, PRIMARY KEY)
- user_id (BIGINT, NOT NULL) - FK to user_management_schema.users
- employee_code (VARCHAR, UNIQUE) - Auto-generated EMP-XXXXXXXX
- specialization (VARCHAR)
- hourly_rate (DECIMAL)
- availability_status (VARCHAR) - AVAILABLE, BUSY, ON_LEAVE, INACTIVE
- total_services_completed (INTEGER)
- average_rating (DECIMAL)
- hire_date (DATE)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)

## API Endpoints

### Employee Management
- `POST /api/employees` - Create employee profile
- `GET /api/employees/{id}` - Get employee by ID
- `GET /api/employees/user/{userId}` - Get employee by user ID
- `GET /api/employees/code/{employeeCode}` - Get employee by code
- `GET /api/employees` - Get all employees
- `PUT /api/employees/{id}` - Update employee
- `DELETE /api/employees/{id}` - Delete employee

### Queries
- `GET /api/employees/availability/{status}` - Get employees by availability
- `GET /api/employees/specialization/{spec}` - Get employees by specialization
- `GET /api/employees/top-rated?minRating=4.0` - Get top-rated employees

### Operations
- `PATCH /api/employees/{id}/availability?status=BUSY` - Update availability
- `POST /api/employees/{id}/services/increment` - Increment service counter
- `POST /api/employees/{id}/rating?rating=4.5` - Update rating

## Swagger UI
http://localhost:8087/swagger-ui.html

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
