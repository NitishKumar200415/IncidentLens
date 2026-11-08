# IncidentLens

Evidence-grounded AI incident investigator over OpenTelemetry data.
An LLM agent queries bounded tools over traces and metrics, proposes ranked root-cause
hypotheses, and every claim must cite stored evidence. An eval harness measures accuracy
against recorded fault scenarios and a non-AI heuristic baseline.

> Status: planning / scaffold. Not production-ready. Results will be added after measurement.

## Quick start
Requirements: Java 21, Maven 3.9+, Docker.

```bash
docker compose up -d          # PostgreSQL
mvn verify                    # compile + run unit tests
mvn spring-boot:run           # starts app, Flyway applies V1__init_schema.sql
curl localhost:8080/actuator/health
```

## Layout
- `src/main/java/com/incidentlens/` - modules: domain, ingestion, telemetry, tools, agent, llm, report, eval, observability, api
- `src/main/resources/db/migration/` - Flyway migrations
- `bundles/` - incident scenarios (scenario.yaml + telemetry)
- `docs/` - project document (PDF) and day-by-day checklist

## Roadmap
See `docs/PROJECT_DOCUMENT.pdf`, section 22 (21-day plan). Start with `docs/DAY1_DAY2_CHECKLIST.md`.

## Known limitations of this scaffold
Generated without being compiled in the authoring environment (no access to Maven Central).
If `mvn verify` fails, fix or report the error before building on top of it.
