package com.incidentlens.tools;

/**
 * A bounded, read-only query tool exposed to the agent.
 * Outputs must be aggregated, size-capped, and tagged with an evidence ID.
 */
public interface Tool {
    String name();

    String description();

    /** JSON schema describing the tool arguments. */
    String jsonSchema();

    ToolResult execute(long bundleId, String argumentsJson);
}
