package edu.upenn.cit594.util;

/**
 * Custom exception to signal formatting errors in CSV files.
 */
public class CSVFormatException extends Exception {

    public CSVFormatException(String message) {
        super(message);
    }

}