# API Gateway Service

## Overview
API Gateway service for RevUp Automobile Service Management System. This service acts as the single entry point for all client requests and handles:
- Firebase authentication and token validation
- Request routing to appropriate microservices
- Load balancing
- Circuit breaking
- Request/Response logging
- CORS configuration

## Technologies
- Spring Boot 3.5.6
- Spring Cloud Gateway
- Firebase Admin SDK
- Resilience4j
- Netflix Eureka Client

## Port
8080

## Key Features
- **Firebase Authentication**: Validates Firebase ID tokens
- **Service Discovery**: Integrates with Eureka for dynamic service routing
- **Circuit Breaker**: Resilience4j for fault tolerance
- **Load Balancing**: Automatic load balancing across service instances
- **CORS**: Configured for frontend communication
- **Fallback**: Graceful degradation when services are unavailable

## Configuration

### Firebase Setup
1. Place your Firebase service account JSON file in the resources folder or specify path via environment variable
2. Set `FIREBASE_CREDENTIALS_PATH` environment variable (optional)

### Environment Variables
- `FIREBASE_CREDENTIALS_PATH`: Path to Firebase credentials JSON file

## Running the Service

### Development
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production
```bash
mvn clean package
java -jar target/api_gateway-0.0.1-SNAPSHOT.jar
```

## API Routes
All routes are prefixed with `/api/v1/`

- `/api/v1/users/**` → User Management Service
- `/api/v1/customers/**` → Customer Service
- `/api/v1/vehicles/**` → Vehicle Service
- `/api/v1/employees/**` → Employee Service
- `/api/v1/appointments/**` → Appointment Service
- `/api/v1/services/**` → Service Management Service
- `/api/v1/projects/**` → Project Service
- `/api/v1/timelogs/**` → Time Logging Service
- `/api/v1/notifications/**` → Notification Service
- `/ws/**` → WebSocket connections

## Health Check
```
GET http://localhost:8080/actuator/health
```

## Authentication Flow
1. Client authenticates with Firebase
2. Client receives Firebase ID token
3. Client sends request with `Authorization: Bearer <token>`
4. Gateway validates token with Firebase
5. Gateway extracts user info (UID, email)
6. Gateway adds user info to request headers:
   - `X-Firebase-UID`
   - `X-User-Email`
   - `X-User-Role`
7. Gateway routes request to target service
8. Target service trusts gateway headers

## Circuit Breaker
Configured for all service routes with:
- Sliding window size: 10
- Failure threshold: 50%
- Wait duration in open state: 10s
- Half-open calls: 3

## Public Endpoints
Following endpoints don't require authentication:
- `/actuator/**`
- `/api/v1/users/register`
- `/api/v1/users/public/**`
