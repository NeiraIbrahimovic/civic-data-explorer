package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.Properties;
import edu.upenn.cit594.util.CharacterReader;
import edu.upenn.cit594.util.CSVReader;
import edu.upenn.cit594.util.CSVFormatException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class PropertyFileReader {

    // Stores all successfully parsed property records
    private final List<Properties> propertiesDataReadin = new ArrayList<>();  // Presized for large datasets

    // Header column indices
    private int marketValueIndex = -1;
    private int totalLivableAreaIndex = -1;
    private int zipCodePropertyIndex = -1;

    /**
     * Constructor that loads and parses the property file at initialization.
     *
     * @param fileName the path to the property CSV file
     * @throws IOException if the file can't be read
     * @throws ParseException if the file can't be parsed correctly
     */
    public PropertyFileReader(String fileName) throws IOException, ParseException {
        readFile(fileName);
    }

    /**
     * Reads the CSV file and extracts market value, livable area, and ZIP code for each row.
     * Skips rows with invalid data.
     *
     * @param fileName the path to the CSV file
     * @throws IOException if reading the file fails
     */
    protected void readFile(String fileName) throws IOException {
        try (CharacterReader charReader = new CharacterReader(fileName)) {
            CSVReader csvReader = new CSVReader(charReader);
            String[] header = csvReader.readRow();

            if (header == null) {
                return; // Empty file
            }

            Map<String, Integer> columnIndex = new HashMap<>();
            for (int i = 0; i < header.length; i++) {
                columnIndex.put(header[i].trim().toLowerCase(), i);
            }

            Integer zipIndex = columnIndex.get("zip_code");
            Integer marketValueIndex = columnIndex.get("market_value");
            Integer areaIndex = columnIndex.get("total_livable_area");

            if (zipIndex == null || marketValueIndex == null || areaIndex == null) {
                return; // Required columns not found
            }

            String[] row;
            while ((row = csvReader.readRow()) != null) {
                if (row.length <= Math.max(zipIndex, Math.max(marketValueIndex, areaIndex))) continue;

                String zip = row[zipIndex].trim();
                if (zip.length() < 5 || !zip.substring(0, 5).matches("\\d{5}")) continue;
                zip = zip.substring(0, 5);

                double marketValue = parseDoubleSafely(row[marketValueIndex]);
                double livableArea = parseDoubleSafely(row[areaIndex]);

                Properties p = new Properties(marketValue, livableArea, zip);
                propertiesDataReadin.add(p);
            }

        } catch (CSVFormatException e) {
            System.err.println("CSV format error: " + e.getMessage());
        }
    }

    private double parseDoubleSafely(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Returns the list of all property records that were parsed from the file.
     * @return a list of Properties objects
     */
    public List<Properties> getPropertyData() {
        return propertiesDataReadin;
    }

    /**
     * Retrieves all market values for properties in a given ZIP code.
     * Ignores zero or missing values.
     *
     * @param zip the ZIP code to filter by
     * @return list of market values for the ZIP code
     */
    public List<Double> getMarketValuesByZip(String zip) {
        List<Double> values = new ArrayList<>();
        for (Properties p : propertiesDataReadin) {
            if (p.getZipCode().equals(zip) && p.getMarketValue() > 0) {
                values.add(p.getMarketValue());
            }
        }
        return values;
    }

    /**
     * Retrieves all livable areas for properties in a given ZIP code.
     * Ignores zero or missing values.
     *
     * @param zip the ZIP code to filter by
     * @return list of livable areas for the ZIP code
     */
    public List<Double> getLivableAreasByZip(String zip) {
        List<Double> areas = new ArrayList<>();
        for (Properties p : propertiesDataReadin) {
            if (p.getZipCode().equals(zip) && p.getTotalLivableArea() > 0) {
                areas.add(p.getTotalLivableArea());
            }
        }
        return areas;
    }
}
