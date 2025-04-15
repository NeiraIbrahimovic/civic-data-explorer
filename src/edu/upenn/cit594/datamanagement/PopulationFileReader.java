package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.Population;
import edu.upenn.cit594.util.Properties;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and parses a CSV file containing ZIP code population data.
 * The file is expected to have a header with "zip_code" and "population" columns.
 */
public class PopulationFileReader {
	
	//List to store successfully parsed population records
    private List<Population> populationDataReadin = new ArrayList<>();
    
    //Temporary holders for values as we read each line
    private int population;
    private String zipCodePopulation;
    
    //Column indices for the headers
    int populationIndex = -1;
    int zipCodePopulationIndex = -1;
    
    /**
     * Constructor that reads and parses the given CSV file immediately.
     *
     * @param fileName full path to the population data file
     * @throws IOException if file reading fails
     * @throws ParseException if parsing fails (not heavily used here)
     */
    public PopulationFileReader(String fileName) throws IOException, ParseException {
        readFile(fileName);
    }
    
    /**
     * Reads the CSV file and extracts ZIP code + population data.
     * Skips malformed or invalid rows.
     */
    private void readFile(String fileName) throws IOException, ParseException {

        List<String> fileContents = Files.readAllLines(Path.of(fileName));
        if (!fileContents.isEmpty()) {
        	//Parse header line to determine column indices
            String headerLine = fileContents.get(0);

            String[] headers = headerLine.split(",");

            for (int i = 0; i < headers.length; i++) {
                String header = headers[i].trim();
                header = header.replace("\"", "");
                if (header.compareTo("population")==0) {
                    populationIndex = i;
                }else if (header.compareTo("zipCode")==0) {
                    zipCodePopulationIndex = i;
                }

            }
            //Debug prints (can remove)
            //System.out.println("populationIndex: " + populationIndex);
            //System.out.println("zipCodePopulationIndex: " + zipCodePopulationIndex);
        }
        boolean headerline = true;

        //Read data lines one by one
        for (String line : fileContents) {  
            if (headerline) {
            	//Skip the header line
                headerline = false;  
                continue;  
            }

            try {

                String[] sections = line.split(","); //Split by comma
                
                //Extract and clean the ZIP code
                zipCodePopulation = sections[zipCodePopulationIndex].trim().replace("\"", "");;

                if (zipCodePopulation.matches("^\\d{5}$")) {

                    zipCodePopulation = zipCodePopulation.substring(0, 5);

                } else {
                    System.out.println("Not exactly 5 digit. " + zipCodePopulation );
                    continue;
                }

                //Extract population value (or default to 0)
                if(!sections[populationIndex].trim().isEmpty()){
                    try {
                        population = Integer.valueOf(sections[populationIndex].trim());
                    } catch (NumberFormatException e) {
                        System.out.println("error not populatione to int- " + sections[populationIndex]);
                        continue;//Skip this row if total livable area is incorrect.
                    }
                } else{
                    population = 0;
                }

                //Add parsed data to the list
                populationDataReadin.add(new Population( zipCodePopulation,  population));

            } catch (NumberFormatException e) {
                System.out.println("Error parsing");
            } catch (Exception e) {
                System.out.println("Unexpected error parsing line");
            }
        }
        // Optional debug output: uncomment to see the size that it read in after all the filter
        // System.out.println(getPropertyData());
        // System.out.println("total i: " + i);
        // System.out.println("Properties size: " + getPropertyData().size());
    }
    
    /**
     * Returns the list of all population records that were parsed from the file.
     *
     * @return a list of Population objects
     */
    public List<Population> getPopulationData(){
    	return populationDataReadin;
    	}
    
    /**
     * Helper method: Returns the total population for a specific ZIP code.
     *
     * @param zip ZIP code to filter
     * @return total population for the ZIP, or 0 if none found
     */
    public int getPopulationByZip(String zip) {
        int total = 0;
        for (Population p : populationDataReadin) {
            if (p.getZipCode().equals(zip)) {
                total += p.getPopulation();
            }
        }
        return total;
    }


}
