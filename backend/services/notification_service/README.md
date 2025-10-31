# Notification Service

## Overview
The Notification Service provides real-time notification delivery within the RevUp system using both WebSocket for bi-directional communication and Server-Sent Events (SSE) for one-way streaming. It manages notification history, read/unread tracking, and supports multiple notification types.

## Features
- **Real-time WebSocket Notifications**: Bi-directional communication for instant notifications
- **Server-Sent Events (SSE)**: One-way streaming for notification updates
- **Multi-channel Support**: WebSocket + SSE + REST API
- **Notification History**: Persistent storage of all notifications
- **Read/Unread Tracking**: Mark notifications as read individually or in bulk
- **Type Categorization**: INFO, SUCCESS, WARNING, ERROR, APPOINTMENT, SERVICE, PROJECT, SYSTEM
- **Entity Linking**: Associate notifications with related entities
- **Auto-Broadcasting**: Notifications automatically broadcast to WebSocket clients

## API Endpoints

### REST API
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/notifications` | Create notification (auto-broadcasts via WebSocket) |
| GET | `/api/notifications/{id}` | Get notification by ID |
| GET | `/api/notifications` | Get all notifications |
| GET | `/api/notifications/user/{userId}` | Get user notifications (ordered by created_at desc) |
| GET | `/api/notifications/user/{userId}/unread` | Get unread notifications for user |
| GET | `/api/notifications/user/{userId}/unread-count` | Get unread count |
| GET | `/api/notifications/type/{type}` | Get notifications by type |
| PATCH | `/api/notifications/{id}/read` | Mark single notification as read |
| PATCH | `/api/notifications/read` | Mark multiple notifications as read |
| PATCH | `/api/notifications/user/{userId}/read-all` | Mark all notifications as read for user |
| DELETE | `/api/notifications/{id}` | Delete notification |
| DELETE | `/api/notifications/user/{userId}` | Delete all notifications for user |

### Server-Sent Events (SSE)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/notifications/sse/user/{userId}` | Stream unread notifications for user |
| GET | `/api/notifications/sse/all` | Stream all notifications |

### WebSocket
- **Endpoint**: `ws://localhost:8089/ws/notifications`
- **Protocol**: Text-based JSON messages
- **Behavior**: Clients connect and receive real-time notifications as they're created
- **Format**: Notifications are sent as JSON matching `NotificationResponse` structure

## Configuration
- **Port**: 8089
- **Database Schema**: `notification_schema`
- **Eureka Server**: http://localhost:8761
- **Swagger UI**: http://localhost:8089/webjars/swagger-ui/index.html
- **WebSocket Endpoint**: ws://localhost:8089/ws/notifications
- **Allowed Origins**: http://localhost:3000, http://localhost:5173

## Database Schema

### notifications table
- `id`: Primary key
- `user_id`: Foreign key to user
- `type`: Notification type (INFO, SUCCESS, WARNING, ERROR, APPOINTMENT, SERVICE, PROJECT, SYSTEM)
- `title`: Notification title
- `message`: Notification message
- `is_read`: Read status (default: false)
- `related_entity_type`: Type of related entity (optional)
- `related_entity_id`: ID of related entity (optional)
- `created_at`: Timestamp

## Request Examples

### Create Notification (REST)
```json
{
  "userId": 5,
  "type": "APPOINTMENT",
  "title": "Appointment Confirmed",
  "message": "Your appointment for 2025-02-15 at 10:00 AM has been confirmed",
  "relatedEntityType": "appointment",
  "relatedEntityId": 123
}
```

### Mark Multiple as Read
```json
{
  "notificationIds": [1, 2, 3, 4, 5]
}
```

## WebSocket Usage

### JavaScript Client Example
```javascript
const ws = new WebSocket('ws://localhost:8089/ws/notifications');

ws.onopen = () => {
  console.log('WebSocket connected');
};

ws.onmessage = (event) => {
  const notification = JSON.parse(event.data);
  console.log('New notification:', notification);
  // Display notification to user
  showNotification(notification);
};

ws.onerror = (error) => {
  console.error('WebSocket error:', error);
};

ws.onclose = () => {
  console.log('WebSocket disconnected');
};
```

### React Hook Example
```javascript
useEffect(() => {
  const ws = new WebSocket('ws://localhost:8089/ws/notifications');
  
  ws.onmessage = (event) => {
    const notification = JSON.parse(event.data);
    setNotifications(prev => [notification, ...prev]);
  };
  
  return () => ws.close();
}, []);
```

## SSE Usage

### JavaScript Client Example
```javascript
const eventSource = new EventSource('http://localhost:8089/api/notifications/sse/user/5');

eventSource.onmessage = (event) => {
  const notification = JSON.parse(event.data);
  console.log('SSE notification:', notification);
  displayNotification(notification);
};

eventSource.onerror = (error) => {
  console.error('SSE error:', error);
  eventSource.close();
};
```

## Notification Types

| Type | Use Case |
|------|----------|
| INFO | General information |
| SUCCESS | Successful operations |
| WARNING | Warning messages |
| ERROR | Error notifications |
| APPOINTMENT | Appointment-related updates |
| SERVICE | Service instance updates |
| PROJECT | Project milestone updates |
| SYSTEM | System-level notifications |

## Real-Time Broadcast Flow

1. **Create Notification**: POST `/api/notifications`
2. **Save to Database**: Notification persisted with `is_read=false`
3. **Auto-Broadcast**: Service automatically broadcasts to all WebSocket clients
4. **Client Receives**: Connected clients receive notification instantly
5. **Mark as Read**: Client calls PATCH endpoint to mark as read

## Technologies Used
- Spring Boot 3.5.6 (WebFlux)
- Spring WebSocket
- Spring Data R2DBC
- PostgreSQL (notification_schema)
- MapStruct 1.6.3
- Flyway
- SpringDoc OpenAPI
- Netflix Eureka Client
- Server-Sent Events (SSE)

## Build and Run
```bash
cd backend/services/notification_service
mvn clean install
mvn spring-boot:run
```

## Testing

### Swagger UI
http://localhost:8089/webjars/swagger-ui/index.html

### WebSocket Test (PowerShell)
```powershell
# Install wscat if not available: npm install -g wscat
wscat -c ws://localhost:8089/ws/notifications
```

### SSE Test (Browser)
Open browser console and run:
```javascript
const es = new EventSource('http://localhost:8089/api/notifications/sse/user/1');
es.onmessage = e => console.log(JSON.parse(e.data));
```

## Key Features

1. **Dual Real-Time Channels**: WebSocket for bi-directional + SSE for streaming
2. **Auto-Broadcasting**: New notifications automatically sent to connected clients
3. **Persistent History**: All notifications stored in database
4. **Read Tracking**: Individual, bulk, and "mark all" read operations
5. **Type Filtering**: Query notifications by type
6. **User-Specific**: Get notifications for specific users
7. **Unread Count**: Quick badge count for UI
8. **Entity Linking**: Link notifications to appointments, services, projects
9. **CORS Support**: Configured for React frontend origins
10. **Active Session Tracking**: Monitor connected WebSocket clients

## Integration Examples

### Service Management Integration
When service progress updates, create notification:
```java
notificationService.createNotification(new CreateNotificationRequest(
    customerId,
    NotificationType.SERVICE,
    "Service Progress Updated",
    "Your vehicle service is now 75% complete",
    "service_instance",
    serviceInstanceId
)).subscribe();
```

### Appointment Confirmation
```java
notificationService.createNotification(new CreateNotificationRequest(
    customerId,
    NotificationType.APPOINTMENT,
    "Appointment Confirmed",
    "Your appointment for " + date + " has been confirmed",
    "appointment",
    appointmentId
)).subscribe();
```

## Performance Considerations
- **Throttling**: SSE endpoints throttled to 1 notification per second
- **Backpressure**: WebSocket uses multicast with backpressure buffer
- **Concurrent Sessions**: ConcurrentHashMap for thread-safe session management
- **Database Indexes**: Optimized queries for user_id, is_read, type, created_at
