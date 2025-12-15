package com.dashboard.logic;
import com.dashboard.model.Metrics;

public class StrictRiskStrategy implements RiskStrategy {
    @Override
    public boolean isAtRisk(Metrics m) {
        // Logic: Risk if Attendance < 75% OR Marks < 40
        return m.getAttendancePercentage() < 75.0 || m.getMarks() < 40.0;
    }
}