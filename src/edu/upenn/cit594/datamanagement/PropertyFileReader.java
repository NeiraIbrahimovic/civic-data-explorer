package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.Properties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads property data from a CSV file and parses it into a list of Properties objects.
 * This class supports flexible header indexing and handles basic data validation and cleaning.
 */
public class PropertyFileReader {
	
	//Stores all successfully parsed property records
    private List<Properties> propertiesDataReadin = new ArrayList<>();
    
    //Temporary variables used while parsing each line
    private double marketValue;
    private double totalLivableArea;
    private String zipCodeProperty;
    
    //Header column indices
    int marketValueIndex = -1;
    int totalLivableAreaIndex = -1;
    int zipCodePropertyIndex = -1;

    /**
     * Constructor that loads and parses the property file at initialization.
     *
     * @param fileName the path to the property CSV file
     * @throws IOException if the file can't be read
     * @throws ParseException if the file can't be parsed correctly (not used here directly)
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
    protected void readFile(String fileName) throws IOException, ParseException {

        List<String> fileContents = Files.readAllLines(Path.of(fileName));

        //Identify header indices
        if (!fileContents.isEmpty()) {
            String headerLine = fileContents.get(0);
            String[] headers = headerLine.split(",");

            for (int i = 0; i < headers.length; i++) {
                String header = headers[i].trim();
                if (header.compareTo("market_value")==0) {
                    marketValueIndex = i;
                } else if (header.compareTo("total_livable_area")==0) {
                    totalLivableAreaIndex = i;
                } else if (header.compareTo("zip_code")==0) {
                    zipCodePropertyIndex = i;

                }

            }

        }

        //Skip header during iteration
        int i = 0;
        boolean headerline = true;

        //Read each line
        for (String line : fileContents) {
            i++;
            //System.out.println(i);
            if (headerline) {
            	//Skip the header line
                headerline = false;
                continue;
            }

            try {

            	//Use regex to split by comma while ignoring commas inside quotes
                String[] sections = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                //Extract and validate zip code
                zipCodeProperty = sections[zipCodePropertyIndex].trim();
                if (zipCodeProperty.matches("^\\d{5}.*")) {
                    zipCodeProperty = zipCodeProperty.substring(0, 5);

                } else {
                    continue; //Skip invalid ZIPs

                    //System.out.println("First 5 characters are NOT all digits." + zip_code_property+ "  " );
                }

                //Parse total livable area (or default to 0)
                if(!sections[totalLivableAreaIndex].trim().isEmpty()){
                    try {

                        totalLivableArea = Double.parseDouble(sections[totalLivableAreaIndex].trim());
                    } catch (NumberFormatException e) {
                        System.out.println("error not total_livable_area to int- " + sections[totalLivableAreaIndex]+ "  index:  " + i);
                        continue;//dont add this row if total libable aread is incorrect.
                    }
                } else{
                    totalLivableArea = 0;
                }

                //Parse market value (or default to 0)
                if (!sections[marketValueIndex].trim().isEmpty()) {
                    try {
                        marketValue = Double.parseDouble(sections[marketValueIndex].trim());
                    } catch (NumberFormatException e) {
                        System.out.println("error not market_value to int- " + sections[marketValueIndex] + "  index:  " + i);
                        continue;	//Skip this row if market value is incorrect (empty or String)
                    }
                } else{
                    marketValue = 0;
                }

                //Create a Properties object and add it to the list
                propertiesDataReadin.add(new Properties( marketValue,  totalLivableArea,  zipCodeProperty));

            } catch (NumberFormatException e) {
                System.out.println("Error parsing");
            } catch (Exception e) {
                //System.out.println("line:  " + line);
                System.out.println("Unexpected Property error parsing line:  " + e.getMessage());
            }
        }

        // Optional debug logs:uncomment to see the size that it read in after all the filter
//         System.out.println("total i: " + i);
//         System.out.println("zipCodePropertyIndex: " + zipCodePropertyIndex);
//         System.out.println("marketValueIndex: " + marketValueIndex);
//         System.out.println("totalLivableAreaIndex: " + totalLivableAreaIndex);

    }
    
    /**
     * Returns the list of all property records that were parsed from the file.
     * @return a list of Properties objects
     */
    public List<Properties> getPropertyData(){
    	return propertiesDataReadin;
    	}
    
    /**
     * Helper method: Retrieves all market values for properties in a given ZIP code.
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
     * Helper method: Retrieves all livable areas for properties in a given ZIP code.
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
