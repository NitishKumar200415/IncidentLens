package com.incidentlens.domain;

/** Allowed root-cause categories. UNKNOWN is intentional: honest abstention beats a wrong guess. */
public enum CauseCategory {
    SLOW_DEPENDENCY,
    ERROR_SPIKE,
    RESOURCE_SATURATION,
    CONFIG_OR_DEPLOY_CHANGE,
    TRAFFIC_SURGE,
    UNKNOWN
}
