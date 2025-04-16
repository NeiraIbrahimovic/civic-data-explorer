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
        
        //Next, ensure file extensions are valid (CSV/JSON) using the FileValidator class. Abort if invalid.
        if (argMap == null || !FileValidator.validate(argMap)) return;

        //Try reading in each file using the FileLoader class. Throw an exception if there is an error.
        try {
            covidFileReader = FileLoader.loadCovidReader(argMap.get("covid"));
        } catch (Exception e) {
            System.err.println("Error loading COVID file: " + e.getMessage());
            return;
        }

        try {
            propertyFileReader = FileLoader.loadPropertyReader(argMap.get("properties"));
        } catch (Exception e) {
        	System.err.println("Error loading property file: " + e.getMessage());
            return;
        }

        try {
            populationFileReader = FileLoader.loadPopulationReader(argMap.get("population"));
        } catch (Exception e) {
        	System.err.println("Error loading population file: " + e.getMessage());
            return;
        }

        //Get the singleton logger instance.
        Logger logger = Logger.getInstance();
        //Set the output of the logs
        logger.setOutput(argMap.get("log"));
        
        // Build COVID and population maps for ProcessorVaccinationStats
        Map<String, List<CovidData>> covidMap = new HashMap<>();
        for (CovidData data : ((CovidFileReader) covidFileReader).getCovidData()) {
            covidMap.computeIfAbsent(data.getZipcode(), z -> new ArrayList<>()).add(data);
        }

        Map<String, Integer> popMap = new HashMap<>();
        for (Population p : ((PopulationFileReader) populationFileReader).getPopulationData()) {
            popMap.put(p.getZipCode(), p.getPopulation());
        }
        
        
        //Initialize processors and UI action handlers for the features
        ProcessorVaccinationStats vaccStats = new ProcessorVaccinationStats(covidMap, popMap);
        ProcessorPopulationStats popStats = new ProcessorPopulationStats(populationFileReader);
        ProcessorPropertyStats propStats = new ProcessorPropertyStats(propertyFileReader);
        ZipCodeDataAggregator aggregator = new ZipCodeDataAggregator(covidFileReader, propertyFileReader, populationFileReader);

        TotalPopulationAllZip action2 = new TotalPopulationAllZip(popStats);
        TotalVaccPerCapitaForZipForDate action3 = new TotalVaccPerCapitaForZipForDate(vaccStats);
        AvgMarketValueInZip action4 = new AvgMarketValueInZip(propStats);
        AvgTotalLivableAreaInZip action5 = new AvgTotalLivableAreaInZip(propStats);
        TotalMarketValuePerCapitaInZip action6 = new TotalMarketValuePerCapitaInZip(aggregator);
        HealthEquityScoreByZip action7 = new HealthEquityScoreByZip(aggregator);

        //Get the shared scanner instance for user input.
        Scanner scanner = ScannerManager.getScanner();

        //Start interactive loop for menu selection.
        while (true) {
            int choice = MainMenu.mainMenu(scanner);
            switch (choice) {
                case 0:
                    System.out.println("Exiting program.");
                    //Close scanner at program exit. Safe if ScannerManager handles repeated close calls.
                    //ScannerManager.closeScanner();
                    return;
                case 1:
                    MainMenu.printMenu();
                    break;
                case 2:
                	if (covidFileReader != null && populationFileReader != null) {
                        action2.execute();
                    } else {
                        System.out.println("Required data files missing.");
                    }
                    break;
                case 3:
                	if (covidFileReader != null && populationFileReader != null) {
                        action3.execute();
                    } else {
                        System.out.println("Required data files missing.");
                    }
                    break;
                case 4:
                	if (propertyFileReader != null) {
                        action4.execute();
                    } else {
                        System.out.println("Required data files missing.");
                    }
                    break;
                case 5:
                	if (propertyFileReader != null) {
                        action5.execute();
                    } else {
                        System.out.println("Required data files missing.");
                    }
                    break;
                case 6:
                	 if (propertyFileReader != null && populationFileReader != null) {
                         action6.execute();
                     } else {
                         System.out.println("Required data files missing.");
                     }
                     break;
                case 7:
                	if (covidFileReader != null && populationFileReader != null && propertyFileReader != null) {
                        action7.execute();
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

