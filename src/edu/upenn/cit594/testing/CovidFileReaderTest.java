package edu.upenn.cit594.testing;

import edu.upenn.cit594.datamanagement.CovidFileReader;
import edu.upenn.cit594.datamanagement.CSVDataFileReader;
import edu.upenn.cit594.datamanagement.JSONDataFileReader;
import edu.upenn.cit594.util.CovidData;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CovidFileReaderTest {

    @Test
    public void testCSVReaderLoadsCovidData() throws IOException, ParseException {
    	//Update with your own file path
        CovidFileReader reader = new CSVDataFileReader("covid_data.csv");
        List<CovidData> data = reader.getCovidData();
        assertFalse(data.isEmpty(), "CSV COVID data should not be empty");
        assertNotNull(data.get(0).getZipcode(), "ZIP code should not be null");
    }

    @Test
    public void testJSONReaderLoadsCovidData() throws IOException, ParseException {
    	//Update with your own file path
        CovidFileReader reader = new JSONDataFileReader("/Users/edwardfu/Documents/School/CIT594/CIT5940-Final-Project/covid_data.json");
        List<CovidData> data = reader.getCovidData();
        assertFalse(data.isEmpty(), "JSON COVID data should not be empty");
        assertNotNull(data.get(0).getZipcode(), "ZIP code should not be null");
    }

    @Test
    public void testInvalidDateOrZipSkipped() throws IOException, ParseException {
    	//Update with your own file path
        CovidFileReader reader = new CSVDataFileReader("/Users/edwardfu/Documents/School/CIT594/CIT5940-Final-Project/covid_data.csv");
        List<CovidData> data = reader.getCovidData();
        assertTrue(data.stream().noneMatch(d -> d.getZipcode() == null || d.getZipcode().length() != 5),
                "All ZIP codes should be valid 5-digit strings");
    }
}
