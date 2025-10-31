# Service Management Service

## Overview
The Service Management Service handles service catalog management, service instance tracking, and real-time progress updates via WebSocket for the RevUp automobile service management system.

## Port
**8085**

## Features
- **Service Catalog Management**
  - Create, read, update, delete services
  - Service categories and pricing
  - Active/inactive status
  - Search and filtering

- **Service Instance Management**
  - Track service instances for appointments
  - Progress tracking (0-100%)
  - Status management (SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED, ON_HOLD)
  - Employee assignment
  - Duration and pricing tracking

- **Real-Time Updates**
  - WebSocket support for live progress updates
  - Automatic status updates based on progress
  - Multi-client broadcasting

## Technologies
- Spring Boot 3.5.6
- Spring WebFlux (Reactive)
- Spring Data R2DBC
- Spring WebSocket
- PostgreSQL (Supabase)
- Netflix Eureka Client
- MapStruct
- Lombok
- Flyway Migration
- SpringDoc OpenAPI (Swagger)

## Database Schema
**Schema:** `service_schema`

**Tables:**
1. **service_catalog**
   - id, name, description, category
   - estimated_duration, base_price
   - is_active, created_at, updated_at

2. **service_instances**
   - id, appointment_id, service_catalog_id, employee_id
   - status, progress_percentage
   - start_date, end_date
   - actual_price, actual_duration
   - notes, created_at, updated_at

## API Endpoints

### Service Catalog
- `POST /api/services/catalog` - Create service
- `GET /api/services/catalog/{id}` - Get service by ID
- `GET /api/services/catalog` - Get all services
- `GET /api/services/catalog/active` - Get active services
- `GET /api/services/catalog/category/{category}` - Get by category
- `GET /api/services/catalog/search?name=...` - Search by name
- `PUT /api/services/catalog/{id}` - Update service
- `PATCH /api/services/catalog/{id}/toggle-active` - Toggle active status
- `DELETE /api/services/catalog/{id}` - Delete service

### Service Instances
- `POST /api/services/instances` - Create service instance
- `GET /api/services/instances/{id}` - Get instance by ID
- `GET /api/services/instances` - Get all instances
- `GET /api/services/instances/appointment/{appointmentId}` - Get by appointment
- `GET /api/services/instances/appointment/{appointmentId}/progress` - Get overall progress
- `GET /api/services/instances/employee/{employeeId}` - Get by employee
- `GET /api/services/instances/employee/{employeeId}/completed-count` - Get completed count
- `GET /api/services/instances/status/{status}` - Get by status
- `GET /api/services/instances/in-progress` - Get in-progress services
- `PUT /api/services/instances/{id}` - Update instance
- `POST /api/services/instances/progress` - Update progress
- `POST /api/services/instances/{id}/start` - Start service
- `POST /api/services/instances/{id}/complete` - Complete service
- `DELETE /api/services/instances/{id}` - Delete instance

### WebSocket
- `ws://localhost:8085/ws/service-progress` - Real-time progress updates

## WebSocket Usage

### Connect
```javascript
const ws = new WebSocket('ws://localhost:8085/ws/service-progress');

ws.onmessage = (event) => {
  const progress = JSON.parse(event.data);
  console.log('Progress update:', progress);
};
```

### Message Format
```json
{
  "id": 1,
  "appointmentId": 10,
  "serviceCatalogId": 5,
  "employeeId": 3,
  "status": "IN_PROGRESS",
  "progressPercentage": 45,
  "startDate": "2025-10-30T10:00:00",
  "actualPrice": 150.00,
  "notes": "Working on engine diagnostic",
  "createdAt": "2025-10-30T09:00:00",
  "updatedAt": "2025-10-30T10:30:00"
}
```

## Business Logic

### Auto-Status Updates
- Progress 0% → SCHEDULED
- Progress 1-99% → IN_PROGRESS (auto-sets start_date)
- Progress 100% → COMPLETED (auto-sets end_date)

### Overall Progress Calculation
Calculates average progress across all service instances for an appointment.

## Swagger UI
http://localhost:8085/swagger-ui.html

## Configuration
See `application.yaml` for database, Eureka, and WebSocket configuration.

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
