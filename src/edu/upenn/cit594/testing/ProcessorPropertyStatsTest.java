package edu.upenn.cit594.testing;

import edu.upenn.cit594.datamanagement.PropertyFileReader;
import edu.upenn.cit594.processor.ProcessorPropertyStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

public class ProcessorPropertyStatsTest {

    private ProcessorPropertyStats processor;

    @BeforeEach
    public void setup() throws IOException, ParseException {
    	//Update with your own file path
        PropertyFileReader reader = new PropertyFileReader("/Users/edwardfu/Documents/School/CIT594/CIT5940-Final-Project/properties.csv");
        processor = new ProcessorPropertyStats(reader);
    }

    @Test
    public void testAverageMarketValueValidZip() {
        int avg = processor.getAverageMarketValueInZip("19104");
        assertTrue(avg > 0, "Average market value for 19104 should be positive");
    }

    @Test
    public void testAverageMarketValueInvalidZip() {
        int avg = processor.getAverageMarketValueInZip("12345");
        assertEquals(0, avg, "Should return 0 for unknown ZIP");
    }

    @Test
    public void testMemoization() {
        int first = processor.getAverageMarketValueInZip("19104");
        int second = processor.getAverageMarketValueInZip("19104");
        assertEquals(first, second, "Cached value should match repeated call");
    }
}
