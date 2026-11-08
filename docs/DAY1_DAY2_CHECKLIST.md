# Day 1-2 checklist

## Day 1
- [ ] `docker compose up -d` (Postgres) and `mvn spring-boot:run` (Flyway migrates the schema)
- [ ] Run the OpenTelemetry Demo locally (official repo, Docker Compose). Check your RAM first.
- [ ] Add a file exporter to the demo Collector config (confirm current exporter name/options in the Collector docs)
- [ ] Record ~10 min baseline, flip ONE feature flag, record ~10 min more
- [ ] Write the flag-flip timestamp immediately into `bundles/<name>/scenario.yaml`

## Day 2
- [ ] Capture 2 more faults (one error spike, one slow dependency)
- [ ] Check telemetry size; shorten windows or lower load if huge
- [ ] DECIDE: recorded vs synthetic data (end of Day 2)

## Candidate demo feature flags (from the OpenTelemetry Demo docs)
| Category | Flags |
|---|---|
| ERROR_SPIKE | adServiceFailure, cartServiceFailure, productCatalogFailure, paymentServiceFailure |
| SLOW_DEPENDENCY | imageSlowLoad, kafkaQueueProblems, paymentServiceUnreachable |
| RESOURCE_SATURATION | adServiceHighCpu, emailMemoryLeak, recommendationServiceCacheFailure |
| TRAFFIC_SURGE | loadgeneratorFloodHomepage |
| CONFIG_OR_DEPLOY_CHANGE | no native flag: make 1-2 synthetic scenarios or drop |

Also record one no-fault run and one run with two flags enabled together.
Re-check the flag names against the current docs before relying on them.
