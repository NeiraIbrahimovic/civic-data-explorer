package edu.upenn.cit594.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Singleton Logger class used to log events (such as user inputs, file accesses).
 * It prepends all logs with timestamps using System.currentTimeMillis().
 * 
 * This logger writes to System.err by default, but can be reconfigured to append to a file.
 */
public class Logger {
    //Singleton instance of the Logger
    private static Logger instance;

    //Writer object used to write logs
    private PrintWriter writer;

    //Track whether we’re using System.err
    private boolean usingSystemErr = true; // Track whether we’re using System.err

    //Private constructor to enforce singleton pattern
    private Logger() {
        // Default to standard error output
        writer = new PrintWriter(System.err, true); //auto-flush
        usingSystemErr = true;
    }

    /**
     * Returns the single Logger instance, creating it if it doesn't exist.
     */
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger(); //Lazy initialization
        }
        return instance;
    }

    /**
     * Sets the output file for the logger.
     * Opens the file in append mode and redirects future log output to it.
     * 
     * If an error occurs while opening the file, logs will fall back to System.err.
     */
    public void setOutput(String filename) {
        //If we already have a writer (and it isn't System.err), close it
        if (writer != null && !usingSystemErr) {
            writer.close();
        }

        if (filename == null || filename.isEmpty()) {
            writer = new PrintWriter(System.err, true);
            usingSystemErr = true;
            return;
        }


        try {
            //Open file in append mode
            FileWriter fileWriter = new FileWriter(filename, true); //append mode
            writer = new PrintWriter(fileWriter, true); //true = autoFlush
            usingSystemErr = false;
        } catch (IOException e) {
            //Fall back to stderr if file can't be opened
            writer = new PrintWriter(System.err, true);
            usingSystemErr = true;
        }
    }

    /**
     * Logs a single event with a millisecond timestamp.
     * @param message The message to log (e.g., user input, file opened)
     */
    public void log(String message) {
        if (writer == null) {
            writer = new PrintWriter(System.err, true); // fallback if unset
            usingSystemErr = true;
        }

        long timestamp = System.currentTimeMillis(); //get current system time
        writer.println(timestamp + " " + message);   //write to output
    }
}
