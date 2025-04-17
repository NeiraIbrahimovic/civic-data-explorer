package edu.upenn.cit594.ui;

import edu.upenn.cit594.datamanagement.CovidFileReader;
import edu.upenn.cit594.datamanagement.PopulationFileReader;
import edu.upenn.cit594.datamanagement.PropertyFileReader;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * UI component that displays the main menu and captures user selection.
 * Enforces input validation and returns a numeric selection between 0 and 7.
 */
public class MainMenu {

    /**
     * Displays the menu repeatedly until a valid input (0–7) is entered.
     *
     * @param scanner shared Scanner instance for reading user input
     * @return the selected menu option as an integer
     */
    public static int mainMenu(Scanner scanner, CovidFileReader covidFileReader, PropertyFileReader propertyFileReader, PopulationFileReader populationFileReader) {
        while (true) {
            // Set to store the valid input options
            Set<String> validOptions = new HashSet<>();

            validOptions.add("0");
            validOptions.add("1");

            if (populationFileReader != null) {
                validOptions.add("2");
            }
            if (covidFileReader != null) {
                validOptions.add("3");
            }
            if (propertyFileReader != null) {
                validOptions.add("4");
                validOptions.add("5");
            }
            if (populationFileReader != null && propertyFileReader != null) {
                validOptions.add("6");
            }
            if (covidFileReader != null && propertyFileReader != null && populationFileReader != null) {
                validOptions.add("7");
            }

//            if (!scanner.hasNextLine()) {
//                System.out.println("Input ended unexpectedly.");
//                return 0;
//            }
            System.out.println("0. Exit the program.");
            System.out.println("1. Show the available actions (subsection 3.1).");
            System.out.println("2. Show the total population for all ZIP Codes (subsection 3.2).");
            System.out.println("3. Show the total vaccinations per capita for each ZIP Code for the specified date (subsection 3.3).");
            System.out.println("4. Show the average market value for properties in a specified ZIP Code (subsection 3.4).");
            System.out.println("5. Show the average total livable area for properties in a specified ZIP Code (subsection 3.5).");
            System.out.println("6. Show the total market value of properties, per capita, for a specified ZIP Code (subsection 3.6).");
            System.out.println("7. Show the results of your custom feature (subsection 3.7).");
            System.out.print(">  ");
            System.out.flush();
            String input = scanner.nextLine();

            if(!input.matches("[0-7]")){
                System.out.println("Invalid input: Please enter a number between 0 and 7\n");
                continue;
            }
            // Accept only inputs that are present in validOptions
            if (validOptions.contains(input)) {
                return Integer.parseInt(input);
            } else {
                System.out.println("File missing for this action.\n");
            }
        }

    }
    /**
     * Prints the list of available actions to the console.
     */
    public static void printMenu(CovidFileReader covidFileReader, PropertyFileReader propertyFileReader, PopulationFileReader populationFileReader) {

        System.out.println("\nBEGIN OUTPUT");
        System.out.println("0");
        System.out.println("1");
        if (populationFileReader != null) {
            System.out.println("2");

        }
        if (covidFileReader != null) {
            System.out.println("3");

        }
        if (propertyFileReader != null) {
            System.out.println("4");
            System.out.println("5");

        }
        if (populationFileReader != null && propertyFileReader != null) {
            System.out.println("6");

        }
        if (covidFileReader != null && propertyFileReader != null && populationFileReader != null) {
            System.out.println("7");

        }
        System.out.println("END OUTPUT");
    }


}
