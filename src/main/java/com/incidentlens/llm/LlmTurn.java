package com.incidentlens.llm;

/** One model turn: either final text, or a tool call request (toolName != null). */
public record LlmTurn(String text, String toolName, String toolArgsJson, long tokensIn, long tokensOut) {
    public boolean isToolCall() {
        return toolName != null;
    }
}
