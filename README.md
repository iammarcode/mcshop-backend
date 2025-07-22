# mcshop backend

create .env file on your local machine:
```
cat << EOF > .env
# auth
SMTP_USERNAME=YOUR_SMTP_USERNAME
SMTP_PASSWORD=YOUR_SMTP_PASSWORD
EOF
```

---

## Prerequisites
- Java 17 or higher
- Maven 3.8+
- Docker & Docker Compose

## 1. Clone the repository
```
git clone <your-repo-url>
cd mcshop-backend
```

## 2. Set up environment variables
Already shown above. Make sure `.env` is present in the project root.

## 3. Run all services locally (using Maven)
Open separate terminals for each service and run:

```
# Auth Service
cd auth-service
mvn spring-boot:run
```
```
# User Service
cd user-service
mvn spring-boot:run
```
```
# Product Service
cd product-service
mvn spring-boot:run
```
```
# Order Service
cd order-service
mvn spring-boot:run
```
```
# Gateway Service
cd gateway-service
mvn spring-boot:run
```

You will also need to start MySQL and Redis. You can use Docker Compose for dependencies only:

```
docker-compose up -d mysql redis
```

## 4. Run all services with Docker Compose
To build and run everything (including dependencies):

```
docker-compose up --build
```

This will start all services and dependencies in containers.

## 5. Verify the services
- Check logs in each terminal or with `docker-compose logs -f`.
- Access the gateway (API entrypoint) at: `http://localhost:8080` (or the port specified in your configs).
- Use `docker ps` to see running containers.

---

For more details, see the `doc/` folder for architecture and endpoints.
