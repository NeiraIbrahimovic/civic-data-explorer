package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.CovidFileReader;
import edu.upenn.cit594.datamanagement.PopulationFileReader;
import edu.upenn.cit594.datamanagement.PropertyFileReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Aggregator class to combine data from population, property, and COVID sources
 * to compute ZIP-level composite metrics.
 */
public class ZipCodeDataAggregator {

    protected PropertyFileReader propReader;
    protected PopulationFileReader popReader;
    protected CovidFileReader covidReader;

    // Memoization cache for market value per capita
    protected Map<String, Integer> cache = new HashMap<>();

    public ZipCodeDataAggregator(CovidFileReader covidReader,
                                  PropertyFileReader propReader,
                                  PopulationFileReader popReader) {
        this.covidReader = covidReader;
        this.propReader = propReader;
        this.popReader = popReader;
    }

    /**
     * Computes total market value per capita for a ZIP code (used in feature 6 UI class).
     * Truncates to an integer. Returns 0 if data is missing or population is 0.
     */
    public int getTotalMarketValuePerCapita(String zip) {
        if (cache.containsKey(zip)) {
            return cache.get(zip);
        }

        List<Double> marketValues = propReader.getMarketValuesByZip(zip);
        int population = popReader.getPopulationByZip(zip);

        if (marketValues == null || marketValues.isEmpty() || population <= 0) {
            cache.put(zip, 0);
            return 0;
        }

        double totalMarketValue = 0;
        for (Double value : marketValues) {
            totalMarketValue += value;
        }

        int result = (int) (totalMarketValue / population);
        cache.put(zip, result);
        return result;
    }

    /**
     * Computes a health equity score for all ZIP codes using the given date (used in feature 7 UI class).
     * The score is defined as:
     *     score = hospitalizations / market value per capita
     * If market value is 0, the score is excluded.
     *
     * @param date The date string (format: YYYY-MM-DD) to filter hospitalization data
     * @return A map of ZIP code to health equity score (as Double)
     */
    public Map<String, Double> getHealthEquityScores(String date) {
        Map<String, Integer> hospitalizationData = covidReader.getTotalHospitalizationsByZipOnDate(date);
        Map<String, Double> scores = new HashMap<>();

        for (String zip : hospitalizationData.keySet()) {
            int hospitalizations = hospitalizationData.get(zip);
            int marketValuePerCapita = getTotalMarketValuePerCapita(zip);

            if (marketValuePerCapita > 0) {
                double score = (double) hospitalizations / marketValuePerCapita;
                scores.put(zip, score);
            }
        }

        return scores;
    }
}
