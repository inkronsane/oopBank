package com.inkronsane.simplebank2.FinalBusinessLogic;


import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TextRead {

    public static String readAllText(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        // Видаляємо останній рядок роздільника (System.lineSeparator())
        if (content.length() > 0) {
            content.setLength(content.length() - System.lineSeparator().length());
        }
        return content.toString();
    }


    public static int readBalance(File file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.getPath()))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line.trim());
            }
            return 0;
        }
    }

    public static List<String> readAllLines(File file) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }

}
