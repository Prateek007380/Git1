package com.dashboard.io;

import java.io.*;
import java.util.*;

public class FileLoader {

    public static Map<String, String[]> loadRawAttendance(String filePath) throws IOException {
        Map<String, String[]> dataMap = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) continue; 

                String id = parts[0].trim();
                String name = parts[1].trim();
                String attended = parts[2].trim();
                String total = parts[3].trim();

                dataMap.put(id, new String[]{name, attended, total});
            }
        }
        return dataMap;
    }

    public static Map<String, String> loadRawMarks(String filePath) throws IOException {
        Map<String, String> marksMap = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue;

                String id = parts[0].trim();
                String marks = parts[1].trim();

                marksMap.put(id, marks);
            }
        }
        return marksMap;
    }
}