package edu.upenn.cit594.ui;

import java.util.Scanner;

public class MainMenu {
    public static final Scanner scanner = new Scanner(System.in);
    public static int mainMenu(Scanner scanner) {
        while (true) {
            printMenu();
            System.out.print(">  ");
            System.out.flush();

            String input = scanner.nextLine().trim();

            if(input.matches("[0-7]")) {
                return Integer.parseInt(input);
            }else{
                System.out.println("Invalid input: Please enter a number between 0 and 7");
            }
        }
    }

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
