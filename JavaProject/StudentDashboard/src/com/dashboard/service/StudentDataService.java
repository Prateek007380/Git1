package com.dashboard.service;

import java.io.IOException;
import java.util.*;
import com.dashboard.model.*;
import com.dashboard.io.FileLoader;

public class StudentDataService {

    private Map<Student, Metrics> database = new HashMap<>();

    public void loadData(String attFile, String marksFile) throws IOException {
        System.out.println("Step 1: Loading Attendance...");
        Map<String, String[]> attData = FileLoader.loadRawAttendance(attFile);

        for (String id : attData.keySet()) {
            String[] details = attData.get(id);
            String name = details[0];
            int attended = Integer.parseInt(details[1]);
            int total = Integer.parseInt(details[2]);

            Student s = new Student(id, name);
            Metrics m = new Metrics(attended, total, 0.0); // Default marks 0
            
            database.put(s, m);
        }

        System.out.println("Step 2: Loading and Merging Marks...");
        Map<String, String> marksData = FileLoader.loadRawMarks(marksFile);

        for (String id : marksData.keySet()) {
            double marks = Double.parseDouble(marksData.get(id));
            Student lookupKey = new Student(id, ""); 
            
            if (database.containsKey(lookupKey)) {
                Metrics m = database.get(lookupKey);
                m.setMarks(marks);
            } else {
                System.err.println("Warning: Marks found for unknown Student ID: " + id);
            }
        }
        System.out.println("Data Loading Complete. Total Students: " + database.size());
    }

    public Map<Student, Metrics> getDatabase() {
        return database;
    }
}