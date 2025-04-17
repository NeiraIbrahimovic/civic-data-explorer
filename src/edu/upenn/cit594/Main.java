package edu.upenn.cit594;

import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.ui.*;
import edu.upenn.cit594.util.*;
import edu.upenn.cit594.processor.*;
import edu.upenn.cit594.logging.Logger;

import java.util.*;

public class Main {
	//File readers that are initialized after file validation
    static PopulationFileReader populationFileReader = null;
    static CovidFileReader covidFileReader = null;
    static PropertyFileReader propertyFileReader = null;

    //Entry point of the application. Initializes input, file readers, processors, and menu.
    public static void main(String[] args) {
        //Define valid argument names the program can accept.
        Set<String> validArgs = Set.of("covid", "properties", "population", "log");

        //First, parse the arguments using the ArgumentParser class. Return null if invalid.
        Map<String, String> argMap = ArgumentParser.parse(args, validArgs);

        if (argMap == null) {
            System.out.println("Invalid or no arguments provided.");
            return;
        }


        //Try reading in each file using the FileLoader class. Throw an exception if there is an error.
        try {
            if (argMap.containsKey("covid")) {
                covidFileReader = FileLoader.loadCovidReader(argMap.get("covid"));
            }
        } catch (Exception e) {
            System.err.println("Error loading COVID file: " + e.getMessage());
            covidFileReader = null;
            //return;
        }

        try {
            if (argMap.containsKey("properties")) {
                propertyFileReader = FileLoader.loadPropertyReader(argMap.get("properties"));
            }
        } catch (Exception e) {
        	System.err.println("Error loading property file: " + e.getMessage());
            propertyFileReader = null;
            //return;
        }

        try {
            if (argMap.containsKey("population")) {
                populationFileReader = FileLoader.loadPopulationReader(argMap.get("population"));
            }
        } catch (Exception e) {
        	System.err.println("Error loading population file: " + e.getMessage());
            populationFileReader = null;
            //return;
        }

        //Get the singleton logger instance.
        Logger logger = Logger.getInstance();
        //Set the output of the logs
        logger.setOutput(argMap.get("log"));
        // Build COVID and population maps for ProcessorVaccinationStats
        Map<String, List<CovidData>> covidMap = new HashMap<>();
        if (covidFileReader != null) {  // Check if covidFileReader is null before iterating
            for (CovidData data : ((CovidFileReader) covidFileReader).getCovidData()) {
                covidMap.computeIfAbsent(data.getZipcode(), z -> new ArrayList<>()).add(data);
            }
        }

        Map<String, Integer> popMap = new HashMap<>();
        if (populationFileReader != null) {  // Check if populationFileReader is null before iterating
            for (Population p : ((PopulationFileReader) populationFileReader).getPopulationData()) {
                popMap.put(p.getZipCode(), p.getPopulation());
            }
        }

        
        //Initialize processors and UI action handlers for the features
        // Processors (initialized only if required data is available)
        ProcessorVaccinationStats vaccStats = null;
        ProcessorPopulationStats popStats = null;
        ProcessorPropertyStats propStats = null;
        ZipCodeDataAggregator aggregator = null;

// Actions (initialized only if the corresponding processor is available)
        TotalPopulationAllZip action2 = null;
        TotalVaccPerCapitaForZipForDate action3 = null;
        AvgMarketValueInZip action4 = null;
        AvgTotalLivableAreaInZip action5 = null;
        TotalMarketValuePerCapitaInZip action6 = null;
        HealthEquityScoreByZip action7 = null;

// Initialize individual processors based on available data
        if (populationFileReader != null) {
            popStats = new ProcessorPopulationStats(populationFileReader);
            action2 = new TotalPopulationAllZip(popStats);
        }

        if (covidFileReader != null && populationFileReader != null) {
            covidMap = new HashMap<>();
            for (CovidData data : covidFileReader.getCovidData()) {
                covidMap.computeIfAbsent(data.getZipcode(), z -> new ArrayList<>()).add(data);
            }

            popMap = new HashMap<>();
            for (Population p : populationFileReader.getPopulationData()) {
                popMap.put(p.getZipCode(), p.getPopulation());
            }

            vaccStats = new ProcessorVaccinationStats(covidMap, popMap);
            action3 = new TotalVaccPerCapitaForZipForDate(vaccStats);
        }

        if (propertyFileReader != null) {
            propStats = new ProcessorPropertyStats(propertyFileReader);
            action4 = new AvgMarketValueInZip(propStats);
            action5 = new AvgTotalLivableAreaInZip(propStats);
        }

        if (propertyFileReader != null && populationFileReader != null) {
            aggregator = new ZipCodeDataAggregator(covidFileReader, propertyFileReader, populationFileReader);
            action6 = new TotalMarketValuePerCapitaInZip(aggregator);

            if (covidFileReader != null) {
                action7 = new HealthEquityScoreByZip(aggregator);
            }
        }

        //Get the shared scanner instance for user input.
        Scanner scanner = new Scanner(System.in);


        //Start interactive loop for menu selection.
        while (true) {
            int choice = MainMenu.mainMenu(scanner,covidFileReader,propertyFileReader,populationFileReader);
            switch (choice) {
                case 0:
                    System.out.println("Exiting program.");
                    //Close scanner at program exit. Safe if ScannerManager handles repeated close calls.
                    //ScannerManager.closeScanner();
                    return;
                case 1:
                    MainMenu.printMenu(covidFileReader,propertyFileReader,populationFileReader);
                    break;
                case 2:
                	if (populationFileReader != null) {
                        action2.execute();
                    } else {
                        System.out.println("Required data files missing.");
                    }
                    break;
                case 3:
                	if (covidFileReader != null) {
                        action3.execute(scanner);
                    } else {
                        System.out.println("Required data files missing.");
                    }
                    break;
                case 4:
                	if (propertyFileReader != null) {
                        action4.execute(scanner);
                    } else {
                        System.out.println("Required data files missing.");
                    }
                    break;
                case 5:
                	if (propertyFileReader != null) {
                        action5.execute(scanner);
                    } else {
                        System.out.println("Required data files missing.");
                    }
                    break;
                case 6:
                	 if (propertyFileReader != null && populationFileReader != null) {
                         action6.execute(scanner);
                     } else {
                         System.out.println("Required data files missing.");
                     }
                     break;
                case 7:
                	if (covidFileReader != null && populationFileReader != null && propertyFileReader != null) {
                        action7.execute(scanner);
                    } else {
                        System.out.println("Required data files missing.");
                    }
                    break;
                default:
                	System.out.println("Invalid input. Please enter a number between 0 and 7.");
                    break;
            }
            System.out.println();
        }
    }

}

