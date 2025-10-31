# 🚀 RevUp - Automobile Service Management System

A production-ready microservices-based automobile service center management system built with **Spring Boot 3.5.6**, **Spring WebFlux**, and **Java 21**.

## 📋 Table of Contents

- [Features](#-features)
- [Architecture](#-architecture)
- [Technology Stack](#-technology-stack)
- [Quick Start](#-quick-start)
- [Deployment](#-deployment)
- [API Documentation](#-api-documentation)
- [Contributing](#-contributing)

## ✨ Features

### 🏢 Core Services

- **Customer Management** - Customer profiles, loyalty points, customer codes
- **Vehicle Management** - Vehicle registration, VIN tracking, mileage history
- **User Management** - User profiles, roles, Firebase authentication integration
- **Employee Management** - Employee profiles, specializations, ratings, availability
- **Appointment Scheduling** - Booking system with conflict detection
- **Service Management** - Service catalog, instances, real-time progress tracking (WebSocket)
- **Project Management** - Custom modification projects with milestone tracking
- **Time Logging** - Employee time tracking, workload reports, billable hours
- **Notification System** - Multi-channel notifications (WebSocket, SSE, REST)

### 🎯 Advanced Features

- **Real-time Updates** - WebSocket support in Service Management and Notification services
- **Server-Sent Events (SSE)** - One-way streaming for notifications
- **Reactive Programming** - Non-blocking, event-driven architecture with Project Reactor
- **Service Discovery** - Netflix Eureka for dynamic service registration
- **API Gateway** - Centralized routing with Firebase authentication
- **Microservices Architecture** - 11 independently deployable services
- **Swagger Documentation** - Interactive API documentation for all services
- **Database Per Service** - Schema isolation with shared PostgreSQL instance

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         Frontend (React)                         │
│                     http://localhost:5173                        │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             v
┌─────────────────────────────────────────────────────────────────┐
│                    API Gateway (Port 8080)                       │
│              Firebase Auth + Circuit Breakers                    │
└────────────────────────────┬─────────────────────────────────────┘
                             │
          ┌──────────────────┼──────────────────┐
          │                  │                  │
          v                  v                  v
┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│ Eureka Server    │  │  Customer (8081) │  │  Vehicle (8082)  │
│   Port 8761      │  │  User (8083)     │  │  Appt (8084)     │
│                  │  │  Service (8085)  │  │  Project (8086)  │
│ Service          │  │  Employee (8087) │  │  TimeLog (8088)  │
│ Discovery        │  │  Notify (8089)   │  │                  │
└──────────────────┘  └──────────────────┘  └──────────────────┘
                             │
                             v
                   ┌──────────────────┐
                   │   PostgreSQL     │
                   │   9 Schemas      │
                   │   Supabase       │
                   └──────────────────┘
```

### Service Ports

| Service | Port | Database Schema | Features |
|---------|------|----------------|----------|
| **Eureka Server** | 8761 | - | Service Discovery |
| **API Gateway** | 8080 | - | Routing, Auth, Circuit Breakers |
| **Customer Service** | 8081 | customer_schema | Loyalty Points, Customer Codes |
| **Vehicle Service** | 8082 | vehicle_schema | VIN/License Tracking, Mileage |
| **User Service** | 8083 | user_schema | Roles, Firebase UID Mapping |
| **Appointment Service** | 8084 | appointment_schema | Scheduling, Conflict Detection |
| **Service Management** | 8085 | service_schema | Catalog, Progress (WebSocket) |
| **Project Service** | 8086 | project_schema | Projects, Milestones, Cost Tracking |
| **Employee Service** | 8087 | employee_schema | Specializations, Ratings |
| **Time Logging Service** | 8088 | timelog_schema | Workload Reports, Billable Hours |
| **Notification Service** | 8089 | notification_schema | WebSocket, SSE, REST |

## 🛠️ Technology Stack

### Backend

- **Java 21** - Latest LTS release
- **Spring Boot 3.5.6** - Application framework
- **Spring WebFlux** - Reactive programming model
- **Spring Cloud Gateway** - API Gateway
- **Netflix Eureka** - Service discovery
- **Spring Data R2DBC** - Reactive database access
- **PostgreSQL** - Relational database
- **Flyway** - Database migrations
- **MapStruct 1.6.3** - DTO mapping
- **Lombok** - Boilerplate reduction
- **SpringDoc OpenAPI 2.7.0** - API documentation

### Frontend

- **React 18** - UI library
- **TypeScript** - Type safety
- **Vite** - Build tool
- **Tailwind CSS** - Styling
- **Firebase Auth** - Authentication

### DevOps

- **Docker** - Containerization
- **Kubernetes** - Orchestration
- **Maven** - Build tool

## 🚀 Quick Start

### Prerequisites

- **Java 21** or higher
- **Maven 3.9+**
- **PostgreSQL 16** (or use Supabase)
- **Node.js 18+** (for frontend)
- **Firebase account** (for authentication)

### Local Development

1. **Clone the repository**

```bash
git clone https://github.com/ChamilkaMihiraj2002/RevUp.git
cd RevUp
```

2. **Configure Database**

Update `application.yaml` in each service with your database connection:

```yaml
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/revup_db
    username: your_username
    password: your_password
  flyway:
    url: jdbc:postgresql://localhost:5432/revup_db
    user: your_username
    password: your_password
```

3. **Start Services**

```bash
# Start Eureka Server
cd backend/services/eureka_server
mvn spring-boot:run

# Start API Gateway
cd backend/services/api_gateway
mvn spring-boot:run

# Start each microservice
cd backend/services/customer_service
mvn spring-boot:run

# Repeat for all services...
```

4. **Start Frontend**

```bash
cd frontend
npm install
npm run dev
```

5. **Access Applications**

- **Frontend**: http://localhost:5173
- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **Swagger UI** (per service): http://localhost:{port}/swagger-ui.html

## 🐳 Deployment

### Docker Compose (Local/Testing)

See **[README-DOCKER.md](README-DOCKER.md)** for detailed instructions.

**Quick Start:**

```powershell
# Build all images
.\build-all.ps1

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

**Features:**
- ✅ Single command deployment
- ✅ Auto-configured networking
- ✅ Health checks
- ✅ Volume persistence
- ✅ Easy scaling

### Kubernetes (Production)

See **[README-KUBERNETES.md](README-KUBERNETES.md)** for detailed instructions.

**Quick Start:**

```powershell
# Deploy to Kubernetes
.\deploy-k8s.ps1

# Access via port-forward
kubectl port-forward -n revup svc/api-gateway 8080:80

# Check status
kubectl get pods -n revup
```

**Features:**
- ✅ High availability (2-3 replicas per service)
- ✅ Auto-scaling support
- ✅ Rolling updates
- ✅ Resource limits
- ✅ Health probes
- ✅ Ingress controller support

## 📚 API Documentation

Each service provides Swagger UI documentation:

| Service | Swagger UI URL |
|---------|---------------|
| Customer Service | http://localhost:8081/swagger-ui.html |
| Vehicle Service | http://localhost:8082/swagger-ui.html |
| User Service | http://localhost:8083/swagger-ui.html |
| Appointment Service | http://localhost:8084/swagger-ui.html |
| Service Management | http://localhost:8085/swagger-ui.html |
| Project Service | http://localhost:8086/swagger-ui.html |
| Employee Service | http://localhost:8087/swagger-ui.html |
| Time Logging Service | http://localhost:8088/swagger-ui.html |
| Notification Service | http://localhost:8089/swagger-ui.html |

### Example API Requests

**Create Customer:**

```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_FIREBASE_TOKEN" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phone": "+1234567890"
  }'
```

**WebSocket Connection (Service Progress):**

```javascript
const ws = new WebSocket('ws://localhost:8085/ws/progress');

ws.onmessage = (event) => {
  const progress = JSON.parse(event.data);
  console.log('Progress update:', progress);
};
```

**SSE Notifications:**

```javascript
const eventSource = new EventSource('http://localhost:8089/api/notifications/sse/user/123');

eventSource.onmessage = (event) => {
  const notification = JSON.parse(event.data);
  console.log('New notification:', notification);
};
```

## 📊 Project Structure

```
RevUp/
├── backend/
│   ├── services/
│   │   ├── eureka_server/          # Service discovery
│   │   ├── api_gateway/            # API Gateway with Firebase auth
│   │   ├── customer_service/       # Customer management
│   │   ├── vehicle_service/        # Vehicle management
│   │   ├── user_service/           # User management
│   │   ├── appointment_service/    # Appointment scheduling
│   │   ├── service_management/     # Service catalog + instances
│   │   ├── project_service/        # Project + milestone management
│   │   ├── employee_service/       # Employee management
│   │   ├── timelog_service/        # Time logging + reports
│   │   └── notification_service/   # Multi-channel notifications
│   └── init-schemas.sql            # Database initialization
├── frontend/                        # React application
├── k8s/                            # Kubernetes manifests
│   ├── namespace.yaml
│   ├── secrets/
│   ├── configmaps/
│   ├── postgres/
│   ├── deployments/
│   └── ingress.yaml
├── docker-compose.yml              # Docker Compose configuration
├── build-all.ps1                   # Docker build script
├── deploy-k8s.ps1                  # Kubernetes deployment script
├── README.md                       # This file
├── README-DOCKER.md                # Docker deployment guide
├── README-KUBERNETES.md            # Kubernetes deployment guide
└── COMPLETION_REPORT.md            # Project completion documentation
```

## 🧪 Testing

### Run Tests

```bash
# Test specific service
cd backend/services/customer_service
mvn test

# Test all services
mvn clean test -f backend/
```

### Integration Testing

Use the provided Swagger UIs for interactive testing of all endpoints.

## 🔐 Security

- **Firebase Authentication** - Secure user authentication
- **JWT Token Validation** - API Gateway validates all requests
- **Schema Isolation** - Each service has its own database schema
- **Non-root Containers** - Docker images run as non-root user
- **Secrets Management** - Kubernetes secrets for sensitive data

## 📈 Monitoring

### Health Checks

All services expose actuator endpoints:

```bash
curl http://localhost:8081/actuator/health
curl http://localhost:8081/actuator/info
```

### Metrics

Spring Boot Actuator metrics available at:

```bash
curl http://localhost:8081/actuator/metrics
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- **Backend Development** - Spring Boot microservices architecture
- **Frontend Development** - React + TypeScript UI
- **DevOps** - Docker + Kubernetes deployment

## 📞 Support

For issues or questions:

1. Check the documentation in this repository
2. Review service-specific README files
3. Check Eureka dashboard for service health
4. Review logs via `docker-compose logs` or `kubectl logs`

## 🎯 Roadmap

- [ ] Add Redis caching layer
- [ ] Implement distributed tracing (Zipkin/Jaeger)
- [ ] Add Kafka event streaming
- [ ] Implement API rate limiting
- [ ] Add email/SMS notification channels
- [ ] Implement payment gateway integration
- [ ] Add reporting dashboards
- [ ] Mobile app (React Native)

---

**Built with ❤️ using Spring Boot, React, and Kubernetes**