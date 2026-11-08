package com.incidentlens.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Checks that every hypothesis cites evidence that exists in this investigation.
 *
 * Guarantees citations are real, NOT that the diagnosis is correct.
 * Correctness is measured by the eval harness.
 *
 * TODO (Day 13-15): also check numeric claims against structured_values within a tolerance,
 * and that the named service appears in the cited evidence.
 */
public class EvidenceValidator {

    public ValidationResult validate(Report report, Set<String> knownEvidenceIds) {
        List<Hypothesis> supported = new ArrayList<>();
        List<Hypothesis> unsupported = new ArrayList<>();
        for (Hypothesis h : report.hypotheses()) {
            List<String> ids = h.evidenceIds();
            boolean ok = ids != null && !ids.isEmpty() && knownEvidenceIds.containsAll(ids);
            if (ok) {
                supported.add(h);
            } else {
                unsupported.add(h);
            }
        }
        return new ValidationResult(supported, unsupported);
    }
}
