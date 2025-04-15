package edu.upenn.cit594.ui;

import edu.upenn.cit594.processor.ProcessorPropertyStats;
import edu.upenn.cit594.util.ScannerManager;

import java.util.Scanner;

/**
 * UI class for feature 4:
 * This class handles the user interaction for calculating
 * the average market value in a specified ZIP Code.
 * It is part of the UI layer and delegates the computation
 * to the ProcessorPropertyStats class.
 */
public class AvgMarketValueInZip {

    //Reference to the processor that provides the computation logic
    protected ProcessorPropertyStats propStats;

    /**
     * Constructor initializes the UI action with the required processor.
     * @param propStats the processor responsible for property statistics
     */
    public AvgMarketValueInZip(ProcessorPropertyStats propStats) {
        this.propStats = propStats;
    }

    /**
     * Executes the user interaction for the menu 4 option:
     * 1. Prompts for a 5-digit ZIP code
     * 2. Validates the ZIP format
     * 3. Retrieves and prints the average market value
     */
    public void execute() {
        Scanner scanner = ScannerManager.getScanner();

        //Prompt the user for a 5-digit ZIP code
        System.out.println("Enter a 5-digit ZIP code:");
        System.out.print("> ");
        System.out.flush();
        String zipCode = scanner.nextLine().trim();

        //Repeat prompt until a valid 5-digit ZIP code is entered
        while (!zipCode.matches("\\d{5}")) {
            System.out.println("Invalid ZIP code. Please enter a 5-digit ZIP code:");
            System.out.print("> ");
            System.out.flush();
            zipCode = scanner.nextLine().trim();
        }

        //Retrieve and display the result
        int avgValue = propStats.getAverageMarketValueInZip(zipCode);

        //Print the output
        System.out.println("BEGIN OUTPUT");
        System.out.println(avgValue);
        System.out.println("END OUTPUT");
    }
}