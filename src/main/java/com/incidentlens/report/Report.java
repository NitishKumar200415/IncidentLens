package com.incidentlens.report;

import java.util.List;

/** Structured investigation output. Hypotheses are ordered best-first. */
public record Report(List<Hypothesis> hypotheses, boolean incomplete) {
}
