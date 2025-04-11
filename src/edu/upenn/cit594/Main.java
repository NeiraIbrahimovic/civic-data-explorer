package edu.upenn.cit594;

import edu.upenn.cit594.datamanagement.CovidFileReader;
import edu.upenn.cit594.datamanagement.JSONDataFileReader;
import edu.upenn.cit594.ui.MainMenu;
import edu.upenn.cit594.util.ScannerManager;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	//testing
	public static void main(String[] args) {
		// only arg that matches following will work
		Set<String> validArgNames = Set.of("covid", "properties", "population", "log");
		Pattern pattern = Pattern.compile("^--(?<name>.+?)=(?<value>.+)$");
		Map<String, String> argMap = new HashMap<>();
		// go through each arg make it pattern and arg matches, if so get the name and value if not throw error
		for (String arg : args) {
			Matcher matcher = pattern.matcher(arg);
			if (!matcher.matches()) {
				System.err.println("Error: Invalid argument format: " + arg);
				return;
			}

			String name = matcher.group("name");
			String value = matcher.group("value");
			//if name is not one of the valid name, throw error
			if (!validArgNames.contains(name)) {
				System.err.println("Error: Invalid argument name: " + name);
				return;
			}

			//if there is already a key associated with that name, that means there is a duplicate
			if (argMap.containsKey(name)) {
				System.err.println("Error: Duplicate argument: " + name);
				return;
			}

			argMap.put(name, value);
		}
		String covidFile = argMap.get("covid");
		String propertiesFile = argMap.get("properties");
		String populationFile = argMap.get("population");
		String logFile = argMap.get("log");

		System.out.println("COVID File: " + covidFile + "  Properties File: " + propertiesFile + "  Population File: " + populationFile + "  Log File: " + logFile);
		//---------by this point arg should be valid, if not it will throw error already -----------//

		//check for file extension
		if( !(covidFile.toLowerCase().endsWith(".json") || covidFile.toLowerCase().endsWith(".csv"))){
			System.err.println("Error: Covid File Format Incorrect: " + covidFile);
			return;
		}
		if( !(propertiesFile.toLowerCase().endsWith(".csv") && populationFile.toLowerCase().endsWith(".csv"))){
			System.err.println("Error: properties/population File Format Incorrect");
			return;
		}

		//----------by this point all arg inputs are valid-----------------------//
		Scanner scanner = ScannerManager.getScanner();

		//----------parsing in data---------------//
		CovidFileReader covidFileReader = null;
		if(covidFile.toLowerCase().endsWith(".json")){
			File file = new File(covidFile);
			if(!file.exists() || !file.canRead()){
				System.out.println("Error: Covid File Not Found: " + covidFile);
				return;
			}
			try{
				// will read in this format "19102 , 2021-03-25 17:20:02 , 1185, 1145"
				// zip, date and time, partial vacc , full vacc
				covidFileReader = new JSONDataFileReader(covidFile);
			} catch (IOException e) {
				System.out.println("error reading file");
            } catch (ParseException e) {
				System.out.println("error parsing file");
            }

        }

		//-----------go through the main menu-----------------//
		while(true) {
			int choice = MainMenu.mainMenu(scanner);
			switch (choice) {
				case 0:
					System.out.println("Exiting program.");
					ScannerManager.closeScanner(); //only need to close it here
					return;
				case 1:
					//Show the available actions
					break;
				case 2:
					//Show total population
					break;
				case 3:
					//Show vaccinations per capita
					break;
				case 4:
					//Show average market value for ZIP
					break;
				case 5:
					//Show average livable area for ZIP
					break;
				case 6:
					//Show total market value per capita
					break;
				case 7:
					//Custom feature
					break;
				default:
					System.out.println("Invalid option.");
			}

			System.out.println();
		}


	}
}
