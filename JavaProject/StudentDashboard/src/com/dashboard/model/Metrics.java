package com.dashboard.model;

public class Metrics {
    private int attendedDays;
    private int totalDays;
    private double marks;

    public Metrics(int attendedDays, int totalDays, double marks) {
        this.attendedDays = attendedDays;
        this.totalDays = totalDays;
        this.marks = marks;
    }

    public double getAttendancePercentage() {
        if (totalDays == 0) return 0.0;
        return ((double) attendedDays / totalDays) * 100.0;
    }

    public double getMarks() { return marks; }
    
    // Setter needed for Phase 3 merging
    public void setMarks(double marks) {
        this.marks = marks;
    }
}