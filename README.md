# app

Multi-module Java + Spring Boot + Spring Cloud microservices project.

- **groupId**: `com.example`
- **Modules**:
- **eureka-server** (EUREKA_SERVER) — http://localhost:28145
- **gateway-service** (GATEWAY) — http://localhost:29875
- **inventory-service** (BUSINESS_SERVICE) — http://localhost:22482
- **cart-service** (BUSINESS_SERVICE) — http://localhost:21954
- **catalog-service** (BUSINESS_SERVICE) — http://localhost:29838
- **order-service** (BUSINESS_SERVICE) — http://localhost:22354
- **user-service** (BUSINESS_SERVICE) — http://localhost:26642

## Build

```bash
mvn clean install -DskipTests
```

## Run (in separate terminals, in this order)

```bash
cd eureka-server && mvn spring-boot:run   # port 28145
cd inventory-service && mvn spring-boot:run   # port 22482
cd cart-service && mvn spring-boot:run   # port 21954
cd catalog-service && mvn spring-boot:run   # port 29838
cd order-service && mvn spring-boot:run   # port 22354
cd user-service && mvn spring-boot:run   # port 26642
cd gateway-service && mvn spring-boot:run   # port 29875
```

## Access the API

**Call the services through the gateway — that's the intended entry point,
not each service's own port.** The gateway (gateway-service, port 29875)
discovers every registered service via Eureka and routes to it by lower-cased
service name:

- **inventory-service**: `http://localhost:29875/inventory-service/api/v1/...`
- **cart-service**: `http://localhost:29875/cart-service/api/v1/...`
- **catalog-service**: `http://localhost:29875/catalog-service/api/v1/...`
- **order-service**: `http://localhost:29875/order-service/api/v1/...`
- **user-service**: `http://localhost:29875/user-service/api/v1/...`

- **Aggregated Swagger docs**: `http://localhost:29875/docs`

Every business service's own `http://localhost:<port>` listed above under
Modules is reachable directly too (and Docker Compose publishes it), but
that's there for local debugging one module in isolation — a real client, or
anything calling more than one service, should go through the gateway so
routing, discovery and any cross-cutting gateway config stay in one place.
