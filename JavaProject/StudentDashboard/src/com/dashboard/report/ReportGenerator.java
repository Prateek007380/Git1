package com.dashboard.report;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import com.dashboard.model.*;

public class ReportGenerator {

    public static void generateCSV(Map<Student, Metrics> defaulters, String outputPath) throws IOException {
        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write("Student ID,Name,Attendance %,Marks,Status\n");

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
        System.out.println("Report saved successfully to: " + outputPath);
    }
}