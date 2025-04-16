package edu.upenn.cit594.ui;

import edu.upenn.cit594.processor.ProcessorPopulationStats;

/**
 * UI class for Feature 2:
 * Handles the user interaction for calculating
 * the total population across all ZIP codes.
 * Delegates computation to the ProcessorPopulationStats class.
 */
public class TotalPopulationAllZip {

    //Processor that performs population-related calculations
    protected ProcessorPopulationStats processor;

    /**
     * Constructor that initializes the population processor.
     * @param processor an instance of ProcessorPopulationStats
     */
    public TotalPopulationAllZip(ProcessorPopulationStats processor) {
        this.processor = processor;
    }

    /**
     * Executes the logic to compute and display total population for all ZIP codes.
     */
    public void execute() {
        int totalPopulation = processor.getTotalPopulationAllZipCodes();
        System.out.println("BEGIN OUTPUT");
        System.out.println(totalPopulation);
        System.out.println("END OUTPUT");
    }
}
