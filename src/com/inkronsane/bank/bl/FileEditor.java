package com.inkronsane.bank.bl;


import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileEditor {

    public static List<String> fileRead(String path) throws IOException {
        return Files.readAllLines(Path.of(path));
    }

    public static void fileWrite(String path, List<String> list) throws IOException {
        File file = new File(path);
        File parentDirectory = file.getParentFile();
        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }

        Files.write(Path.of(path), list);
    }

    public static String findInFile(String path, String trigger) throws IOException {
        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(trigger)) {
                    return line.substring(trigger.length());
                }
            }
        }
        return null;
    }




}
