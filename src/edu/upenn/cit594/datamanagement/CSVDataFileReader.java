package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.CovidData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CSVDataFileReader extends CovidFileReader{
    private List<CovidData> covid_data_readin = new ArrayList<>();

    private int zip_code_covid_index ;
    private int etl_timestamp_index ;
    private int partially_vaccinated_index ;
    private int fully_vaccinated_index ;

    String zip_code_covid;
    String etl_timestamp;
    int partially_vaccinated =0;
    int fully_vaccinated=0;
    int num_partially_vaccinated_error =0;
    int num_fully_vaccinated_error =0;
    boolean headerline = true;


    public CSVDataFileReader(String fileName) throws IOException, ParseException {
        readFile(fileName);
    }

    @Override
    protected void readFile(String fileName) throws IOException, ParseException {
        List<String> fileContents = Files.readAllLines(Path.of(fileName));
        if (!fileContents.isEmpty()) {
            String headerLine = fileContents.get(0);
            String[] headers = headerLine.split(",");

            for (int i = 0; i < headers.length; i++) {
                String header = headers[i].replace("\"", "");
                if (header.compareTo("zip_code")==0) {
                    zip_code_covid_index = i;
                }else if (header.compareTo("etl_timestamp")==0) {
                    etl_timestamp_index = i;
                }else if (header.compareTo("partially_vaccinated")==0) {
                    partially_vaccinated_index = i;
                }else if (header.compareTo("fully_vaccinated")==0) {
                    fully_vaccinated_index = i;
                }else{
                    System.out.println("error");
                }

            }

        }

        for (String line : fileContents) {  // Read each line
            if (headerline) {
                headerline = false;  // Skip the first line
                continue;  // Skip the rest of the loop body for the first iteration
            }

            try {
                String[] sections = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // Split by comma
                zip_code_covid = sections[zip_code_covid_index].replace("\"", "");
                etl_timestamp = sections[etl_timestamp_index].replace("\"", "");

                // ignore row if zip is not 5 digit
                if (!zip_code_covid.matches("^\\d{5}$")) {
                    System.out.println(zip_code_covid);
                    continue;
                }
                // ignore row if timestamp is not YYYY-MM-DD hh:mm:ss format
                if (!etl_timestamp.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")) {
                    System.out.println("in not match timestamp");
                    System.out.println(etl_timestamp);
                    continue;
                }


                try {
                    partially_vaccinated = Integer.valueOf(sections[partially_vaccinated_index].replace("\"", ""));
                } catch (NumberFormatException e) {

                    num_partially_vaccinated_error++;
                    partially_vaccinated = 0;  // Set to 0 if the value is not a valid integer
                }
                try {
                    partially_vaccinated = Integer.valueOf(sections[fully_vaccinated_index].replace("\"", ""));
                } catch (NumberFormatException e) {
                    num_fully_vaccinated_error++;
                    fully_vaccinated = 0;  // Set to 0 if the value is not a valid integer
                }



                // ignore row if zip is not 5 digit
//                if (zip_code_covid == null || !zip_code_covid.matches("^\\d{5}$")) {
//                    continue;
//                }
                // ignore row if timestamp is not YYYY-MM-DD hh:mm:ss format
//                if (!etl_timestamp.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")) {
//                    continue;
//                }

                // Get the state, lat, and long, and add it to the locations list
                covid_data_readin.add(new CovidData(zip_code_covid, etl_timestamp, partially_vaccinated,fully_vaccinated));

            } catch (NumberFormatException e) {
                System.out.println("Error parsing");
            } catch (Exception e) {
                System.out.println("Unexpected error parsing line");
            }
        }
        //uncomment to see the size that it read in after all the filter
        System.out.println("size: "+getCovid_data_readin().size());
        System.out.println("partially_vaccinated_error: " + num_partially_vaccinated_error);
        System.out.println("fully_vaccinated_error: " + num_fully_vaccinated_error);

    }
    public List<CovidData> getCovid_data_readin() {return covid_data_readin;}


}
