# TrekUL — Hiking Trip Booking Platform (Java · DDD · TDD)

A REST API for booking complete hiking trips in Québec: departure transport, accommodation, hiking activities and the return trip. It was built with a strict **layered / Domain-Driven Design** architecture and **test-driven development**.

> Team project — software architecture course (GLO-4003), Université Laval.

## Highlights

- **Clean layered architecture** as separate Maven modules, with dependencies pointing inward:
  `api` (JAX-RS resources, DTOs, exception mappers) → `service` (use cases) → `domain` (trips, travelers, transport, hiking, payment, filters) ← `infrastructure` (persistence, configuration, external data)
- **150+ test classes**: JUnit 5, Mockito and AssertJ for unit tests, REST-assured for API integration tests
- **Quality gates**: JaCoCo coverage, Checkstyle (Google-based rules) and Spotless/Cleanthat formatting
- **API documentation** generated from Swagger annotations at build time
- **Real open-data ingestion**: parses and validates the Québec Ministry of Tourism accommodation dataset (300K+ records, XML against the official XSD) plus regional trek data

## Tech stack

Java · JAX-RS (Jersey on Jetty) · Jackson · Maven (multi-module) · JUnit 5 · Mockito · REST-assured · JaCoCo · Swagger

## Run

Requirements: JDK 21, Maven.

```bash
unzip Hebergement.zip            # Ministry of Tourism accommodation dataset
mvn verify                       # tests, coverage, checkstyle
./start.sh                       # or start.bat on Windows
```

Set your own SMTP credentials in `config.trek-ul.yaml` before sending e-mail. The value committed here is a placeholder.

### Useful commands

| Command | Purpose |
|---|---|
| `mvn test` | Unit tests |
| `mvn verify` | Unit and integration tests |
| `mvn test --fail-at-end jacoco:prepare-agent verify jacoco:report-aggregate` | Coverage report |
| `mvn spotless:apply` | Auto-format |
| `mvn checkstyle:check` | Style check |

A Postman collection is included: `TrekUl-eq6.postman_collection.json`.
