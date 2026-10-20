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

## Supported platforms

Any OS with JDK 21: **Linux** and **macOS** use `start.sh`, **Windows** uses `start.bat` (or the same `mvn` and `java` commands in PowerShell). CI builds on Ubuntu.

## Requirements

- **JDK 21** (`java -version` must report 21 or later)
- **Maven 3.8+** (`mvn -version`)
- `unzip`, and about 200 MB of free disk space for the accommodation dataset
- Port **8080** free
- Optional: an SMTP account to actually send e-mail, and [Postman](https://www.postman.com/) to try the API

## Install and run

Run everything from the repository root: the API reads `config.trek-ul.yaml` and its data files (`hebergement.xml`, `supported_regions.json`, `campus-departure-location.json`) from the current directory.

```bash
git clone https://github.com/alir961/trekul-hiking-booking-platform.git
cd trekul-hiking-booking-platform
unzip Hebergement.zip            # Ministry of Tourism accommodation dataset: hebergement.xml (~180 MB, git-ignored)
                                 # Windows PowerShell: Expand-Archive Hebergement.zip -DestinationPath .
./start.sh                       # Windows: start.bat
```

`start.sh` runs `mvn clean verify` (build plus the whole unit and integration test suite, a few minutes the first time) and then starts the API with `java -jar api/target/application.jar`. Once it is built, start it directly with that `java -jar` command, or rebuild without the tests with `mvn clean package -DskipTests -DskipITs`. The jar needs the `api/target/lib/` folder next to it. Stop the API with Ctrl+C.

The API won't start without `hebergement.xml`: it fails with a `FileNotFoundException` from `LocalFileHostingExtractor`.

The API listens on **http://localhost:8080**:

- endpoints under `/api/trekul/...`: users and login, trips and their transports, reservations, groups, invoices and payment
- the OpenAPI description at `GET /api` (generated from the Swagger annotations at build time); paste it into https://editor.swagger.io to browse it
- a ready-made Postman collection: `TrekUl-eq6.postman_collection.json` (Import it in Postman)

```bash
curl http://localhost:8080/api                                    # OpenAPI description
curl "http://localhost:8080/api/trekul/trips?startDate=2025-01-01&endDate=2025-12-31&priceLimit=500"   # search trips
```

Set your own SMTP credentials in `config.trek-ul.yaml` to send e-mail (confirmations, 2FA codes, trip reminders). The committed values are placeholders: the API runs without them, but sending an e-mail fails. The same file holds the business settings (prices, seats, expiration delays, scheduler intervals).

### Useful commands

| Command | Purpose |
|---|---|
| `mvn test` | Unit tests |
| `mvn verify` | Unit and integration tests, then packages the jar |
| `mvn test --fail-at-end jacoco:prepare-agent verify jacoco:report-aggregate` | Coverage report |
| `mvn spotless:apply` | Auto-format |
| `mvn checkstyle:check` | Style check |
