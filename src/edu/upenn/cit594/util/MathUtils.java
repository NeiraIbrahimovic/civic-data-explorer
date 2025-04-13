package edu.upenn.cit594.util;

import java.util.List;
import java.util.Scanner;

public class MathUtils {
    public static int getTotalPopulationAllZipCode(List<Population> populationList){
        int totalPopulation = 0;
        for (Population p : populationList) {
            totalPopulation += p.getPopulation();
        }
        return totalPopulation;
    }
    public static int getTotalPopulationSpecificZipCode(List<Population> populationList,String zipCode){
        int totalPopulation = 0;
        for (Population p : populationList) {
            if (p.getZip_code().equals(zipCode)) {
                totalPopulation += p.getPopulation();
            }

        }
        return totalPopulation;
    }

    public static int getAvgMarketValue(List<Properties> propertiesDataReadin, Scanner scanner){
        int property =0;
        double totalValue = 0;
        String input;
        while (true) {
            System.out.println("Please enter the 5 digit zip code: ");
            System.out.print(">  ");
            System.out.flush();

             input = scanner.nextLine().trim();

            if(input.matches("^\\d{5}$")) {
                break;
            }
        }
        for (Properties p : propertiesDataReadin) {
            if(p.getZip_code().equals(input)) {
                if(p.getMarket_value()!= 0){
                    property ++;
                    totalValue = totalValue + p.getMarket_value();
                }

            }
        }
        return (int) (totalValue/property);

    }

    public static int gettotalMarketValuePerCapita(List<Properties> propertiesDataReadin, List<Population> populationDataReadin, Scanner scanner) {
        int property =0;
        double totalValue = 0;
        String input;
        while (true) {
            System.out.println("Please enter the 5 digit zip code: ");
            System.out.print(">  ");
            System.out.flush();

            input = scanner.nextLine().trim();

            if(input.matches("^\\d{5}$")) {
                break;
            }
        }
        for (Properties p : propertiesDataReadin) {
            if(p.getZip_code().equals(input)) {
                if(p.getMarket_value()!= 0){
                    property ++;
                    totalValue = totalValue + p.getMarket_value();
                }

            }
        }
        int totalPopulation = getTotalPopulationSpecificZipCode(populationDataReadin,input);
        System.out.println("totalValue: " + totalValue);
        System.out.println("totalPopulation: " + totalPopulation);
        return (int)totalValue/totalPopulation;

    }
}
