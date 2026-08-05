# Running Locally
## Prerequisites

| Tool | Version | Check |
|---|---|---|
| JDK | 21 | `java -version` |
| Maven | 3.9+ | `mvn -version` |
| Docker Desktop | any recent | `docker version` |

## Build

From the repo root (builds both modules via the aggregator `pom.xml`):

```powershell
mvn compile
mvn package -DskipTests
```

## Run

Start only the infrastructure services:

```powershell
docker compose up dynamodb-local dynamodb-setup localstack localstack-setup
```

The local-only settings are checked in as a Spring `local` profile, so you don't need to export them by hand each session:

- `src/Order.Api/src/main/resources/application-local.yml`
- `src/Payment.Api/src/main/resources/application-local.yml`

**Maven CLI:**

```powershell
cd src\Order.Api
mvn spring-boot:run "-Dspring-boot.run.profiles=local"
```

```powershell
cd src\Payment.Api
mvn spring-boot:run "-Dspring-boot.run.profiles=local"
```