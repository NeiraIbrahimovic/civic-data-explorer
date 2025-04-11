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

            String zip_code = (String) jsonObj.get("zip_code");
            String etl_timestamp = (String) jsonObj.get("etl_timestamp");
            int partially_vaccinated = (int) jsonObj.get("partially_vaccinated");
            int fully_vaccinated = (int) jsonObj.get("fully_vaccinated");


            covid_data.add(new CovidData(zip_code, etl_timestamp, partially_vaccinated, fully_vaccinated));
        }
    }
}
