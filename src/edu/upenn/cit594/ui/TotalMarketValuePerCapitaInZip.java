package edu.upenn.cit594.ui;

import edu.upenn.cit594.processor.ZipCodeDataAggregator;
import edu.upenn.cit594.util.ScannerManager;

import java.util.Scanner;

/**
 * UI class for feature 6:
 * This class handles the user interaction for calculating
 * the total market value per capita for a given ZIP code.
 * It is part of the UI layer and delegates the computation
 * to the ZipCodeDataAggregator class.
 */
public class TotalMarketValuePerCapitaInZip {

    //Aggregator that provides the calculation logic for combined data
    protected ZipCodeDataAggregator aggregator;

    /**
     * Constructor that initializes the ZipCodeDataAggregator dependency.
     * @param aggregator Aggregator with access to property, population, and COVID data
     */
    public TotalMarketValuePerCapitaInZip(ZipCodeDataAggregator aggregator) {
        this.aggregator = aggregator;
    }

    /**
     * Executes the UI flow:
     * 1. Prompts user to enter a 5-digit ZIP code
     * 2. Validates input format
     * 3. Computes and displays total market value per capita for that ZIP
     */
    public void execute() {
        Scanner scanner = ScannerManager.getScanner();

        //Prompt user to enter a valid 5-digit ZIP code
        System.out.println("Enter a 5-digit ZIP code:");
        System.out.print("> ");
        System.out.flush();
        String zipCode = scanner.nextLine().trim();

        //Repeat prompt until valid ZIP format is provided
        while (!zipCode.matches("\\d{5}")) {
            System.out.println("Invalid ZIP code. Please enter a 5-digit ZIP code:");
            System.out.print("> ");
            System.out.flush();
            zipCode = scanner.nextLine().trim();
        }

        //Perform computation using the aggregator
        int marketValuePerCapita = aggregator.getTotalMarketValuePerCapita(zipCode);

        //Display result in required format
        System.out.println("BEGIN OUTPUT");
        System.out.println(marketValuePerCapita);
        System.out.println("END OUTPUT");
    }
}