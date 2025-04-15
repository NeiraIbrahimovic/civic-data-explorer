package edu.upenn.cit594.ui;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.ProcessorPropertyStats;
import edu.upenn.cit594.util.ScannerManager;

import java.util.Scanner;

/**
 * UI class for Feature 5:
 * This class handles user interaction for computing the average 
 * total livable area for properties in a given ZIP code.
 * It is part of the UI layer and delegates the computation
 * to the ProcessorPropertyStats class.
 */
public class AvgTotalLivableAreaInZip {

    //Reference to the processor that handles livable area statistics 
    protected ProcessorPropertyStats processor;

    /**
     * Constructor that injects the processor used to compute livable area statistics.
     * @param processor the ProcessorPropertyStats instance
     */
    public AvgTotalLivableAreaInZip(ProcessorPropertyStats processor) {
        this.processor = processor;
    }

    /**
     * Executes the user interaction flow:
     * 1. Prompts for a valid ZIP code
     * 2. Logs the input
     * 3. Computes and prints the average livable area
     */
    public void execute() {
    	//Get shared Scanner and Logger instances
    	Scanner scanner = ScannerManager.getScanner();
        Logger logger = Logger.getInstance();

        String zip = "";
        //Prompt until a valid 5-digit ZIP code is entered
        while (!zip.matches("\\d{5}")) {
            System.out.println("Enter a 5-digit ZIP code:");
            System.out.print("> ");
            System.out.flush();
            zip = scanner.nextLine().trim();
            
            //Log every input attempt
            logger.log(zip);
        }

        //Perform the calculation using the processor
        int avgArea = processor.getAverageLivableAreaInZip(zip);

        //Print result
        System.out.println("BEGIN OUTPUT");
        System.out.println(avgArea);
        System.out.println("END OUTPUT");
    }
}