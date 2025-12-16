package com.dashboard.main;

import java.util.*;
import com.dashboard.model.*;
import com.dashboard.service.StudentDataService;
import com.dashboard.logic.*;
import com.dashboard.report.ReportGenerator;

public class AttendanceDashboard {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentDataService service = new StudentDataService();
        RiskStrategy strategy = new StrictRiskStrategy();

        System.out.println("=== DIGITAL ATTENDANCE DASHBOARD ===");

        while (true) {
            System.out.println("\n1. Load Data Files");
            System.out.println("2. Show Analysis (Console)");
            System.out.println("3. Download Defaulter Report");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();

            try {
                switch (choice) {
                    case 1:
                        service.loadData("attendance.csv", "marks.csv");
                        break;

                    case 2:
                        System.out.println("\n--- ANALYSIS RESULTS ---");
                        Map<Student, Metrics> allData = service.getDatabase();
                        
                        if (allData.isEmpty()) {
                            System.out.println("No data loaded. Please run Option 1 first.");
                        } else {
                             System.out.printf("%-15s | %-10s | %-10s | %-10s%n", "NAME", "ATT %", "MARKS", "STATUS");
                             System.out.println("----------------------------------------------------------");
                             for (Map.Entry<Student, Metrics> entry : allData.entrySet()) {
                                Metrics m = entry.getValue();
                                boolean isRisk = strategy.isAtRisk(m);
                                String status = isRisk ? "[RISK]" : "[SAFE]";
                                
                                System.out.printf("%-15s | %5.1f%%     | %5.1f      | %-10s%n", 
                                    entry.getKey().getName(), 
                                    m.getAttendancePercentage(), 
                                    m.getMarks(), 
                                    status);
                            }
                        }
                        break;

                    case 3:
                        System.out.println("Generating Reports...");
                        Map<Student, Metrics> allStudents = service.getDatabase();
                        Map<Student, Metrics> riskyStudents = new HashMap<>();

                        // 1. Generate the Defaulters CSV (Existing Logic)
                        for (Map.Entry<Student, Metrics> entry : allStudents.entrySet()) {
                            if (strategy.isAtRisk(entry.getValue())) {
                                riskyStudents.put(entry.getKey(), entry.getValue());
                            }
                        }
                        
                        if(riskyStudents.isEmpty()) {
                            System.out.println("No at-risk students found for CSV.");
                        } else {
                            ReportGenerator.generateCSV(riskyStudents, "defaulters_report.csv");
                        }

                        // 2. Generate the NEW Correlation Text File (New Logic)
                        ReportGenerator.generateCorrelationReport(allStudents, "correlation.txt");
                        
                        break;

                    case 4:
                        System.out.println("Exiting...");
                        scanner.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}