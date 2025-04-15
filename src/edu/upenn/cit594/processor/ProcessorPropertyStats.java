package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.PropertyFileReader;
import edu.upenn.cit594.util.Population;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles property-related statistics using data from PropertyFileReader.
 */
public class ProcessorPropertyStats {

    protected PropertyFileReader reader;
    protected Map<String, Integer> avgMarketValueCache = new HashMap<>();

    public ProcessorPropertyStats(PropertyFileReader reader) {
        this.reader = reader;
    }

    /**
     * Returns average market value in the specified ZIP Code (used in feature 4 UI class).
     * Ignores properties with non-numeric or missing market values.
     * Truncates result to an integer.
     *
     * @param zip ZIP code to filter by
     * @return average market value as an integer
     */
    public int getAverageMarketValueInZip(String zip) {
        if (avgMarketValueCache.containsKey(zip)) {
            return avgMarketValueCache.get(zip);
        }

        List<Double> marketValues = reader.getMarketValuesByZip(zip);

        if (marketValues == null || marketValues.isEmpty()) {
            avgMarketValueCache.put(zip, 0);
            return 0;
        }

        double total = 0;
        for (Double val : marketValues) {
            total += val;
        }

        int result = (int)(total / marketValues.size());
        avgMarketValueCache.put(zip, result);
        return result;
    }

    /**
     * Calculates total market value per capita for a given ZIP code (used in feature 6 UI class).
     * Uses population data and market value data from file readers.
     *
     * @param populationList List of Population objects
     * @param zip ZIP code to analyze
     * @return total market value per capita, or 0 if no population
     */
    public int getTotalMarketValuePerCapita(List<Population> populationList, String zip) {
        List<Double> marketValues = reader.getMarketValuesByZip(zip);
        if (marketValues == null || marketValues.isEmpty()) {
            return 0;
        }

        double totalValue = 0;
        for (Double val : marketValues) {
            totalValue += val;
        }

        // Find population for the given ZIP
        int totalPopulation = 0;
        for (Population pop : populationList) {
            if (pop.getZipCode().equals(zip)) {
                totalPopulation += pop.getPopulation();
            }
        }

        // Avoid division by zero
        return (totalPopulation > 0) ? (int)(totalValue / totalPopulation) : 0;
    }

    /**
     * Calculates the average livable area for a given ZIP code (Used in feature 5 UI class).
     *
     * @param zip ZIP code to filter by
     * @return average total livable area in square feet, or 0 if no valid entries
     */
    public int getAverageLivableAreaInZip(String zip) {
        List<Double> livableAreas = reader.getLivableAreasByZip(zip);
        if (livableAreas == null || livableAreas.isEmpty()) {
            return 0;
        }

        double total = 0;
        for (Double area : livableAreas) {
            total += area;
        }

        return (int)(total / livableAreas.size());
    }
}
