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

public class PopulationFileReader {
    private List<Population> population_data_readin = new ArrayList<>();
    private int population;
    private String zip_code_population;
    int population_index=-1;
    int zip_code_population_index =-1;
    public PopulationFileReader(String fileName) throws IOException, ParseException {
        readFile(fileName);
    }
    private void readFile(String fileName) throws IOException, ParseException {

        List<String> fileContents = Files.readAllLines(Path.of(fileName));
        if (!fileContents.isEmpty()) {
            String headerLine = fileContents.get(0);

            String[] headers = headerLine.split(",");

            for (int i = 0; i < headers.length; i++) {
                String header = headers[i].trim();
                header = header.replace("\"", "");
                if (header.compareTo("population")==0) {
                    population_index = i;
                }else if (header.compareTo("zip_code")==0) {
                    zip_code_population_index = i;
                }

            }


            System.out.println("population_index: "+population_index);
            System.out.println("zip_code_population_index: "+zip_code_population_index);
        }
        boolean headerline = true;

        for (String line : fileContents) {  // Read each line
            if (headerline) {
                headerline = false;  // Skip the first line
                continue;  // Skip the rest of the loop body for the first iteration
            }

            try {

                String[] sections = line.split(","); // Split by comma
                zip_code_population = sections[zip_code_population_index].trim().replace("\"", "");;

                if (zip_code_population.matches("^\\d{5}$")) {

                    zip_code_population = zip_code_population.substring(0, 5);

                } else {
                    System.out.println("Not exactly 5 digit. " + zip_code_population );
                    continue;
                }


                if(!sections[population_index].trim().isEmpty()){
                    try {

                        population = Integer.valueOf(sections[population_index].trim());
                    } catch (NumberFormatException e) {
                        System.out.println("error not populatione to int- " + sections[population_index]);
                        continue;//dont add this row if total libable aread is incorrect.
                    }
                }else{
                    population = 0;
                }


                population_data_readin.add(new Population( zip_code_population,  population));

            } catch (NumberFormatException e) {
                System.out.println("Error parsing");
            } catch (Exception e) {
                System.out.println("Unexpected error parsing line");
            }
        }
        //uncomment to see the size that it read in after all the filter
        // System.out.println(getProperties_data_readin());
//        System.out.println("total i: " + i);
        System.out.println("Properties size: "+getPopulation_data_readin().size());


    }
    public List<Population> getPopulation_data_readin() {return population_data_readin;}


}
