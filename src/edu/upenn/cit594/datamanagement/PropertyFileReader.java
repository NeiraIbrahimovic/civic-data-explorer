package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.Properties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class PropertyFileReader {
    private List<Properties> properties_data_readin = new ArrayList<>();
    private double market_value;
    private double total_livable_area;
    private String zip_code_property;
    int market_value_index=-1;
    int total_livable_area_index =-1;
    int zip_code_property_index=-1;


    public PropertyFileReader(String fileName) throws IOException, ParseException {
        readFile(fileName);
    }
    protected void readFile(String fileName) throws IOException, ParseException {

        List<String> fileContents = Files.readAllLines(Path.of(fileName));
        if (!fileContents.isEmpty()) {
            String headerLine = fileContents.get(0);
            String[] headers = headerLine.split(",");

            for (int i = 0; i < headers.length; i++) {
                String header = headers[i].trim();
                if (header.compareTo("market_value")==0) {
                    market_value_index = i;
                }else if (header.compareTo("total_livable_area")==0) {
                    total_livable_area_index = i;
                }else if (header.compareTo("zip_code")==0) {
                    zip_code_property_index = i;

                }

            }

        }
        int i = 0;
        boolean headerline = true;
        for (String line : fileContents) {  // Read each line
            i++;
            if (headerline) {
                headerline = false;  // Skip the first line
                continue;  // Skip the rest of the loop body for the first iteration
            }

            try {
                String[] sections = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // Split by comma
                zip_code_property = sections[zip_code_property_index].trim();
               // System.out.println();
                if (zip_code_property.matches("^\\d{5}.*")) {
                    zip_code_property = zip_code_property.substring(0, 5);

                } else {

                    //System.out.println("First 5 characters are NOT all digits." + zip_code_property+ "  " );
                }


                if(!sections[total_livable_area_index].trim().isEmpty()){
                    try {

                        total_livable_area = Double.parseDouble(sections[total_livable_area_index].trim());
                    } catch (NumberFormatException e) {
                        System.out.println("error not total_livable_area to int- " + sections[total_livable_area_index]+ "  index:  " + i);
                        continue;//dont add this row if total libable aread is incorrect.
                    }
                }else{
                    total_livable_area = 0;
                }

                if (!sections[market_value_index].trim().isEmpty()) {
                    try {
                        market_value = Double.parseDouble(sections[market_value_index].trim());
                    } catch (NumberFormatException e) {
                        System.out.println("error not market_value to int- " + sections[market_value_index] + "  index:  " + i);
                        continue;//dont add this row if market value is incorrect. empty or string
                    }
                }else{
                    market_value = 0;
                }



                properties_data_readin.add(new Properties( market_value,  total_livable_area,  zip_code_property));

            } catch (NumberFormatException e) {
                System.out.println("Error parsing");
            } catch (Exception e) {
                System.out.println("Unexpected error parsing line");
            }
        }
        //uncomment to see the size that it read in after all the filter
       // System.out.println(getProperties_data_readin());
//        System.out.println("total i: " + i);
//        System.out.println("Properties size: "+getProperties_data_readin().size());
//        System.out.println("zip_code_property_index: "+zip_code_property_index);
//        System.out.println("market_value_index: "+market_value_index);
//        System.out.println("total_livable_area_index: "+total_livable_area_index);

    }
    public List<Properties> getProperties_data_readin() {return properties_data_readin;}
}
