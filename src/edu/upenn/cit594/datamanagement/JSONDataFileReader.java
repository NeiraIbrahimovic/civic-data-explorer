package edu.upenn.cit594.datamanagement;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.CovidData;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/**
 * Reads COVID vaccination data from a JSON file.
 * The class extends CovidFileReader and overrides the readFile method.
 * Expects the JSON file to contain an array of records with:
 *   - "zip_code"
 *   - "etl_timestamp"
 *   - "partially_vaccinated"
 *   - "fully_vaccinated"
 */

public class JSONDataFileReader extends CovidFileReader {
   
	/**
     * Constructor that loads the JSON file immediately.
     *
     * @param fileName path to the JSON file
     * @throws IOException if the file cannot be read
     * @throws ParseException not used here but kept for consistency
     */
    public JSONDataFileReader(String fileName) throws IOException, ParseException {
        readFile(fileName);
    }
    
    /**
     * Reads and parses the JSON COVID data file.
     * Validates ZIP code and timestamp format.
     * Handles errors in vaccination fields gracefully.
     */
    @Override
    protected void readFile(String fileName) throws IOException {
        //Log reading the file
        Logger.getInstance().log(fileName);

        int numPartiallyVaccinatedError = 0;
        int numFullyVaccinatedError = 0;

        Object obj = null;
        
        //Attempt to parse JSON file into a JSONArray
        try {
            obj = new JSONParser().parse(new FileReader(fileName));
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e); //rethrow as unchecked exception
        }
        JSONArray jo = (JSONArray) obj;
        
        //Loop through each record in the json array and use json simple to get the text and location
        for (Object jsonElement : jo) {
            JSONObject jsonObj = (JSONObject) jsonElement;

            String zipCodeCovid = String.valueOf(jsonObj.get("zip_code"));
            String etlTimestamp = String.valueOf(jsonObj.get("etl_timestamp"));

            //Ignore row if zip is not 5 digits
            if (zipCodeCovid == null || !zipCodeCovid.matches("^\\d{5}$")) {
                continue;
            }
            //Ignore row if timestamp is not YYYY-MM-DD hh:mm:ss format
            if (etlTimestamp == null || !etlTimestamp.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")) {
                continue;
            }

            //Parse partially_vaccinated, defaulting to 0 if not a number
            int partiallyVaccinated = 0;
            Object partiallyObj = jsonObj.get("partially_vaccinated");
            if (partiallyObj instanceof Number) {
                partiallyVaccinated = ((Number) partiallyObj).intValue();
            } else{
                numPartiallyVaccinatedError++;
            }

            //Parse fully_vaccinated, defaulting to 0 if not a number
            int fullyVaccinated = 0;
            Object fullyObj = jsonObj.get("fully_vaccinated");
            if (fullyObj instanceof Number) {
                fullyVaccinated = ((Number) fullyObj).intValue();
            } else{
                numFullyVaccinatedError++;
            }

            //Add validated and parsed record to the covidData list
            covidData.add(new CovidData(zipCodeCovid, etlTimestamp, partiallyVaccinated, fullyVaccinated));
        }

        //Debug output: uncomment to see the size that it read in after all the filter
//        System.out.println("size " + covidData.size());
//        System.out.println("partiallyVaccinatedError " + numPartiallyVaccinatedError);
//        System.out.println("fullyVaccinatedError" + numFullyVaccinatedError);


        //Optional: uncomment this to show the entire covid_data read files.
        //for (CovidData data : covid_data) {
        //  System.out.println(data);
        // }  
    }

    /**
     * Returns the list of parsed CovidData records.
     *
     * @return list of CovidData
     */
    @Override
    public List<CovidData> getCovidData() {
        return covidData;
    }
    
    
    /**
     * Computes total hospitalizations (proxy: fully vaccinated counts)
     * by ZIP code for a given date.
     *
     * @param date date string in format YYYY-MM-DD
     * @return ZIP → total full vaccinations on that date
     */
    @Override
    public Map<String, Integer> getTotalHospitalizationsByZipOnDate(String date) {
        Map<String, Integer> result = new HashMap<>();
        for (CovidData data : covidData) {
            if (data.getTimestamp().startsWith(date)) {
                result.put(data.getZipcode(), result.getOrDefault(data.getZipcode(), 0) + data.getFullyVaccinated());
            }
        }
        return result;
    }
}
