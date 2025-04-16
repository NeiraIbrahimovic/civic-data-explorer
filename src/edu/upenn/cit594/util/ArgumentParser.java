package edu.upenn.cit594.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for parsing command-line arguments provided to the Main method.
 * Converts arguments in the format --key=value into a key-value map.
 */
public class ArgumentParser {
	
	/**
	 * Parses the given command-line arguments and returns a map of key-value pairs.
     * Validates that each argument is in the expected --key=value format,
     * and that all keys are from the allowed set.
     * 
	 * @param args Array of command-line arguments passed to the program
	 * @param validArgs Set of valid argument keys (e.g., covid, population, etc.)
	 * @return Map of parsed arguments (key-value), or null if validation fails
	 */
	
    public static Map<String, String> parse(String[] args, Set<String> validArgs) {
        //Initialize a new HashMap to hold the parsed arguments
    	Map<String, String> argMap = new HashMap<>();
        
        //Regex to extract --name=value format
        Pattern pattern = Pattern.compile("^--(?<name>.+?)=(?<value>.+)$");

        //If the provided argument does not match the --name=value format, return an error
        for (String arg : args) {
                Matcher matcher = pattern.matcher(arg);
                if (!matcher.matches()) {
                    System.err.println("Error: Invalid argument format: " + arg);

                }

                //Assign the name and value from the argument pattern to variables
                String name = matcher.group("name");
                String value = matcher.group("value");

            // Validate that the argument name is in the validArgs set
                if (!validArgs.contains(name) || argMap.containsKey(name)) {
                    throw new IllegalArgumentException("Invalid argument name: " + name);
                }

                //Throw an error for duplicate arguments
                if (argMap.containsKey(name)) {
                    throw new IllegalArgumentException("Duplicate argument: " + name);
                }

                //If not a duplicate argument, add the name and value of the argument to the HashMap
                argMap.put(name, value);
        }
        return argMap;
    }
}