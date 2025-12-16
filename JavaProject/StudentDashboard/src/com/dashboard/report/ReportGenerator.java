package com.dashboard.report;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*; // Imported java.util.* to use List and ArrayList
import com.dashboard.model.Student;
import com.dashboard.model.Metrics;

public class ReportGenerator {

    // --- METHOD 1: Generates the Defaulters List (CSV) ---
    public static void generateCSV(Map<Student, Metrics> defaulters, String outputPath) throws IOException {
        try (FileWriter writer = new FileWriter(outputPath)) {
            // Write Header
            writer.write("Student ID,Name,Attendance %,Marks,Status\n");

            // Write Data Rows
            for (Map.Entry<Student, Metrics> entry : defaulters.entrySet()) {
                Student s = entry.getKey();
                Metrics m = entry.getValue();

                String line = String.format("%s,%s,%.2f,%.2f,AT_RISK\n",
                        s.getId(), 
                        s.getName(), 
                        m.getAttendancePercentage(), 
                        m.getMarks()
                );
                writer.write(line);
            }
        }
        System.out.println("Defaulter Report saved successfully to: " + outputPath);
    }

    // --- METHOD 2: Generates the Correlation Analysis (Text File) ---
    public static void generateCorrelationReport(Map<Student, Metrics> data, String outputPath) throws IOException {
        // 1. Prepare the lists of numbers for the math formula
        List<Double> attendance = new ArrayList<>();
        List<Double> marks = new ArrayList<>();

        for (Metrics m : data.values()) {
            attendance.add(m.getAttendancePercentage());
            marks.add(m.getMarks());
        }

        // 2. Calculate the Correlation (The Math Part)
        double r = calculateCorrelation(attendance, marks);

        // 3. Interpret the result
        String conclusion;
        if (r > 0.5) {
            conclusion = "Strong Positive: High Attendance leads to High Marks.";
        } else if (r < -0.5) {
            conclusion = "Negative: Attendance and Marks are opposite (Unusual).";
        } else {
            conclusion = "Weak/None: Attendance doesn't seem to affect Marks much.";
        }

        // 4. Write to File
        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write("=== STATISTICAL ANALYSIS ===\n");
            writer.write("Total Students Analyzed: " + data.size() + "\n");
            writer.write("----------------------------\n");
            writer.write(String.format("Correlation Coefficient (r): %.4f\n", r));
            writer.write("Conclusion: " + conclusion + "\n");
        }
        System.out.println("Correlation Analysis saved to: " + outputPath);
    }

    // --- HELPER METHOD: The Pearson Correlation Formula ---
    private static double calculateCorrelation(List<Double> x, List<Double> y) {
        int n = x.size();
        if (n == 0) return 0.0;

        double sumX = 0, sumY = 0, sumXY = 0;
        double sumXSquare = 0, sumYSquare = 0;

        for (int i = 0; i < n; i++) {
            sumX += x.get(i);
            sumY += y.get(i);
            sumXY += x.get(i) * y.get(i);
            sumXSquare += x.get(i) * x.get(i);
            sumYSquare += y.get(i) * y.get(i);
        }

        double numerator = (n * sumXY) - (sumX * sumY);
        double denominator = Math.sqrt((n * sumXSquare - sumX * sumX) * (n * sumYSquare - sumY * sumY));

        if (denominator == 0) return 0; // Avoid division by zero
        return numerator / denominator;
    }
}