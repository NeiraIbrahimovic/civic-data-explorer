package edu.upenn.cit594.datamanagement;
import edu.upenn.cit594.util.CovidData;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class JSONDataFileReader extends CovidFileReader {
    public JSONDataFileReader(String fileName) throws IOException, ParseException {
        readFile(fileName);
    }
    // will need to override the readFile
    //read in all the lines

    @Override
    protected void readFile(String fileName) throws IOException {

        Object obj = null;
        //try to make sure the file can be parse
        try {
            obj = new JSONParser().parse(new FileReader(fileName));
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }
        JSONArray jo = (JSONArray) obj;
        //loop thourgh each line and use json simple to get the text and location
        for (Object jsonElement : jo) {
            JSONObject jsonObj = (JSONObject) jsonElement;

            String zip_code = String.valueOf(jsonObj.get("zip_code"));
            String etl_timestamp = String.valueOf(jsonObj.get("etl_timestamp"));

            // ignore row if zip is not 5 digit
            if (zip_code == null || !zip_code.matches("^\\d{5}$")) {
                continue;
            }
            // ignore row if timestamp is not YYYY-MM-DD hh:mm:ss format
            if (etl_timestamp == null || !etl_timestamp.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")) {
                continue;
            }

            int partially_vaccinated = 0;
            Object partiallyObj = jsonObj.get("partially_vaccinated");
            if (partiallyObj instanceof Number) {
                partially_vaccinated = ((Number) partiallyObj).intValue();
            }

            int fully_vaccinated = 0;
            Object fullyObj = jsonObj.get("fully_vaccinated");
            if (fullyObj instanceof Number) {
                fully_vaccinated = ((Number) fullyObj).intValue();
            }


            covid_data.add(new CovidData(zip_code, etl_timestamp, partially_vaccinated, fully_vaccinated));
        }
        // uncomment this to show the entire covid_data read files.
//        for (CovidData data : covid_data) {
//            System.out.println(data);
//        }
    }
}
