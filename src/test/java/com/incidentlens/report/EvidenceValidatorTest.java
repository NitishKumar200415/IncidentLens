package com.incidentlens.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.incidentlens.domain.CauseCategory;
import com.incidentlens.domain.Confidence;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EvidenceValidatorTest {

    private final EvidenceValidator validator = new EvidenceValidator();

    private Hypothesis h(List<String> ids) {
        return new Hypothesis("payment", CauseCategory.ERROR_SPIKE, Confidence.HIGH, "error rate rose", ids);
    }

    @Test
    void acceptsHypothesisWithKnownEvidence() {
        Report r = new Report(List.of(h(List.of("ev_1"))), false);
        ValidationResult res = validator.validate(r, Set.of("ev_1", "ev_2"));
        assertTrue(res.fullyGrounded());
        assertEquals(1, res.supported().size());
    }

    @Test
    void rejectsUnknownEvidenceId() {
        Report r = new Report(List.of(h(List.of("ev_99"))), false);
        ValidationResult res = validator.validate(r, Set.of("ev_1"));
        assertFalse(res.fullyGrounded());
        assertEquals(1, res.unsupported().size());
    }

    @Test
    void rejectsHypothesisWithNoEvidence() {
        Report r = new Report(List.of(h(List.of())), false);
        ValidationResult res = validator.validate(r, Set.of("ev_1"));
        assertEquals(1, res.unsupported().size());
    }
}
