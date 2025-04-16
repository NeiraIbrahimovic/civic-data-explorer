package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.Properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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
        try (BufferedReader reader = Files.newBufferedReader(Path.of(fileName))) {
            String line;
            boolean headerLine = true;

            while ((line = reader.readLine()) != null) {
                if (headerLine) {
                    String[] headers = line.split(",");
                    for (int i = 0; i < headers.length; i++) {
                        String header = headers[i].trim();
                        if (header.equalsIgnoreCase("market_value")) {
                            marketValueIndex = i;
                        } else if (header.equalsIgnoreCase("total_livable_area")) {
                            totalLivableAreaIndex = i;
                        } else if (header.equalsIgnoreCase("zip_code")) {
                            zipCodePropertyIndex = i;
                        }
                    }
                    headerLine = false;
                    continue;
                }

                try {
                    String[] sections = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                    if (sections.length <= Math.max(marketValueIndex,
                            Math.max(totalLivableAreaIndex, zipCodePropertyIndex))) {
                        continue;
                    }

                    String zip = sections[zipCodePropertyIndex].trim();
                    if (!zip.matches("^\\d{5}.*")) continue;
                    zip = zip.substring(0, 5);

                    double area = 0;
                    String areaStr = sections[totalLivableAreaIndex].trim();
                    if (!areaStr.isEmpty()) {
                        try {
                            area = Double.parseDouble(areaStr);
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }

                    double value = 0;
                    String valueStr = sections[marketValueIndex].trim();
                    if (!valueStr.isEmpty()) {
                        try {
                            value = Double.parseDouble(valueStr);
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }

                    propertiesDataReadin.add(new Properties(value, area, zip));

                } catch (Exception e) {
                    // Optional: log or suppress individual line errors
                    // System.err.println("Line skipped: " + e.getMessage());
                }
            }
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
