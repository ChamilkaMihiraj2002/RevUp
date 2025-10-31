# User Management Service

## Overview
User Management Service for RevUp Automobile Service Management System. Handles user profiles, roles, and Firebase UID mapping.

## Technologies
- Spring Boot 3.5.6 (WebFlux - Reactive)
- Spring Data R2DBC (Reactive Database Access)
- PostgreSQL
- Flyway (Database Migrations)
- MapStruct (DTO Mapping)
- SpringDoc OpenAPI (Swagger)
- Netflix Eureka Client

## Port
8083

## Key Features
- User profile management
- Role-based access control (CUSTOMER, EMPLOYEE, ADMIN)
- Firebase UID to user mapping
- User status management
- Reactive REST APIs
- Complete CRUD operations

## Database Schema
Schema: `user_schema`

### Users Table
- `id` (PK)
- `firebase_uid` (Unique, indexed)
- `email` (Unique, indexed)
- `first_name`, `last_name`
- `phone`
- `role` (CUSTOMER, EMPLOYEE, ADMIN)
- `status` (ACTIVE, INACTIVE, SUSPENDED, DELETED)
- `profile_image_url`
- `address`, `city`, `state`, `zip_code`, `country`
- `created_at`, `updated_at`

## API Endpoints

### Create User
```http
POST /api/v1/users/register
```

### Get User by ID
```http
GET /api/v1/users/{id}
```

### Get User by Firebase UID
```http
GET /api/v1/users/firebase/{firebaseUid}
```

### Get User by Email
```http
GET /api/v1/users/email/{email}
```

### Get All Users
```http
GET /api/v1/users
```

### Get Users by Role
```http
GET /api/v1/users/role/{role}
```

### Update User
```http
PUT /api/v1/users/{id}
```

### Delete User
```http
DELETE /api/v1/users/{id}
```

## Swagger UI
```
http://localhost:8083/swagger-ui.html
```

## Running the Service

### Development
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production
```bash
mvn clean package
java -jar target/user_management_service-0.0.1-SNAPSHOT.jar
```

## Health Check
```
GET http://localhost:8083/actuator/health
```
