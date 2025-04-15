package edu.upenn.cit594.util;

/**
 * Represents a single record of COVID vaccination data.
 * Each record stores ZIP code, timestamp, and counts for partial and full vaccinations.
 */

public class CovidData {
	//Private instance variables for each data field in the COVID record
    private String zipCode; //ZIP code of the region
    private String timeStamp;  //Date and time of the record (ETL timestamp)
    private int partiallyVaccinated; //Number of individuals partially vaccinated
    private int fullyVaccinated; //Number of individuals fully vaccinated


    /**
     * Constructor to initialize a new CovidData object.
     *
     * @param zipCode             5-digit ZIP code as a string
     * @param timeStamp           ETL timestamp of the record
     * @param partiallyVaccinated Number of people partially vaccinated
     * @param fullyVaccinated     Number of people fully vaccinated
     */
    public CovidData(String zipCode, String timeStamp, int partiallyVaccinated, int fullyVaccinated) {
        this.zipCode = zipCode;
        this.timeStamp = timeStamp;
        this.partiallyVaccinated = partiallyVaccinated;
        this.fullyVaccinated = fullyVaccinated;
    }

    /**
     * Getter for ZIP code.
     * @return the ZIP code as a String
     */
    public String getZipcode(){ 
    	return zipCode; 
    }
    
    /**
     * Getter for timestamp.
     * @return the record date as a String
     */
    public String getTimestamp(){ 
    	return timeStamp; 
    }
    
    /**
     * Getter for partially vaccinated count.
     * @return number of partially vaccinated individuals
     */
    public int getPartiallyVaccinated(){ 
    	return partiallyVaccinated; 
    }
    
    /**
     * Getter for fully vaccinated count.
     * @return number of fully vaccinated individuals
     */
    public int getFullyVaccinated(){ 
    	return fullyVaccinated; 
    }

    /**
     * Returns a comma-separated string representation of this record.
     * Useful for debugging or output display.
     */
    @Override
    public String toString() {
        return zipCode + " , " + timeStamp + " , " + partiallyVaccinated + ", " +  fullyVaccinated;
    }

}