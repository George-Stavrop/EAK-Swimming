# EAK Swimming Club Management System

A microservices-based backend system for managing a swimming club. Built with Spring Boot and Spring Cloud, featuring full observability, event-driven communication, and containerized deployment.

---

## Features

- **Membership Management** — Register members, track medical certificates
- **Subscription Management** — Create and renew subscriptions, automatic expiry notifications via email
- **Access Control** — Issue access cards, validate entry based on active subscription
- **Email Notifications** — Automated emails for new memberships, expiring and expired subscriptions
- **Observability** — Distributed tracing, metrics, and log aggregation via Grafana stack
- **Security** — OAuth2/JWT authentication via Keycloak

---

## Architecture

The system is composed of the following services:

| Service | Description                    | Port |
|---|--------------------------------|---|
| `config-server` | Centralized configuration server | 8071 |
| `eureka-server` | Service discovery              | 8070 |
| `gateway-server` | API Gateway with OAuth2 security | 8072 |
| `membership-service` | Member and membership management | 8080 |
| `subscription-service` | Subscription management        | 8090 |
| `access-service` | Access card and entry validation | 9000 |
| `message-service` | Email notifications via RabbitMQ | 9010 |


### Architecture Image ###

### Communication

- **Synchronous** — REST via OpenFeign (access-service → subscription-service, subscription-service → membership-service)
- **Asynchronous** — Event-driven via RabbitMQ + Spring Cloud Stream (membership/subscription → message-service)

---

## Tech Stack

### Backend
- Java 21
- Spring Boot 3.5
- Spring Cloud 2025 (Config, Eureka, Gateway, OpenFeign, Stream)
- Spring Data JPA
- Spring Security + OAuth2 Resource Server
- Resilience4j (Circuit Breaker)
- MapStruct
- Springdoc OpenAPI (Swagger UI)

### Infrastructure
- MySQL 8 (production)
- H2 (development)
- RabbitMQ
- Keycloak 26

### Observability
- Grafana
- Loki (log aggregation)
- Tempo (distributed tracing)
- Prometheus (metrics)
- Grafana Alloy (log collector)
- OpenTelemetry Java Agent

### DevOps
- Docker + Docker Compose
- Helm
- Kubernetes
- GitHub Actions (CI/CD)

---

## Getting Started

### Prerequisites
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

### Run Modes

**Development** — Lightweight, no MySQL/Keycloak, all ports exposed, security disabled
```bash
docker compose -f docker-compose/dev/compose-dev.yml up -d
```

**Production** — With MySQL, Keycloak, services only accessed through the Gateway server
```bash
docker compose -f docker-compose/default/compose.yml up
```

**Production + Observability** (Grafana, Loki, Tempo, Prometheus)
```bash
docker compose -f docker-compose/default/compose.yml \
               -f docker-compose/default/compose-observability.yml up
```

## API Testing (Postman)

Import the collection from the `postman/` folder to explore and test all available endpoints.

| File | Description |
|---|---|
| `EAK-Swimming.postman_collection.json` | All requests |

The collection contains 5 folders:

| Folder | Description |
|---|---|
| `Membership` | Direct access to membership-service (dev only) |
| `Subscription` | Direct access to subscription-service (dev only) |
| `Access` | Direct access to access-service (dev only) |
| `gatewayserver_noAuth` | All services via Gateway — no auth (dev only) |
| `gatewayserver_auth` | All services via Gateway — Keycloak Auth Code Flow (production) |

> For `gatewayserver_auth`: open the folder → Authorization → **Get New Access Token**.  
> You will be redirected to Keycloak. Sign in with:  
> **Username:** grammateas1 | **Password:** 12345


---

## API Documentation (Swagger UI)

| Service | URL |
|---|---|
| membership-service | http://localhost:8080/swagger-ui/index.html |
| subscription-service | http://localhost:8090/swagger-ui/index.html |
| access-service | http://localhost:9000/swagger-ui/index.html |


---


## Key Flows

### Member Registration
1. `POST /api/create` → creates Member + Membership
2. Publishes event to RabbitMQ
3. `message-service` sends welcome email

### Subscription Expiry
1. Scheduler runs daily at 09:00
2. Finds subscriptions expiring in 5 days → sends warning email
3. Finds subscriptions expired yesterday → sends expired email

### Access Validation
1. `GET /api/can-enter/{accessCardNumber}`
2. Fetches access card → calls subscription-service via Feign
3. Grants or refuses access depending on if the Subscription is active or not

---

## Project Structure
```
EAK-Swimming/
├── membership-service/
├── subscription-service/
├── access-service/
├── message-service/
├── gatewayserver/
├── configserver/
├── eurekaserver/
├── eak-bom/                  # Parent BOM for dependency management
├── docker-compose/
│   ├── common-config.yml
│   ├── default/              # Production compose files
│   ├── dev/                  # Development compose files
│   └── observability/        # Observability config files
├── helm/                     # Helm charts for Kubernetes
├── kubernetes/               # Raw Kubernetes manifests
└── mysql/
    └── init.sql
```
--- 

## Observability

After starting with the observability profile, access Grafana at:

```
http://localhost:3000
```

> No login required (anonymous access enabled)

![Grafana Dashboard](docs/screenshots/grafana.png)


---

## CI/CD

Each service has its own GitHub Actions workflow that triggers on changes to its folder or the `eak-bom`. The pipeline:

1. Installs the shared BOM
2. Builds and tests the service
3. Builds and pushes Docker image to Docker Hub (tagged with commit SHA and `latest`)

---

## Author

George Stavropoulos — [GitHub](https://github.com/George-Stavrop)


## License

This project is created for educational and portfolio purposes.
