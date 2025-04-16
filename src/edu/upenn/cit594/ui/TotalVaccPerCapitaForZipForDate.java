package edu.upenn.cit594.ui;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.ProcessorVaccinationStats;

import java.util.*;

/**
 * UI class for feature 3:
 * This class handles the user interaction for computing
 * total vaccinations per capita for a given date and type (partial/full).
 * It is part of the UI layer and delegates the computation
 * to the ProcessorVaccinationStats class.
 */

public class TotalVaccPerCapitaForZipForDate {

    // Reference to the processor that holds and computes vaccination data
    protected ProcessorVaccinationStats processor;

    /**
     * Constructor receives the processor responsible for vaccination statistics.
     * @param processor ProcessorVaccinationStats instance
     */
    public TotalVaccPerCapitaForZipForDate(ProcessorVaccinationStats processor) {
        this.processor = processor;
    }

    /**
     * Executes the interactive flow for Feature 3:
     * 1. Prompts the user for vaccination type and date
     * 2. Logs both inputs
     * 3. Delegates to the processor to calculate per capita results
     * 4. Prints results in sorted order
     */
    public void execute(Scanner scanner) {
        // Create a new Scanner instance directly
        //Scanner scanner = new Scanner(System.in);

        // Get the shared logger instance
        Logger logger = Logger.getInstance();

        // Prompt the user for vaccination type (must be "partial" or "full")
        String type = "";
        while (!type.equals("partial") && !type.equals("full")) {
            System.out.println("Enter vaccination type (partial/full):");
            System.out.print("> ");
            //System.out.flush();  // Ensure prompt appears before input
            try {
                if (!scanner.hasNextLine()) {
                    System.out.println("Input ended no next line.");
                    return;
                }
                type = scanner.nextLine().trim().toLowerCase(); // Normalize input
                logger.log(type); // Log user input
                if (!type.equals("partial") && !type.equals("full")) {
                    System.out.println("Invalid input: Please enter 'partial' or 'full'.");
                }
            } catch (Exception e) {
                System.out.println("Error reading input: " + e.getMessage());
                return;
            }
        }

        // Prompt the user for the reporting date in YYYY-MM-DD format
        String date = "";
        while (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("Enter date (YYYY-MM-DD):");
            System.out.print("> ");
            System.out.flush();  // Prompt formatting
            try {
                if (!scanner.hasNextLine()) {
                    System.out.println("Input ended unexpectedly.");
                    return;
                }
                date = scanner.nextLine().trim(); // Capture user input
                logger.log(date); // Log input
                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    System.out.println("Invalid input: Please enter a date in the format YYYY-MM-DD.");
                }
            } catch (Exception e) {
                System.out.println("Error reading input: " + e.getMessage());
                return;
            }
        }

        // Delegate to the processor to get per-capita vaccination rates
        Map<String, Double> results = processor.getVaccinationsPerCapitaByZip(date, type);

        // Print results in the required format
        System.out.println("\nBEGIN OUTPUT");
        // Sort ZIPs numerically using TreeMap
        TreeMap<String, Double> sorted = new TreeMap<>(results);
        for (Map.Entry<String, Double> entry : sorted.entrySet()) {
            // Print ZIP and per capita value rounded to 4 decimal places
            System.out.printf("%s %.4f%n", entry.getKey(), entry.getValue());
        }
        System.out.println("END OUTPUT");

        // Close the scanner when done
        //scanner.close();
    }
}
