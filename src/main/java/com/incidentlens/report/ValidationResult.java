package com.incidentlens.report;

import java.util.List;

public record ValidationResult(List<Hypothesis> supported, List<Hypothesis> unsupported) {
    public boolean fullyGrounded() {
        return unsupported.isEmpty();
    }
}
