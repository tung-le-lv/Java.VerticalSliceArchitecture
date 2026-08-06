# Must-Know Libraries — .NET / C# ↔ Java

A mapping of the everyday .NET libraries to their Java ecosystem equivalents, so your C# instincts transfer.

**Legend:** ⚠️ = went commercial / dual-license in 2025 (free tier usually exists for small or non-commercial use). ≈ = close analog, not a drop-in.

> **Framework-level orientation:** **ASP.NET Core ≈ Spring Boot**. .NET has **dependency injection built in** (`Microsoft.Extensions.DependencyInjection`); in Java that role is filled by **Spring** (or Jakarta CDI / Guice). Keep that in mind — several .NET "libraries" are just parts of Spring on the Java side.

---

## Master comparison table

| Category | Purpose | .NET / C# | Java | Notes |
|---|---|---|---|---|
| **ORM** | Full-featured ORM | **EF Core** | **Hibernate / JPA** (usually via **Spring Data JPA**) | Direct analog. `DbContext` ≈ `EntityManager` / a Spring Data repository. |
| **Data access** | Lightweight "write your own SQL" | **Dapper** | **JdbcTemplate** (Spring) · **JDBI** · **MyBatis** · **jOOQ** | jOOQ adds type-safe SQL DSL — no exact .NET twin. |
| **Validation** | Declarative model validation | **FluentValidation** | **Hibernate Validator** (Jakarta Bean Validation) + Spring `@Valid` | Java is annotation-first (`@NotNull`, `@Size`); FluentValidation is fluent/separate. |
| **Mapping** | Object-to-object mapping | ⚠️ **AutoMapper** · **Mapperly** · **Mapster** | **MapStruct** · **ModelMapper** · **Orika** | **MapStruct ≈ Mapperly** (both compile-time source-gen). ModelMapper ≈ classic reflection AutoMapper. |
| **Mediator / CQRS** | In-process messaging, CQRS | ⚠️ **MediatR** · **Wolverine** · **Mediator** | Spring **ApplicationEventPublisher** (events) · **Axon Framework** (full CQRS/ES) · **Spring Modulith** | No 1:1 mediator. Simple in-process = Spring events; heavyweight CQRS/event-sourcing = Axon. |
| **Resilience** | Retry, circuit breaker, timeout | **Polly** (+ `Microsoft.Extensions.Http.Resilience`) | **Resilience4j** | Direct modern analog. (Netflix Hystrix is the deprecated ancestor.) |
| **Logging** | Structured logging | **Serilog** | **SLF4J** (facade) + **Logback** or **Log4j 2** | SLF4J = the API; Logback/Log4j2 = the implementation. Structured output via logstash-logback-encoder. |
| **JSON** | Serialization | **System.Text.Json** (default) · **Newtonsoft.Json** | **Jackson** (standard) · **Gson** · **JSON-B** | Jackson is the near-universal default, like STJ. |
| **Test framework** | Unit test runner | **xUnit** · **NUnit** · **TUnit** | **JUnit 5** (Jupiter) · **TestNG** | JUnit 5 is the default choice. |
| **Mocking** | Test doubles | **NSubstitute** · **Moq** | **Mockito** | Mockito is the undisputed standard. |
| **Assertions** | Fluent assertions | ⚠️ **FluentAssertions** · **Shouldly** | **AssertJ** · **Hamcrest** | `assertThat(x).isEqualTo(...)` — AssertJ ≈ FluentAssertions. |
| **Test data** | Fake / random data | **Bogus** | **Datafaker** (fork of JavaFaker) · **Instancio** | Same idea: generate realistic fake objects. |
| **Integration tests** | Real deps in Docker | **Testcontainers** (.NET port) | **Testcontainers** | **Same project** — Testcontainers *originated* in Java. |
| **Messaging / bus** | Message bus, async comms | ⚠️ **MassTransit** · **Wolverine** · **Rebus** | Spring **Kafka** / Spring **AMQP** · **Spring Cloud Stream** · **Apache Camel** · **Axon** | Spring wraps the brokers; Camel = integration/routing (EIP). |
| **Raw broker client** | Direct queue access | `RabbitMQ.Client` · `Azure.Messaging.ServiceBus` | **RabbitMQ Java Client** · **Kafka Clients** | When you don't want a framework. |
| **Background jobs** | Fire-and-forget + dashboard | **Hangfire** | **JobRunr** | JobRunr ≈ Hangfire (dashboard, persistent jobs) almost feature-for-feature. |
| **Scheduling** | Cron-style jobs | **Quartz.NET** | **Quartz** · Spring `@Scheduled` · **Spring Batch** | Quartz.NET is literally a **port of Java Quartz**. |
| **Typed HTTP client** | Interface-based REST client | **Refit** | **OpenFeign** · **Retrofit** (Square) · Spring 6 **`@HttpExchange`** | Declare an interface, get an HTTP client. Refit ≈ Retrofit/Feign. |
| **Web / API layer** | Build HTTP endpoints | **ASP.NET Core** (Minimal APIs, Controllers) · **FastEndpoints** | **Spring MVC** / **Spring WebFlux** · **Jakarta REST (JAX-RS)** · **Javalin** · **Micronaut** | FastEndpoints' lean style ≈ Javalin / Micronaut. |
| **API docs (OpenAPI)** | Generate + serve Swagger UI | **Swashbuckle** · **NSwag** · **Scalar** (UI) | **springdoc-openapi** (Swagger UI for Spring) · **Swagger UI** | .NET 9+ ships OpenAPI gen but dropped Swagger UI from templates; springdoc covers both on Java. |
| **Auth / Identity** | OAuth2 / OIDC server | **OpenIddict** · ⚠️ **Duende IdentityServer** | **Spring Authorization Server** · **Keycloak** · **Nimbus JOSE+JWT** | Keycloak = self-hosted identity server (often run standalone). |
| **DI container** | Dependency injection | *built into .NET* (`Microsoft.Extensions.DependencyInjection`) | **Spring** · Jakarta **CDI** · **Guice** | Biggest structural difference — DI is a first-class framework in Java, built-in in .NET. |
| **Humanize** | Human-friendly strings/dates | **Humanizer** | **PrettyTime** · **ICU4J** | "2 hours ago", pluralization, etc. |

---

## Quick takeaways

- **Spring absorbs a lot.** Where .NET reaches for a discrete NuGet package (DI, messaging, HTTP clients, scheduling), Java often reaches for a **Spring module**. So "which library?" in .NET frequently becomes "which Spring starter?" in Java.
- **Shared lineage in a few places.** *Testcontainers* is the same project on both sides; *Quartz.NET* is a port of Java's *Quartz*. Familiar ground.
- **Compile-time mapping is the modern norm on both.** Mapperly (C#) and MapStruct (Java) both use source generation / annotation processing — faster and safer than reflection-based mapping, and a better default than the reflection-era AutoMapper/ModelMapper.
- **The licensing shift is .NET-specific.** The 2025 commercial moves (⚠️ AutoMapper, MediatR, MassTransit, FluentAssertions) are a .NET-ecosystem story. Their Java analogs (MapStruct, Spring events/Axon, Spring messaging, AssertJ) remain fully open source.
- **CQRS/mediator is the weakest 1:1 mapping.** There's no MediatR-shaped default in Java; you compose it from Spring events, or adopt Axon if you genuinely need CQRS + event sourcing — which, per the "don't over-engineer" principle, most projects don't.

---

*Versions in scope: .NET 10 / C# 14 (Nov 2025) · Java 25 (Sept 2025), Spring Boot 3.x. ⚠️ marks .NET libraries that adopted commercial/dual licensing in 2025.*
