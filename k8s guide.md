# RevUp Application Startup Guide with Docker Desktop

## Prerequisites
- Docker Desktop installed and running
- Kubernetes enabled in Docker Desktop settings
- At least 4GB RAM allocated to Docker Desktop
- kubectl installed (comes with Docker Desktop)

## Step 1: Enable Kubernetes in Docker Desktop
1. Open Docker Desktop
2. Go to Settings → Kubernetes
3. Check "Enable Kubernetes"
4. Click "Apply & Restart"
5. Wait for Kubernetes to start (may take 2-3 minutes)

## Step 2: Verify Kubernetes Setup
```bash
kubectl cluster-info
kubectl get nodes
```

## Step 3: Clone the Repository (if not already done)
```bash
git clone https://github.com/ChamilkaMihiraj2002/RevUp.git
cd RevUp
```

## Step 4: Deploy in Order

### 4.1 Create Namespaces
```bash
kubectl apply -f k8s/namespace.yaml
kubectl get namespaces
```

### 4.2 Deploy Database
```bash
kubectl apply -f k8s/database/
kubectl get pods -n revup-database -w
```

Wait for PostgreSQL to be ready:

```
postgres-0   1/1     Running   0          2m
```

Press Ctrl+C when postgres pod shows Running and 1/1 Ready.

### 4.3 Deploy Backend Configuration
```bash
kubectl apply -f k8s/backend/configmap.yaml
kubectl apply -f k8s/backend/secrets.yaml
```

### 4.4 Deploy Eureka Server First
```bash
kubectl apply -f k8s/backend/eureka-server.yaml
kubectl get pods -n revup-backend -w
```

Wait for Eureka to be ready:

```
eureka-server-xxxxx   1/1     Running   0          1m
```

Press Ctrl+C when eureka-server pod shows Running.

### 4.5 Deploy All Backend Services
```bash
kubectl apply -f k8s/backend/
kubectl get pods -n revup-backend
```

### 4.6 Deploy Frontend
```bash
kubectl apply -f k8s/frontend/
kubectl get pods -n revup-frontend
```

### 4.7 Deploy Ingress
```bash
kubectl apply -f k8s/ingress.yaml
kubectl get ingress -n revup-frontend
```

## Step 5: Access the Application

### 5.1 Port Forward Frontend (if needed)
If ingress doesn't work, port forward the frontend:
```bash
kubectl port-forward -n revup-frontend svc/frontend-service 3000:80
```

Then access: http://localhost:3000

### 5.2 Port Forward API Gateway for Testing
```bash
kubectl port-forward -n revup-backend svc/api-gateway-service 8080:8080
```

Test API endpoints:
```bash
curl http://localhost:8080/gateway/health
```

## Step 6: Check Service Registration
Open: http://localhost:8761 to see registered services.

## Step 7: View Logs (if issues occur)
```bash
# Backend services
kubectl logs -n revup-backend -f deployment/api-gateway

# Frontend
kubectl logs -n revup-frontend -f deployment/frontend

# Database
kubectl logs -n revup-database -f postgres-0
```

## Troubleshooting

### Common Issues:
- **Pods not starting**: Check resource limits, image pull errors
- **Services not registering**: Check Eureka server logs
- **Database connection**: Verify PostgreSQL is running
- **Frontend not loading**: Check ingress or port forwarding

### Reset Everything:
```bash
kubectl delete -f k8s/ingress.yaml
kubectl delete -f k8s/frontend/
kubectl delete -f k8s/backend/
kubectl delete -f k8s/database/
kubectl delete -f k8s/namespace.yaml
```

## Architecture Overview
- **Frontend**: React app served by NGINX
- **API Gateway**: Routes requests, handles authentication
- **Microservices**: User, Vehicle, Appointment, Project, Time Tracking, Chatbot
- **Eureka**: Service discovery
- **PostgreSQL**: Database
- **Ingress**: External access routing

The application should now be running at http://localhost:3000 (or your ingress domain)!