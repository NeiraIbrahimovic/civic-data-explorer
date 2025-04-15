package edu.upenn.cit594.datamanagement;
import edu.upenn.cit594.util.CovidData;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for loading COVID data files.
 * Subclasses (e.g., CSV or JSON readers) must implement the file-specific logic.
 *
 * This class provides a shared structure for storing and retrieving parsed COVID data.
 */
public abstract class CovidFileReader {
	
	//List to hold parsed CovidData records from the input file
    protected List<CovidData> covidData = new ArrayList<>();

    
    /**
     * Abstract method that subclasses must implement to load data
     * from a specific file format (e.g., CSV or JSON).
     *
     * @param fileName the full path to the file to be read
     * @throws IOException if there's an error reading the file
     * @throws ParseException if the file contains invalid date or number formats
     */
    protected abstract void readFile(String fileName) throws IOException, ParseException;

    /**
     * Returns the parsed list of CovidData records.
     * This is used by processors or aggregators that rely on COVID metrics.
     *
     * @return List of CovidData parsed from the file
     */
    public List<CovidData> getCovidData() {
        return covidData;
    }
    
    /**
     * Helper method: Returns a map of ZIP code to hospitalization count on a given date.
     * @param date Date in YYYY-MM-DD format
     * @return map of ZIP -> total hospitalizations
     */
    public abstract Map<String, Integer> getTotalHospitalizationsByZipOnDate(String date);
}
