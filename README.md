# NammaArena - Turf Service

Turf Management Service for the NammaArena microservices architecture. Handles turf listings, sports and turf management for owners.

## Tech Stack
- Java 17
- Spring Boot 3.2.0
- Spring Security
- JWT (jjwt 0.11.5)
- Spring Data JPA
- Spring Cloud OpenFeign
- MySQL

## Port
`8082`

## Getting Started

### Prerequisites
- Java 17
- Maven 3.9+
- MySQL running on port 3306
- Eureka Server running on port 8761

### Run Locally
```bash
mvn clean package -DskipTests
java -jar target/*.jar
```

### Run with Docker
```bash
docker build -t nammaarena-turf-service .
docker run -p 8082:8082 nammaarena-turf-service
```

## API Endpoints
| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/turfs` | Get all turfs | Public |
| GET | `/api/turfs/{id}` | Get turf by ID | Public |
| POST | `/api/turfs` | Create a new turf | Owner only |
| PUT | `/api/turfs/{id}` | Update a turf | Owner only |
| DELETE | `/api/turfs/{id}` | Delete a turf | Owner only |

## Part of NammaArena Microservices
| Service | Repository |
|---|---|
| Eureka Server | [NammaArena-EurekaServer](https://github.com/VamsiK-11/NammaArena-EurekaServer) |
| API Gateway | [NammaArena-APIGateway](https://github.com/VamsiK-11/NammaArena-APIGateway) |
| Auth Service | [NammaArena-AuthService](https://github.com/VamsiK-11/NammaArena-AuthService) |
| Turf Service | [NammaArena-TurfService](https://github.com/VamsiK-11/NammaArena-TurfService) |
| Booking Service | [NammaArena-BookingService](https://github.com/VamsiK-11/NammaArena-BookingService) |
| Payment Service | [NammaArena-PaymentService](https://github.com/VamsiK-11/NammaArena-PaymentService) |
