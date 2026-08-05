# Running Locally

This repo is two independent Spring Boot services (`Order.Api`, `Payment.Api`) that talk to each other only through SNS/SQS events, backed by DynamoDB for storage. `Payment.Api` has no HTTP endpoints — it's a background SQS consumer (`web-application-type: none`).

## Prerequisites

| Tool | Version | Check |
|---|---|---|
| JDK | 21 | `java -version` |
| Maven | 3.9+ | `mvn -version` |
| Docker Desktop | any recent | `docker version` |

**Windows, if `java`/`mvn` are missing:**

```powershell
winget install --id Microsoft.OpenJDK.21 -e
```

Maven has no official winget package. Download and unzip it manually instead:

```powershell
$dest = "$env:USERPROFILE\tools"
Invoke-WebRequest "https://dlcdn.apache.org/maven/maven-3/3.9.16/binaries/apache-maven-3.9.16-bin.zip" -OutFile "$dest\apache-maven.zip"
Expand-Archive "$dest\apache-maven.zip" -DestinationPath $dest -Force
[System.Environment]::SetEnvironmentVariable("Path", "$([System.Environment]::GetEnvironmentVariable('Path','User'));$dest\apache-maven-3.9.16\bin", "User")
```

Restart your terminal afterward so the updated `PATH` takes effect.

## Option A — Everything in Docker (fastest)

From the repo root:

```powershell
docker compose up --build
```

This brings up, in order:

1. **dynamodb-local** (port `8000`) — local DynamoDB.
2. **dynamodb-setup** — one-shot init container that creates the `Orders-local` and `Payments-local` tables and seeds 5 sample orders.
3. **localstack** (port `4566`) — local SNS/SQS.
4. **localstack-setup** — one-shot init container that creates the `order-events-topic-local.fifo` / `payment-events-topic-local.fifo` SNS topics, their SQS queues, and the cross-subscriptions between `Order.Api` and `Payment.Api`.
5. **order-api** — built from `src/Order.Api/Dockerfile`, exposed on host port **8081** (container listens on 8080).
6. **payment-api** — built from `src/Payment.Api/Dockerfile`, exposed on host port **8082**, but has no HTTP surface (it only consumes SQS messages and writes to DynamoDB).

Wait for `order-api` and `payment-api` to log Spring Boot's "Started ... in N seconds" line, then verify:

```powershell
curl http://localhost:8081/orders
curl http://localhost:8081/orders/order-sample-1
```

Placing an order (`POST /orders/{id}/place`) publishes an `OrderPlaced` event that `payment-api` consumes and processes; watch the `payment-api` container logs to see it react.

Stop everything:

```powershell
docker compose down
```

Add `-v` to also drop the `dynamodb-data` volume (wipes seeded data) — this is a destructive operation.

## Option B — Infra in Docker, apps run locally (for debugging in an IDE)

Start only the infrastructure services:

```powershell
docker compose up dynamodb-local dynamodb-setup localstack localstack-setup
```

The local-only settings (table names, LocalStack endpoint, topic ARNs, queue URLs, fake AWS creds) are checked in as a Spring `local` profile, so you don't need to export them by hand each session:

- `src/Order.Api/src/main/resources/application-local.yml`
- `src/Payment.Api/src/main/resources/application-local.yml`

Activate the profile with whichever of these fits your workflow:

**VS Code** — press `F5` (or use the "Run and Debug" panel). `.vscode/launch.json` already has `Order.Api (local)` and `Payment.Api (local)` configurations wired to `-Dspring.profiles.active=local`. Requires the [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack).

**Maven CLI:**

```powershell
cd src\Order.Api
mvn spring-boot:run "-Dspring-boot.run.profiles=local"
```

```powershell
cd src\Payment.Api
mvn spring-boot:run "-Dspring-boot.run.profiles=local"
```

**IntelliJ IDEA** — open the Run/Debug Configuration for `OrderApiApplication` / `PaymentApiApplication` and set the "Active profiles" field (Modify Options → Add VM options if not shown by default) to `local`.

**Packaged jar:**

```powershell
java -jar target\order-api.jar --spring.profiles.active=local
```

`Order.Api` runs on `http://localhost:8081` (set via `server.port` in its `application-local.yml`, distinct from the `8080` docker-compose maps internally). `Payment.Api` has no HTTP port — it's ready once you see "Started PaymentApiApplication" in the logs.

If you'd rather not touch profile files at all, the raw `$env:` variable route from before still works — just set them in the shell before `mvn spring-boot:run` (no `-Dspring-boot.run.profiles` needed in that case).

## Building without running

From the repo root (builds both modules via the aggregator `pom.xml`):

```powershell
mvn compile
mvn package -DskipTests
```

## Troubleshooting

- **`mvn`/`java` not recognized`** — see Prerequisites above; also restart your terminal after installing so `PATH` updates take effect.
- **Port already in use (`8000`, `4566`, `8081`, `8082`)** — another instance of this stack (or another project) is already running; `docker compose down` it first.
- **`payment-api` never reacts to placed orders** — check `localstack-setup` logs completed successfully (it must finish before `order-api`/`payment-api` start due to `depends_on: condition: service_completed_successfully`); a failed subscription step means events silently go nowhere.
