-- IncidentLens core schema (MVP)

CREATE TABLE bundles (
    id            BIGSERIAL PRIMARY KEY,
    name          TEXT NOT NULL UNIQUE,
    source        TEXT NOT NULL CHECK (source IN ('RECORDED', 'SYNTHETIC')),
    window_start  TIMESTAMPTZ NOT NULL,
    window_end    TIMESTAMPTZ NOT NULL
);

CREATE TABLE spans (
    bundle_id      BIGINT NOT NULL REFERENCES bundles(id) ON DELETE CASCADE,
    trace_id       TEXT NOT NULL,
    span_id        TEXT NOT NULL,
    parent_span_id TEXT,
    service        TEXT NOT NULL,
    name           TEXT NOT NULL,
    start_ts       TIMESTAMPTZ NOT NULL,
    duration_ns    BIGINT NOT NULL,
    status_code    TEXT,
    attributes     JSONB NOT NULL DEFAULT '{}'::jsonb,
    PRIMARY KEY (bundle_id, trace_id, span_id)
);
CREATE INDEX idx_spans_service_time ON spans (bundle_id, service, start_ts);
CREATE INDEX idx_spans_trace ON spans (bundle_id, trace_id);

CREATE TABLE metric_points (
    bundle_id    BIGINT NOT NULL REFERENCES bundles(id) ON DELETE CASCADE,
    service      TEXT NOT NULL,
    metric_name  TEXT NOT NULL,
    ts           TIMESTAMPTZ NOT NULL,
    value        DOUBLE PRECISION NOT NULL,
    labels       JSONB NOT NULL DEFAULT '{}'::jsonb
);
CREATE INDEX idx_metrics_lookup ON metric_points (bundle_id, service, metric_name, ts);

CREATE TABLE service_edges (
    bundle_id   BIGINT NOT NULL REFERENCES bundles(id) ON DELETE CASCADE,
    caller      TEXT NOT NULL,
    callee      TEXT NOT NULL,
    call_count  BIGINT NOT NULL,
    error_count BIGINT NOT NULL,
    p95_ns      BIGINT,
    PRIMARY KEY (bundle_id, caller, callee)
);

CREATE TABLE investigations (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    bundle_id       BIGINT NOT NULL REFERENCES bundles(id),
    alert_text      TEXT NOT NULL,
    idempotency_key TEXT NOT NULL UNIQUE,
    status          TEXT NOT NULL CHECK (status IN ('PENDING','RUNNING','COMPLETED','FAILED','INCOMPLETE')),
    started_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    finished_at     TIMESTAMPTZ,
    tokens_in       BIGINT NOT NULL DEFAULT 0,
    tokens_out      BIGINT NOT NULL DEFAULT 0,
    cost_estimate   NUMERIC(12,6)
);

CREATE TABLE investigation_steps (
    investigation_id UUID NOT NULL REFERENCES investigations(id) ON DELETE CASCADE,
    seq              INT NOT NULL,
    type             TEXT NOT NULL CHECK (type IN ('LLM_CALL','TOOL_CALL')),
    request          JSONB,
    response         JSONB,
    latency_ms       BIGINT,
    PRIMARY KEY (investigation_id, seq)
);

CREATE TABLE evidence_items (
    investigation_id  UUID NOT NULL REFERENCES investigations(id) ON DELETE CASCADE,
    evidence_id       TEXT NOT NULL,
    tool_name         TEXT NOT NULL,
    arguments         JSONB,
    result_summary    TEXT NOT NULL,
    structured_values JSONB NOT NULL DEFAULT '{}'::jsonb,
    PRIMARY KEY (investigation_id, evidence_id)
);

CREATE TABLE reports (
    investigation_id  UUID PRIMARY KEY REFERENCES investigations(id) ON DELETE CASCADE,
    report_json       JSONB NOT NULL,
    validation_status TEXT NOT NULL
);

CREATE TABLE eval_scenarios (
    id                       BIGSERIAL PRIMARY KEY,
    bundle_id                BIGINT NOT NULL REFERENCES bundles(id),
    ground_truth_service     TEXT NOT NULL,
    ground_truth_category    TEXT NOT NULL,
    held_out                 BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE eval_runs (
    id             BIGSERIAL PRIMARY KEY,
    model          TEXT NOT NULL,
    prompt_version TEXT NOT NULL,
    tool_version   TEXT NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE eval_results (
    run_id            BIGINT NOT NULL REFERENCES eval_runs(id) ON DELETE CASCADE,
    scenario_id       BIGINT NOT NULL REFERENCES eval_scenarios(id),
    repeat_no         INT NOT NULL DEFAULT 1,
    top1_correct      BOOLEAN,
    top3_correct      BOOLEAN,
    category_correct  BOOLEAN,
    raw_grounding_rate DOUBLE PRECISION,
    unsupported_count INT,
    tool_calls        INT,
    tokens_total      BIGINT,
    latency_ms        BIGINT,
    PRIMARY KEY (run_id, scenario_id, repeat_no)
);
