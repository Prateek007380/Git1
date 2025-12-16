package com.dashboard.logic;
import com.dashboard.model.Metrics;

public interface RiskStrategy {
    boolean isAtRisk(Metrics m);
}