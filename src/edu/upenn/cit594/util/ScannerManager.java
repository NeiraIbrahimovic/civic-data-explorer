package edu.upenn.cit594.util;
import java.util.Scanner;
public class ScannerManager {
    private static final Scanner scanner = new Scanner(System.in);

    private ScannerManager() {}

    public static Scanner getScanner() {
        return scanner;
    }

    //close it one time only, when user clicks 0;
    public static void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }

}
