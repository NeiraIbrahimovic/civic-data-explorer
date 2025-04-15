package edu.upenn.cit594.testing;

import edu.upenn.cit594.datamanagement.PropertyFileReader;
import edu.upenn.cit594.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyFileReaderTest {

    private PropertyFileReader reader;

    @BeforeEach
    //Update this with your file path
    public void setup() throws IOException, ParseException {
        reader = new PropertyFileReader("../CIT5940_Final_Project_testFiles/src/edu/upenn/cit594/testing/properties.csv");
    }

    @Test
    public void testValidZipIsParsed() {
        List<Properties> allProps = reader.getPropertyData();
        assertFalse(allProps.isEmpty(), "Properties list should not be empty");

        boolean has19104 = allProps.stream().anyMatch(p -> p.getZipCode().equals("19104"));
        assertTrue(has19104, "Should contain at least one valid 19104 entry");
    }

    @Test
    public void testInvalidZipFilteredOut() {
        List<Properties> allProps = reader.getPropertyData();
        boolean hasInvalid = allProps.stream().anyMatch(p -> p.getZipCode().length() != 5);
        assertFalse(hasInvalid, "Should not include entries with malformed ZIP codes");
    }

    @Test
    public void testMalformedMarketValueHandled() {
        List<Properties> allProps = reader.getPropertyData();
        boolean hasNonNumeric = allProps.stream()
            .anyMatch(p -> Double.isNaN(p.getMarketValue()));
        assertFalse(hasNonNumeric, "Market value should be numeric or default to 0");
    }
}
