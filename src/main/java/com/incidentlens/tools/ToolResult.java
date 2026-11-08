package com.incidentlens.tools;

/** Result returned to the model plus the evidence stored for later citation. */
public record ToolResult(String evidenceId, String summary, String structuredValuesJson) {
}
