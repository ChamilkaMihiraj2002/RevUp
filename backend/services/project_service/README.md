# Project Service

## Overview
The Project Service manages automotive modification projects and custom requests within the RevUp system. It handles project lifecycle management, milestone tracking, cost estimation, and project completion monitoring.

## Features
- **Project Management**: Create and manage modification projects
- **Project Code Generation**: Automatic unique code generation (PROJ-XXXXXXXX)
- **Milestone Tracking**: Track project progress through milestones
- **Cost Management**: Estimated vs actual cost tracking
- **Status Workflow**: DRAFT → PENDING → APPROVED → IN_PROGRESS → COMPLETED
- **Completion Tracking**: Calculate project completion percentage based on milestones
- **Overdue Detection**: Identify overdue milestones automatically

## API Endpoints

### Projects
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/projects` | Create new project |
| GET | `/api/projects/{id}` | Get project by ID |
| GET | `/api/projects/code/{code}` | Get project by code |
| GET | `/api/projects` | Get all projects |
| GET | `/api/projects/customer/{customerId}` | Get projects by customer |
| GET | `/api/projects/vehicle/{vehicleId}` | Get projects by vehicle |
| GET | `/api/projects/status/{status}` | Get projects by status |
| GET | `/api/projects/active` | Get active projects |
| GET | `/api/projects/search?term={term}` | Search projects |
| PUT | `/api/projects/{id}` | Update project |
| PATCH | `/api/projects/{id}/approve` | Approve project |
| PATCH | `/api/projects/{id}/start` | Start project |
| PATCH | `/api/projects/{id}/complete` | Complete project |
| PATCH | `/api/projects/{id}/cancel` | Cancel project |
| DELETE | `/api/projects/{id}` | Delete project |

### Milestones
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/milestones` | Create new milestone |
| GET | `/api/milestones/{id}` | Get milestone by ID |
| GET | `/api/milestones/project/{projectId}` | Get milestones by project |
| GET | `/api/milestones/project/{projectId}/completion` | Get completion percentage |
| GET | `/api/milestones/status/{status}` | Get milestones by status |
| GET | `/api/milestones/overdue` | Get overdue milestones |
| PUT | `/api/milestones/{id}` | Update milestone |
| PATCH | `/api/milestones/{id}/start` | Start milestone |
| PATCH | `/api/milestones/{id}/complete` | Complete milestone |
| DELETE | `/api/milestones/{id}` | Delete milestone |

## Project Status Flow
```
DRAFT → PENDING → APPROVED → IN_PROGRESS → COMPLETED
                      ↓
                  CANCELLED
```

## Milestone Status Flow
```
PENDING → IN_PROGRESS → COMPLETED
            ↓
        CANCELLED
```

## Configuration
- **Port**: 8086
- **Database Schema**: `project_schema`
- **Eureka Server**: http://localhost:8761
- **Swagger UI**: http://localhost:8086/webjars/swagger-ui/index.html

## Database Schema

### projects table
- `id`: Primary key
- `customer_id`: Foreign key to customer
- `vehicle_id`: Foreign key to vehicle
- `project_code`: Unique project identifier
- `title`: Project title
- `description`: Project description
- `project_type`: Type of modification
- `status`: Current project status
- `estimated_cost`: Initial cost estimate
- `actual_cost`: Final cost (auto-updated)
- `start_date`: Project start date
- `end_date`: Project completion date
- `created_at`, `updated_at`: Timestamps

### project_milestones table
- `id`: Primary key
- `project_id`: Foreign key to project
- `title`: Milestone title
- `description`: Milestone description
- `status`: Current milestone status
- `due_date`: Expected completion date
- `completed_date`: Actual completion date
- `created_at`, `updated_at`: Timestamps

## Request Examples

### Create Project
```json
{
  "customerId": 1,
  "vehicleId": 2,
  "title": "Custom Engine Modification",
  "description": "Upgrade to performance exhaust and intake system",
  "projectType": "Engine Modification",
  "estimatedCost": 5000.00,
  "startDate": "2025-02-01T09:00:00"
}
```

### Create Milestone
```json
{
  "projectId": 1,
  "title": "Parts Procurement",
  "description": "Order all required performance parts",
  "dueDate": "2025-02-05T17:00:00"
}
```

## Technologies Used
- Spring Boot 3.5.6 (WebFlux)
- Spring Data R2DBC
- PostgreSQL (project_schema)
- MapStruct 1.6.3
- Flyway
- SpringDoc OpenAPI
- Netflix Eureka Client

## Build and Run
```bash
cd backend/services/project_service
mvn clean install
mvn spring-boot:run
```

## Testing
Access Swagger UI: http://localhost:8086/webjars/swagger-ui/index.html

## Key Features
1. **Auto Code Generation**: Unique project codes (PROJ-XXXXXXXX)
2. **Milestone Completion**: Automatic completion percentage calculation
3. **Overdue Tracking**: Find milestones past due date
4. **Active Project Query**: Filter in-progress and approved projects
5. **Full-Text Search**: Search projects by title or description
6. **Cost Tracking**: Estimated vs actual cost management
7. **Status Automation**: Auto-set dates on status changes
