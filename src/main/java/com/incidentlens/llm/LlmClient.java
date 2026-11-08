package com.incidentlens.llm;

import com.incidentlens.tools.Tool;
import java.util.List;

/**
 * Provider abstraction. Two implementations planned:
 *  - a real provider client (decide provider after checking current tool-calling docs and pricing)
 *  - a scripted fake for deterministic tests and CI
 */
public interface LlmClient {
    LlmTurn next(List<LlmMessage> history, List<Tool> tools);
}
