package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.CovidData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads COVID vaccination data from a CSV file.
 * This implementation handles header-based indexing and quoted field parsing,
 * and is RFC 4180-compliant.
 */
public class CSVDataFileReader extends CovidFileReader {

    //Local storage of CovidData records
    protected List<CovidData> covidData = new ArrayList<>();

    //Expected date format in CSV file
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructor reads the file and populates covidData.
     *
     * @param fileName path to the CSV file
     * @throws IOException if file reading fails
     * @throws ParseException if date fields are invalid
     */
    public CSVDataFileReader(String fileName) throws IOException, ParseException {
        readFile(fileName);
    }

    /**
     * Reads the CSV file and populates the covidData list.
     * Handles header parsing and row-by-row field extraction.
     */
    @Override
    protected void readFile(String fileName) throws IOException, ParseException {
        //Log reading the file
        Logger.getInstance().log(fileName);

        List<String> lines = Files.readAllLines(Path.of(fileName));
        if (lines.isEmpty()) return;

        //Parse header row to identify field indices
        String headerLine = lines.get(0);
        String[] headers = parseCSVLine(headerLine);

        int zipIndex = -1, dateIndex = -1, partialIndex = -1, fullIndex = -1;

        //Identify relevant columns by header name
        for (int i = 0; i < headers.length; i++) {
            String header = headers[i].trim().toLowerCase();
            if (header.equals("zip_code")) zipIndex = i;
            else if (header.equals("etl_timestamp")) dateIndex = i;
            else if (header.equals("partially_vaccinated")) partialIndex = i;
            else if (header.equals("fully_vaccinated")) fullIndex = i;
        }

        //Parse each row, skipping malformed lines
        for (int i = 1; i < lines.size(); i++) {
            String[] fields = parseCSVLine(lines.get(i));
            if (fields.length <= Math.max(Math.max(zipIndex, dateIndex), Math.max(partialIndex, fullIndex))) continue;

            String zip = fields[zipIndex].trim();
            String dateStr = fields[dateIndex].trim();
            String partial = fields[partialIndex].trim();
            String full = fields[fullIndex].trim();

            //Validate ZIP and date presence
            if (!zip.matches("\\d{5}") || dateStr.isEmpty()) continue;
            
            int partialVacc = partial.isEmpty() ? 0 : Integer.parseInt(partial);
            int fullVacc = full.isEmpty() ? 0 : Integer.parseInt(full);

            //Add parsed data to the list
            covidData.add(new CovidData(zip, dateStr, partialVacc, fullVacc));
        }
    }

    /**
     * Returns the list of parsed CovidData records.
     *
     * @return list of CovidData
     */
    @Override
    public List<CovidData> getCovidData() {
        return covidData;
    }

    /**
     * Parses a single line of CSV data, handling quoted values correctly.
     * Delimiters (commas) inside quoted fields are ignored.
     *
     * @param line a raw CSV line
     * @return array of parsed fields
     */
    private String[] parseCSVLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes; //Toggle quote state
            } else if (c == ',' && !inQuotes) {
                tokens.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        //Add final field
        tokens.add(current.toString());
        return tokens.toArray(new String[0]);
    }
    
    /**
     * Computes total hospitalizations (proxy: fully vaccinated counts)
     * by ZIP code for a given date.
     *
     * @param date date string in format YYYY-MM-DD
     * @return ZIP → total full vaccinations on that date
     */
    @Override
    public Map<String, Integer> getTotalHospitalizationsByZipOnDate(String date) {
        Map<String, Integer> result = new HashMap<>();
        for (CovidData data : covidData) {
            if (data.getTimestamp().startsWith(date)) {
                result.put(data.getZipcode(), result.getOrDefault(data.getZipcode(), 0) + data.getFullyVaccinated());
            }
        }
        return result;
    }
}
