package edu.upenn.cit594.ui;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.ZipCodeDataAggregator;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * UI class for Feature 7: Health Equity Score.
 * This class handles the user interaction for computing ZIP-based 
 * scores using hospitalizations and market value per capita.
 * It is part of the UI layer and delegates the computation
 * to the ZipCodeDataAggregator class.
 */
public class HealthEquityScoreByZip {

    //Aggregator that combines COVID, property, and population data
    protected ZipCodeDataAggregator aggregator;

    /**
     * Constructor for the UI class.
     * @param aggregator the aggregator used to compute health equity scores
     */
    public HealthEquityScoreByZip(ZipCodeDataAggregator aggregator) {
        this.aggregator = aggregator;
    }

    /**
     * Executes the UI interaction:
     * 1. Prompts user to enter a date
     * 2. Logs the user input
     * 3. Calls the aggregator to compute scores
     * 4. Prints results in a sorted format
     */
    public void execute(Scanner scanner) {

        Logger logger = Logger.getInstance();

        //Prompt the user to enter a date for filtering hospitalization data
        System.out.println("Enter date for hospitalization data (YYYY-MM-DD):");
        System.out.print("> ");
        System.out.flush();
        String date = scanner.nextLine().trim();
        
        //Log the user-entered date
        logger.log(date);

        //Compute health equity scores for all zip codes using the provided date
        Map<String, Double> scores = aggregator.getHealthEquityScores(date);

        //Sort and display the results
        System.out.println("\nBEGIN OUTPUT");
        TreeMap<String, Double> sorted = new TreeMap<>(scores); //Use TreeMap because automatically sorts by zip
        for (Map.Entry<String, Double> entry : sorted.entrySet()) {
            System.out.printf("%s %.4f%n", entry.getKey(), entry.getValue());
        }
        System.out.println("END OUTPUT");
    }
}