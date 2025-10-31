# Time Logging Service

## Overview
The Time Logging Service manages employee time tracking, workload analysis, and productivity reporting within the RevUp system. It provides comprehensive time logging capabilities for services and projects with billable/non-billable hour tracking.

## Features
- **Time Entry Management**: Log employee work hours with validation
- **Billable Hours Tracking**: Distinguish between billable and non-billable hours
- **Service/Project Association**: Link time logs to service instances or projects
- **Date Range Queries**: Filter logs by custom date ranges
- **Workload Reports**: Generate employee productivity and workload reports
- **Hours Validation**: Ensure logged hours are between 0.01 and 24 per entry

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/timelogs` | Create new time log entry |
| GET | `/api/timelogs/{id}` | Get time log by ID |
| GET | `/api/timelogs` | Get all time logs |
| GET | `/api/timelogs/employee/{employeeId}` | Get time logs by employee |
| GET | `/api/timelogs/service/{serviceInstanceId}` | Get time logs by service instance |
| GET | `/api/timelogs/project/{projectId}` | Get time logs by project |
| GET | `/api/timelogs/billable` | Get billable time logs |
| GET | `/api/timelogs/non-billable` | Get non-billable time logs |
| GET | `/api/timelogs/date-range?startDate={start}&endDate={end}` | Get logs by date range |
| GET | `/api/timelogs/employee/{employeeId}/date-range?startDate={start}&endDate={end}` | Get employee logs by date range |
| GET | `/api/timelogs/employee/{employeeId}/workload-report?startDate={start}&endDate={end}` | Get employee workload report |
| PUT | `/api/timelogs/{id}` | Update time log |
| DELETE | `/api/timelogs/{id}` | Delete time log |

## Configuration
- **Port**: 8088
- **Database Schema**: `timelog_schema`
- **Eureka Server**: http://localhost:8761
- **Swagger UI**: http://localhost:8088/webjars/swagger-ui/index.html

## Database Schema

### time_logs table
- `id`: Primary key
- `employee_id`: Foreign key to employee
- `service_instance_id`: Foreign key to service instance (nullable)
- `project_id`: Foreign key to project (nullable)
- `log_date`: Date of work performed
- `hours_worked`: Hours worked (0.01 - 24.00)
- `description`: Work description
- `billable`: Billable flag (true/false)
- `created_at`, `updated_at`: Timestamps

## Request Examples

### Create Time Log
```json
{
  "employeeId": 3,
  "serviceInstanceId": 15,
  "logDate": "2025-01-25",
  "hoursWorked": 6.5,
  "description": "Engine diagnostics and repair",
  "billable": true
}
```

### Workload Report Response
```json
{
  "employeeId": 3,
  "totalDays": 15,
  "totalHours": 112.50,
  "billableHours": 98.00,
  "nonBillableHours": 14.50,
  "averageHoursPerDay": 7.50
}
```

## Workload Report Features
The workload report provides:
- **Total Days Worked**: Count of distinct dates with logged time
- **Total Hours**: Sum of all hours worked
- **Billable vs Non-Billable**: Breakdown of billable and non-billable hours
- **Average Hours Per Day**: Calculated average based on working days

## Query Capabilities
1. **Employee Time Tracking**: View all time logs for specific employees
2. **Service Association**: Link time to specific service instances
3. **Project Association**: Track project-related time entries
4. **Billable Filtering**: Separate billable from non-billable hours
5. **Date Range Analysis**: Custom date range queries for reporting
6. **Productivity Metrics**: Calculate average hours per day

## Technologies Used
- Spring Boot 3.5.6 (WebFlux)
- Spring Data R2DBC
- PostgreSQL (timelog_schema)
- MapStruct 1.6.3
- Flyway
- SpringDoc OpenAPI
- Netflix Eureka Client

## Build and Run
```bash
cd backend/services/timelog_service
mvn clean install
mvn spring-boot:run
```

## Testing
Access Swagger UI: http://localhost:8088/webjars/swagger-ui/index.html

## Validation Rules
- **Hours Worked**: Must be between 0.01 and 24.00
- **Employee ID**: Required for all time logs
- **Log Date**: Required, cannot be future date (enforced by business logic if needed)
- **Billable Flag**: Required (true/false)
- **Service or Project**: At least one should be specified (optional validation)

## Use Cases
1. **Daily Time Logging**: Employees log hours daily for services/projects
2. **Billing Reports**: Generate billable hours reports for invoicing
3. **Workload Analysis**: Monitor employee workload distribution
4. **Productivity Tracking**: Calculate average hours and efficiency metrics
5. **Project Costing**: Track time spent on specific projects
6. **Service Duration**: Analyze actual time vs estimated time for services
