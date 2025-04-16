package edu.upenn.cit594.util;

import java.util.Map;

/**
 * Utility class responsible for validating the format of input file paths.
 * Ensures files have the expected extensions (e.g., .csv or .json).
 */

public class FileValidator {
	
    /**
     * Checks that the provided file paths have valid file extensions based on file type:
     * - COVID data must be either .json or .csv
     * - Properties and population files must be .csv
     *
     * @param argMap A map of argument names to file paths (as parsed from the command line)
     * @return true if all file paths have valid formats; false otherwise
     */
	
    public static boolean validate(Map<String, String> argMap) {
    	//Get paths from argument map
        String covidFile = argMap.get("covid");
        String propFile = argMap.get("properties");
        String popFile = argMap.get("population");


        //Return true only if all provided file extensions are valid
        //COVID: JSON or CSV
        if (covidFile == null ||
                !(covidFile.endsWith(".json") || covidFile.endsWith(".csv"))) {
            return false;
        }

        //Properties: must be CSV
        if (propFile != null && !propFile.endsWith(".csv")) {
            return false;
        }

        //Population: must be CSV
        if (popFile != null && !popFile.endsWith(".csv")) {
            return false;
        }

        return true;
    }
}