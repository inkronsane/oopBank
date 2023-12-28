package com.inkronsane.simplebank2.FinalBusinessLogic;


import java.io.*;

public class TextWriter {

    public static void saveData(String info, File file)
      throws IOException {
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println(info);
        }
    }

    public static void balanceDestroyer(File file, int newBalance) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath()))) {
            writer.write(String.valueOf(newBalance));
        }
    }

}
