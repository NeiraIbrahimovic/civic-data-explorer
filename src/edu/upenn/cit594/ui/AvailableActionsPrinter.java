package edu.upenn.cit594.ui;

import edu.upenn.cit594.datamanagement.*;

import java.util.*;

/**
 * UI class for feature 1:
 * Utility class responsible for printing the list of available actions
 * based on which data files were provided during program execution.
 */
public class AvailableActionsPrinter {

    /**
     * Prints the valid menu options available to the user.
     * The options depend on which datasets (population, COVID, property) are loaded.
     *
     * @param pop   the population file reader (null if not provided)
     * @param covid the COVID file reader (null if not provided)
     * @param prop  the property file reader (null if not provided)
     */
    public static void printMenu(PopulationFileReader pop, CovidFileReader covid, PropertyFileReader prop) {
        List<Integer> actions = new ArrayList<>();

        //Actions 0 and 1 are always available: Exit and Show Menu
        actions.add(0);
        actions.add(1);

        //Add action 2: Show total population, if population data is available
        if (pop != null) actions.add(2);

        //Add action 3: Show vaccination per capita, requires COVID and population data
        if (covid != null && pop != null) actions.add(3);

        //Add action 4: Show average market value for ZIP, requires property data
        if (prop != null) actions.add(4);

        //Add action 5: Show average livable area for ZIP, requires property data
        if (prop != null) actions.add(5);

        //Add action 6: Show total market value per capita, needs property + population data
        if (prop != null && pop != null) actions.add(6);

        //Add action 7: Show custom health equity score, needs all three datasets
        if (covid != null && pop != null && prop != null) actions.add(7);

        //Output actions in required format
        System.out.println("BEGIN OUTPUT");
        for (int a : actions) System.out.println(a);
        System.out.println("END OUTPUT");
    }
}
