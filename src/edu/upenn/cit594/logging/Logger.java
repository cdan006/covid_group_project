package edu.upenn.cit594.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private static String logFile;

    private Logger() {
    }

    private static Logger instance = new Logger();

    public static Logger getInstance() {
        return instance;
    }

    public static String getLogFile() {
        return logFile;
    }

    //Setter function to allow the user to set/update the logfile
    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    //Create or append the log file with the flu tweets
    public void log(String msg) throws IOException {
        try {
            FileWriter fileWriter = new FileWriter(logFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(msg);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
