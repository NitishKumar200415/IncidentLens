package com.incidentlens.report;

import com.incidentlens.domain.CauseCategory;
import com.incidentlens.domain.Confidence;
import java.util.List;

/** One ranked root-cause hypothesis. Every claim must cite stored evidence IDs. */
public record Hypothesis(
        String service,
        CauseCategory causeCategory,
        Confidence confidence,
        String summary,
        List<String> evidenceIds) {
}
