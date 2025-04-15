package edu.upenn.cit594.testing;

import edu.upenn.cit594.processor.ProcessorVaccinationStats;
import edu.upenn.cit594.util.CovidData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProcessorVaccinationStatsTest {

    private ProcessorVaccinationStats processor;

    @BeforeEach
    public void setup() {
        Map<String, List<CovidData>> covidDataByZip = new HashMap<>();
        Map<String, Integer> populationByZip = new HashMap<>();

        //Setup test data for ZIP 19104
        CovidData data1 = new CovidData("2021-04-01T00:00:00", "19104", 100, 50);
        CovidData data2 = new CovidData("2021-04-02T00:00:00", "19104", 120, 60);
        covidDataByZip.put("19104", Arrays.asList(data1, data2));
        populationByZip.put("19104", 200);

        //Setup test data for ZIP 19103 (no matching date)
        CovidData data3 = new CovidData("2021-03-31T00:00:00", "19103", 80, 40);
        covidDataByZip.put("19103", List.of(data3));
        populationByZip.put("19103", 100);

        //ZIP 19102 exists but has no population
        CovidData data4 = new CovidData("2021-04-01T00:00:00", "19102", 70, 30);
        covidDataByZip.put("19102", List.of(data4));
        //No entry in populationByZip for 19102

        processor = new ProcessorVaccinationStats(covidDataByZip, populationByZip);
    }

    @Test
    public void testPartialVaccinationPerCapita() {
        Map<String, Double> result = processor.getVaccinationsPerCapitaByZip("2021-04-01", "partial");
        assertEquals(1, result.size());
        assertEquals(0.5, result.get("19104"), 0.0001); // 100 / 200
    }

    @Test
    public void testFullVaccinationPerCapita() {
        Map<String, Double> result = processor.getVaccinationsPerCapitaByZip("2021-04-01", "full");
        assertEquals(1, result.size());
        assertEquals(0.25, result.get("19104"), 0.0001); // 50 / 200
    }

    @Test
    public void testNoMatchingDateResultsInEmptyMap() {
        Map<String, Double> result = processor.getVaccinationsPerCapitaByZip("2021-04-05", "partial");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testZipWithNoPopulationIsSkipped() {
        Map<String, Double> result = processor.getVaccinationsPerCapitaByZip("2021-04-01", "partial");
        assertFalse(result.containsKey("19102"));
    }

    @Test
    public void testMemoizationReturnsSameReference() {
        Map<String, Double> first = processor.getVaccinationsPerCapitaByZip("2021-04-01", "partial");
        Map<String, Double> second = processor.getVaccinationsPerCapitaByZip("2021-04-01", "partial");
        assertSame(first, second); // should be the same cached instance
    }
}
