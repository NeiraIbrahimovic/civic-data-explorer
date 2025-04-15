package edu.upenn.cit594.util;

/**
 * Represents a population record associated with a ZIP code.
 * Each object stores the ZIP code and the population for that area.
 */

public class Population {
	
	//Private variables for population fields
    private String zipCode; //The ZIP code as a 5-digit string
    private int population; //Population count for the given ZIP

    /**
     * Constructor to initialize the population record with a ZIP code and population value.
     *
     * @param zipCode    The 5-digit ZIP code
     * @param population The number of people in that ZIP code
     */
    
    public Population(String zipCode, int population) {
        this.zipCode = zipCode;
        this.population = population;
    }

    /**
     * Getter for the ZIP code.
     * @return the ZIP code as a String
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Getter for the population count.
     * @return the population as an integer
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Provides a string representation of the population object in the format:
     * ZIP<TAB>Population
     *
     * @return formatted string
     */
    @Override
    public String toString() {
        return zipCode + "\t" + population;
    }
}
