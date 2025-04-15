package edu.upenn.cit594.datamanagement;

import java.io.IOException;
import java.text.ParseException;

/**
 * Utility class responsible for loading the correct file readers
 * based on the provided file paths and their extensions.
 *
 */
public class FileLoader {
    /**
     * Creates and returns an appropriate CovidFileReader
     * based on the file extension (.csv or .json).
     *
     * @param path the path to the COVID data file
     * @return an instance of either CSVDataFileReader or JSONDataFileReader
     * @throws IOException if file access fails
     * @throws ParseException if parsing the file contents fails
     */
    public static CovidFileReader loadCovidReader(String path) throws IOException, ParseException {
        if (path.endsWith(".json")) return new JSONDataFileReader(path);
        return new CSVDataFileReader(path);
    }

    /**
     * Creates and returns a PropertyFileReader given the file path.
     *
     * @param path the path to the property data file (.csv)
     * @return a PropertyFileReader instance
     * @throws IOException if file cannot be accessed
     * @throws ParseException if parsing fails
     */
    public static PropertyFileReader loadPropertyReader(String path) throws IOException, ParseException {
        return new PropertyFileReader(path);
    }

    /**
     * Creates and returns a PopulationFileReader given the file path.
     *
     * @param path the path to the population data file (.csv)
     * @return a PopulationFileReader instance
     * @throws IOException if file cannot be accessed
     * @throws ParseException if parsing fails
     */
    public static PopulationFileReader loadPopulationReader(String path) throws IOException, ParseException {
        return new PopulationFileReader(path);
    }
}