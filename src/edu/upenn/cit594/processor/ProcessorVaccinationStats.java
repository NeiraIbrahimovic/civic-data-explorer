package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.CovidData;

import java.util.*;

/**
 * This processor handles calculations related to COVID vaccinations and populations.
 * Specifically, it computes vaccination rates per ZIP code on a given date.
 * 
 * Feature 3 logic is implemented here.
 */
public class ProcessorVaccinationStats {

    //Mapping of ZIP code → list of CovidData records
    protected Map<String, List<CovidData>> covidDataByZip;

    //Mapping of ZIP code → total population (from population file)
    protected Map<String, Integer> populationByZip;

    //Cache to store already computed results for fast lookups (memoization)
    //Format: "type:date" → Map<ZIP, rate>
    protected Map<String, Map<String, Double>> memoizedResults = new HashMap<>();

    /**
     * Constructor to initialize the processor with grouped COVID data
     * and total population per ZIP code.
     *
     * @param covidDataByZip   COVID vaccination data grouped by ZIP
     * @param populationByZip  population totals grouped by ZIP
     */
    public ProcessorVaccinationStats(Map<String, List<CovidData>> covidDataByZip, Map<String, Integer> populationByZip) {
        this.covidDataByZip = covidDataByZip;
        this.populationByZip = populationByZip;
    }

    /**
     * Computes the vaccination rate per ZIP code for the given date and type (partial/full) (used by feature 3 UI class).
     * Results are cached.
     *
     * @param date The target date (YYYY-MM-DD)
     * @param type "partial" or "full"
     * @return A map of ZIP code → vaccination rate per capita (as a double)
     */
    public Map<String, Double> getVaccinationsPerCapitaByZip(String date, String type) {
        String memoKey = type + ":" + date;

        //Return cached result if it already exists
        if (memoizedResults.containsKey(memoKey)) {
            return memoizedResults.get(memoKey);
        }

        Map<String, Double> result = new HashMap<>();

        //Loop over all ZIP codes
        for (String zip : covidDataByZip.keySet()) {
            List<CovidData> records = covidDataByZip.get(zip);




            //Search for the first CovidData record matching the given date
            for (CovidData record : records) {

                if (record.getTimestamp().startsWith(date)) {

                    int population = populationByZip.getOrDefault(zip, 0);
                    if (population == 0) break; // skip if no population data

                    //Get the correct vaccination count based on type
                    int count = 0;
                    if (type.equals("partial")) {
                        count = record.getPartiallyVaccinated();
                    } else if (type.equals("full")) {
                        count = record.getFullyVaccinated();
                    }

                    //Skip zip if vaccination count is zero
                    if (count > 0) {
                        double perCapita = (double) count / population;
                        result.put(zip, perCapita);
                    }

                    //Stop once the relevant record is found
                    break; 
                }
            }
        }

        //Cache the result for future reuse
        memoizedResults.put(memoKey, result);
        return result;
    }
}