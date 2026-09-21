package edu.upenn.cit594.testing;

import edu.upenn.cit594.Main;
import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.ui.MainMenu;
import edu.upenn.cit594.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

/** Small, synthetic inputs reproduce defects without redistributing course datasets. */
public class PortfolioRegressionTest {
    @TempDir Path temp;
    private String file(String name, String text) throws IOException {
        return Files.writeString(temp.resolve(name), text).toString();
    }
    private String run(String[] args, String input) {
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(output));
            Main.main(args);
            return output.toString(StandardCharsets.UTF_8);
        } finally {
            edu.upenn.cit594.logging.Logger.getInstance().setOutput(null);
            System.setIn(oldIn);
            System.setOut(oldOut);
        }
    }
    @Test void covidWithoutPopulationDoesNotOfferPerCapita() throws Exception {
        CovidFileReader covid = new CSVDataFileReader(file("covid.csv",
            "zip_code,etl_timestamp,partially_vaccinated,fully_vaccinated\n19104,2021-04-01 00:00:00,100,50\n"));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream old = System.out;
        try {
            System.setOut(new PrintStream(output));
            MainMenu.printMenu(covid, null, null);
        } finally { System.setOut(old); }
        assertFalse(output.toString().lines().anyMatch("3"::equals));
    }
    @Test void endOfInputExitsMenu() {
        assertEquals(0, MainMenu.mainMenu(new Scanner(""), null, null, null));
    }
    @Test void repeatedMainDoesNotReuseOldPopulation() throws Exception {
        String population = file("population.csv", "zip_code,population\n19104,200\n");
        run(new String[]{"--population=" + population}, "0\n");
        String output = run(new String[]{"--log=" + temp.resolve("events.log")}, "1\n0\n");
        String actions = output.substring(output.indexOf("BEGIN OUTPUT"), output.indexOf("END OUTPUT"));
        assertFalse(actions.lines().anyMatch("2"::equals));
    }
    @Test void missingCovidHeaderReportsInputError() throws Exception {
        String path = file("missing.csv", "zip_code,etl_timestamp\n19104,2021-04-01 00:00:00\n");
        assertThrows(IOException.class, () -> new CSVDataFileReader(path));
    }
    @Test void malformedVaccinationRowDoesNotDiscardValidRows() throws Exception {
        CSVDataFileReader reader = new CSVDataFileReader(file("mixed.csv",
            "zip_code,etl_timestamp,partially_vaccinated,fully_vaccinated\n"
            + "19104,2021-04-01 00:00:00,bad,50\n19103,2021-04-01 00:00:00,20,10\n"));
        assertEquals(1, reader.getCovidData().size());
        assertEquals("19103", reader.getCovidData().get(0).getZipcode());
    }
    @Test void trailingEmptyCsvFieldIsPreserved() throws Exception {
        try (CharacterReader chars = new CharacterReader(file("trailing.csv", "a,b,"))) {
            assertArrayEquals(new String[]{"a", "b", ""}, new CSVReader(chars).readRow());
        }
    }
    @Test void quotedCsvHandlesEscapesAndEmbeddedNewline() throws Exception {
        try (CharacterReader chars = new CharacterReader(file("quoted.csv", "\"a\"\"b\",\"c\nd\"\n"))) {
            assertArrayEquals(new String[]{"a\"b", "c\nd"}, new CSVReader(chars).readRow());
        }
    }
    @Test void nonFinitePropertyValuesExcluded() throws Exception {
        PropertyFileReader reader = new PropertyFileReader(file("properties.csv",
            "zip_code,market_value,total_livable_area\n19104,Infinity,NaN\n19104,100,25\n"));
        assertEquals(java.util.List.of(100.0), reader.getMarketValuesByZip("19104"));
        assertEquals(java.util.List.of(25.0), reader.getLivableAreasByZip("19104"));
    }
}
