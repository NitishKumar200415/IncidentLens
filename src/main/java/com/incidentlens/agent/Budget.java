package com.incidentlens.agent;

import java.time.Duration;

/** Per-investigation limits. Starting values; tune using eval data. */
public record Budget(int maxToolCalls, long maxTokens, Duration maxWallClock) {
    public static Budget defaults() {
        return new Budget(12, 60_000, Duration.ofSeconds(90));
    }
}
