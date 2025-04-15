package edu.upenn.cit594.testing;

import edu.upenn.cit594.datamanagement.PopulationFileReader;
import edu.upenn.cit594.util.Population;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PopulationFileReaderTest {

    private PopulationFileReader reader;

    @BeforeEach
    //Update the file path to point to your file
    public void setup() throws IOException, ParseException {
        reader = new PopulationFileReader("../CIT5940_Final_Project_testFiles/src/edu/upenn/cit594/testing/population.csv");
    }

    @Test
    public void testReadValidPopulationData() {
        List<Population> populations = reader.getPopulationData();
        assertFalse(populations.isEmpty(), "Population list should not be empty");

        boolean found19104 = populations.stream()
                .anyMatch(p -> p.getZipCode().equals("19104") && p.getPopulation() > 0);
        assertTrue(found19104, "Should contain valid ZIP 19104 entry with positive population");
    }

    @Test
    public void testIgnoreInvalidZip() {
        List<Population> populations = reader.getPopulationData();
        boolean foundInvalid = populations.stream().anyMatch(p -> p.getZipCode().length() != 5 || !p.getZipCode().matches("\\d{5}"));
        assertFalse(foundInvalid, "Should ignore entries with invalid ZIP codes");
    }
}
