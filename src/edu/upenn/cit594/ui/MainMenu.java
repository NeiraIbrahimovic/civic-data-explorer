package edu.upenn.cit594.ui;

import java.util.Scanner;

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
    public static int mainMenu(Scanner scanner) {
        while (true) {
            printMenu(); //Display the menu options
            System.out.print(">  ");
            System.out.flush();
            //Check if there's a next line

            String input = scanner.nextLine().trim();

            //Accept only single-digit inputs 0 through 7
            if(input.matches("[0-7]")) {
                return Integer.parseInt(input);
            }else{
                System.out.println("Invalid input: Please enter a number between 0 and 7");
            }
        }
    }

    /**
     * Prints the list of available actions to the console.
     */
    public static void printMenu() {
        System.out.println("0. Exit the program.");
        System.out.println("1. Show the available actions (subsection 3.1).");
        System.out.println("2. Show the total population for all ZIP Codes (subsection 3.2).");
        System.out.println("3. Show the total vaccinations per capita for each ZIP Code for the specified date (subsection 3.3).");
        System.out.println("4. Show the average market value for properties in a specified ZIP Code (subsection 3.4).");
        System.out.println("5. Show the average total livable area for properties in a specified ZIP Code (subsection 3.5).");
        System.out.println("6. Show the total market value of properties, per capita, for a specified ZIP Code (subsection 3.6).");
        System.out.println("7. Show the results of your custom feature (subsection 3.7).");
    }

}
