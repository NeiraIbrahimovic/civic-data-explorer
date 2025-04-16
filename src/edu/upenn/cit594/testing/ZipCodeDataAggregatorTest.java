package edu.upenn.cit594.testing;

import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.processor.ZipCodeDataAggregator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

//INTEGRATION TEST
public class ZipCodeDataAggregatorTest {

    private ZipCodeDataAggregator aggregator;

    @BeforeEach
    public void setup() throws IOException, ParseException {
    	//Update file paths with your own test files
        CovidFileReader covidReader = new CSVDataFileReader("/Users/edwardfu/Documents/School/CIT594/CIT5940-Final-Project/covid_data.csv");
        PropertyFileReader propReader = new PropertyFileReader("/Users/edwardfu/Documents/School/CIT594/CIT5940-Final-Project/properties.csv");
        PopulationFileReader popReader = new PopulationFileReader("/Users/edwardfu/Documents/School/CIT594/CIT5940-Final-Project/population.csv");
        aggregator = new ZipCodeDataAggregator(covidReader, propReader, popReader);
    }

    @Test
    public void testTotalMarketValuePerCapitaValidZip() {
        int value = aggregator.getTotalMarketValuePerCapita("19104");
        assertTrue(value >= 0, "Total market value per capita should be non-negative");
    }

    @Test
    public void testTotalMarketValuePerCapitaInvalidZipReturnsZero() {
        int value = aggregator.getTotalMarketValuePerCapita("99999");
        assertEquals(0, value, "Should return 0 for invalid ZIP with no data");
    }

    @Test
    public void testMemoizationReturnsCachedResult() {
        int first = aggregator.getTotalMarketValuePerCapita("19104");
        int second = aggregator.getTotalMarketValuePerCapita("19104");
        assertEquals(first, second, "Should return cached result on second call");
    }

    @Test
    public void testHealthEquityScoresValidDate() {
        Map<String, Double> scores = aggregator.getHealthEquityScores("2021-04-01");
        assertNotNull(scores, "Scores map should not be null");
        assertTrue(scores.size() > 0, "Should contain scores for valid ZIPs with data");
        for (Map.Entry<String, Double> entry : scores.entrySet()) {
            assertTrue(entry.getValue() >= 0, "Health equity score should be non-negative");
        }
    }

    @Test
    public void testHealthEquityScoresWithNoMarketValue() {
        // Use a ZIP in the covid data but missing from properties or population to force 0 market value
        Map<String, Double> scores = aggregator.getHealthEquityScores("2021-04-01");
        assertFalse(scores.containsKey("99999"), "ZIP with no market value should not be included");
    }

    @Test
    public void testHealthEquityScoresInvalidDateReturnsEmptyMap() {
        Map<String, Double> scores = aggregator.getHealthEquityScores("2099-01-01");
        assertTrue(scores.isEmpty(), "Should return empty map for future/invalid date");
    }
}