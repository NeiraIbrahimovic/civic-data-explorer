package edu.upenn.cit594.util;

import java.util.Scanner;

/**
 * Singleton utility class for managing a shared Scanner instance across the application.
 * Prevents multiple Scanner objects from being created on System.in,
 * which could cause input issues or exceptions.
 */

public class ScannerManager {

    //Static Scanner instance initialized once and shared throughout the program
    private static final Scanner scanner = new Scanner(System.in);

    //Private constructor to prevent instantiation of this utility class
    private ScannerManager() {}

    /**
     * Returns the shared Scanner instance.
     * @return the single Scanner object for reading from System.in
     */
    public static Scanner getScanner() {
        return scanner;
    }

    /**
     * Closes the shared Scanner instance.
     * This should be called only once, typically when the user exits the program.
     * Subsequent calls to read from System.in after closing the scanner will throw exceptions.
     */
    public static void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
