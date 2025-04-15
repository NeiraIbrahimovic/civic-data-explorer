package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.Population;

import java.util.List;

/**
 * Processor class for performing population-related statistical operations.
 */
public class ProcessorPopulationStats {

    private List<Population> populationList;

    /**
     * Constructor to initialize the processor with parsed population data.
     * @param populationList list of population records
     */
    public ProcessorPopulationStats(List<Population> populationList) {
        this.populationList = populationList;
    }

    /**
     * Computes the total population across all ZIP codes (used in feature 2 UI class)
	 *
     * @return total population count
     */
    public int getTotalPopulationAllZipCodes() {
        int totalPopulation = 0;
        for (Population p : populationList) {
            totalPopulation += p.getPopulation();
        }
        return totalPopulation;
    }

    /**
     * Computes the total population for a specific ZIP code (used in ZipCodeDataAggregator class).
     *
     * @param zip ZIP code to filter on
     * @return total population for the specified ZIP code
     */
    public int getPopulationForZip(String zip) {
        int totalPopulation = 0;
        for (Population p : populationList) {
            if (p.getZipCode().equals(zip)) {
                totalPopulation += p.getPopulation();
            }
        }
        return totalPopulation;
    }
}
