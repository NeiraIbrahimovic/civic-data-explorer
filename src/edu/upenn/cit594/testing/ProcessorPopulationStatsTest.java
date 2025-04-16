package edu.upenn.cit594.testing;

import edu.upenn.cit594.datamanagement.PopulationFileReader;
import edu.upenn.cit594.processor.ProcessorPopulationStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

public class ProcessorPopulationStatsTest {

    private ProcessorPopulationStats processor;

    @BeforeEach
    public void setup() throws IOException, ParseException {
    	//Update with your own file path
        PopulationFileReader reader = new PopulationFileReader("/Users/edwardfu/Documents/School/CIT594/CIT5940-Final-Project/population.csv");
        processor = new ProcessorPopulationStats(reader);
    }

    @Test
    public void testTotalPopulationComputation() {
        int totalPop = processor.getTotalPopulationAllZipCodes();
        assertTrue(totalPop > 1000000, "Total population should be greater than 1 million");
    }

    @Test
    public void testMemoization() {
        int first = processor.getTotalPopulationAllZipCodes();
        int second = processor.getTotalPopulationAllZipCodes();
        assertEquals(first, second, "Memoized result should be the same on repeated calls");
    }
}
