# RevUp (AutoCare)

A microservice example project for an automotive service platform. RevUp contains multiple backend services (Spring Boot), a React + Vite frontend, and Kubernetes manifests for deployment. This README provides a quick overview, local development tips, and deployment notes for GitHub visitors.

## Contents

- `backend/services/` - Multiple Spring Boot microservices (API gateway, user, vehicle, appointment, project, chatbot, time-tracking, Eureka server). Each service contains a Maven wrapper (`mvnw`, `mvnw.cmd`).
- `frontend/` - React + Vite frontend application.
- `k8s/` - Kubernetes manifests (namespace, backend services, database/statefulset, frontend, ingress, secrets, configmaps).
- `k8s/database/init-configmap.yaml` - DB init SQL (creates per-service databases and users).

## Quick links

- Kubernetes manifests: `k8s/`
- Docker Compose for local testing (subset): `backend/services/docker-compose.yml`

## Prerequisites

- Java 17+ and Maven (or use the included wrapper `mvnw` / `mvnw.cmd`)
- Node.js 18+ and npm/yarn for the frontend
- Docker & Docker Compose (for local container runs)
- kubectl and access to a Kubernetes cluster (for k8s deployments)

## Local (Docker Compose) - quick start

1. From the repo root, go to the compose folder:

```powershell
cd backend/services
docker-compose up --build
```

2. Wait for services to start. Services expose ports as defined in `docker-compose.yml` (e.g., API gateway, user, vehicle, etc.).

Notes: The compose file in `backend/services/docker-compose.yml` is intended for local development and references the service Dockerfiles in the sibling service folders.

## Frontend - run locally

1. Install dependencies and start dev server:

```powershell
cd frontend
npm install
npm run dev
```

2. Production build:

```powershell
npm run build
npm run preview
```

## Backend - build and run (each service)

Each backend service is a Spring Boot app with a Maven wrapper. Example (Windows PowerShell):

```powershell
cd backend/services/api-gateway
./mvnw.cmd clean package
java -jar target/<artifact>.jar
```

On Unix/macOS use `./mvnw` instead of `./mvnw.cmd`.

## Kubernetes deployment (high-level)

1. Create the namespace and CRs (apply in order):

```powershell
# create namespace
kubectl apply -f k8s/namespace.yaml

# apply database resources
kubectl apply -f k8s/database/init-configmap.yaml
kubectl apply -f k8s/database/secrets.yaml
kubectl apply -f k8s/database/statefulset.yaml
kubectl apply -f k8s/database/service.yaml

# apply backend services (api-gateway, eureka, user, vehicle, appointment, ...)
kubectl apply -f k8s/backend/

# apply frontend
kubectl apply -f k8s/frontend/
```

Adjust the order if you have additional prerequisites (for example ensure the DB StatefulSet and PVCs are ready before backend deployments).

## Database notes

- The PostgreSQL StatefulSet mounts a `ConfigMap` at `/docker-entrypoint-initdb.d` (`k8s/database/init-configmap.yaml`) which runs the included `init.sql` to create service databases and users (for example `appointment_db` and `appointment_service` user).
- Credentials for PostgreSQL are stored in `k8s/database/secrets.yaml` (check and update before using in production).

If you need to update a single appointment record's `scheduled_start` to the current time (useful during testing), use the following command against the Postgres pod (replace the pod name if different):

```powershell
# Example: update the first appointment's start time to now
kubectl exec -it postgres-0 -n revup-database -- psql -U revup_admin -d appointment_db -c "UPDATE appointments SET scheduled_start = NOW(), updated_at = NOW() WHERE appointment_id = (SELECT appointment_id FROM appointments ORDER BY appointment_id LIMIT 1);"

# Or, update a specific appointment ID (e.g., id = 1):
kubectl exec -it postgres-0 -n revup-database -- psql -U revup_admin -d appointment_db -c "UPDATE appointments SET scheduled_start = NOW(), updated_at = NOW() WHERE appointment_id = 1;"
```

Note: The correct DB user in this setup is `revup_admin` (not `postgres`). Confirm secrets in `k8s/database/secrets.yaml` if your cluster was customized.

## Tests & CI

- Backend services contain unit and integration tests under `src/test` and typical Maven test lifecycle. Run tests via the wrapper:

```powershell
./mvnw.cmd test
```

## Contributing

- Please open issues or PRs against the `dev` branch.
- Add clear descriptions and small, focused commits. Include tests for behavioral changes.

## License

This repository includes a `LICENSE` file at the project root. Review it for permitted usage.

## Contact / Maintainers

For questions, open an issue or contact the repository owner.

---

If you'd like, I can also add a short `CONTRIBUTING.md`, CI workflow (GitHub Actions) skeleton, and badges—tell me which you prefer and I'll add them.
