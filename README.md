# Order Processing System

A real-time order processing system built with **Apache Kafka** and **Spring Boot microservices**. Demonstrates event-driven architecture similar to how e-commerce platforms like Amazon process orders at scale.

---

## Architecture

```
Client (REST)
     ↓
Order Service (Producer)
     ↓
Apache Kafka — topic: order-placed
     ↓              ↓
Inventory       Notification
Service         Service
(Consumer)      (Consumer)
     ↓
PostgreSQL
```

When a customer places an order:
1. **Order Service** receives the request and publishes an event to Kafka
2. **Inventory Service** consumes the event and updates stock in PostgreSQL
3. **Notification Service** consumes the same event and sends an order confirmation

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 25 | Programming language |
| Spring Boot 4.0.5 | Application framework |
| Apache Kafka | Event streaming / message broker |
| Spring Kafka | Kafka integration for Spring Boot |
| PostgreSQL | Inventory database |
| Spring Data JPA | Database access layer |
| Flyway | Database schema migrations |
| Lombok | Boilerplate reduction |
| Testcontainers | Integration testing with real services |
| Docker Compose | Local infrastructure setup |
| Maven (Multi-module) | Project structure and build |

---

## Project Structure

```
order-system/
├── pom.xml                          # Parent POM
├── common/                          # Shared models (Order)
├── order-service/                   # REST API + Kafka Producer
├── inventory-service/               # Kafka Consumer + PostgreSQL
└── notification-service/            # Kafka Consumer + Notifications
```

---

## Prerequisites

- Java 25
- Maven 3.9+
- Docker and Docker Compose

---

## Running Locally

### 1. Start infrastructure

```bash
docker compose up -d
```

This starts:
- Apache Kafka on port `9092`
- PostgreSQL on port `5432`

### 2. Start the services

Open 3 separate terminals:

```bash
# Terminal 1
mvn spring-boot:run -pl order-service

# Terminal 2
mvn spring-boot:run -pl inventory-service

# Terminal 3
mvn spring-boot:run -pl notification-service
```

| Service | Port |
|---|---|
| Order Service | 8081 |
| Inventory Service | 8082 |
| Notification Service | 8083 |

### 3. Place an order

```bash
curl -X POST http://localhost:8081/orders \
  -H "Content-Type: application/json" \
  -d '{"product": "Laptop", "quantity": 2}'
```

### 4. Verify stock update

```bash
docker exec -it postgres psql -U admin -d inventory -c "SELECT * FROM inventory;"
```

---

## API Reference

### Place Order

```
POST /orders
```

**Request body:**

```json
{
  "product": "Laptop",
  "quantity": 2
}
```

**Response:**

```
Order placed successfully!
```

---

## Database

The inventory database is pre-seeded with:

| Product | Initial Stock |
|---|---|
| Laptop | 100 |
| Phone | 200 |
| Tablet | 150 |

Schema is managed by Flyway migrations in `inventory-service/src/main/resources/db/migration/`.

---

## Key Features

- **Event-driven architecture** — services communicate through Kafka topics, not direct calls
- **Decoupled microservices** — each service is independent and can scale separately
- **Consumer groups** — inventory and notification services use different group IDs, so both receive every message
- **JSON serialization** — messages are serialized as JSON using Jackson
- **Database migrations** — Flyway manages schema changes in a version-controlled way
- **Multi-module Maven** — shared code lives in the `common` module

---

## Future Improvements

- Add Spring Security for API authentication
- Implement dead letter queue for failed message processing
- Add distributed tracing with OpenTelemetry
- Containerize all services with Docker
