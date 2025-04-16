package edu.upenn.cit594.util;

import java.util.Scanner;

/**
 * Singleton utility class for managing a shared Scanner instance across the application.
 * Prevents multiple Scanner objects from being created on System.in,
 * which could cause input issues or exceptions.
 */

public class ScannerManager {

    // Shared instance
    private static Scanner instance;

    // Private constructor to prevent instantiation
    private ScannerManager() {}

    /**
     * Returns the single shared Scanner instance.
     * If it doesn't exist yet, it will be created.
     * @return shared Scanner instance
     */
    public static Scanner getInstance() {
        if (instance == null) {
            instance = new Scanner(System.in);
        }
        return instance;
    }

    /**
     * Closes the shared Scanner instance.
     * This should be called only once, typically when the user exits the program.
     * Subsequent calls to read from System.in after closing the scanner will throw exceptions.
     */
    public static void closeScanner() {
        if (instance != null) {
            instance.close();
            instance = null;
        }
    }
}
