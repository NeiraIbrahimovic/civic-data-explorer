package edu.upenn.cit594.util;

/**
 * Represents a property record with market value, livable area, and ZIP code.
 */
public class Properties {

    //Private fields for property attributes
    private double marketValue;        //Market value of the property
    private double totalLivableArea;   //Total livable square footage
    private String zipCode;            //5-digit ZIP code

    /**
     * Constructs a new Properties object with specified market value, livable area, and ZIP code.
     *
     * @param marketValue      Market value of the property
     * @param totalLivableArea Total livable area in square feet
     * @param zipCode          5-digit ZIP code as a String
     */
    public Properties(double marketValue, double totalLivableArea, String zipCode) {
        this.marketValue = marketValue;
        this.totalLivableArea = totalLivableArea;
        this.zipCode = zipCode;
    }

    /**
     * Getter for market value.
     * @return the market value of the property
     */
    public double getMarketValue() {
        return marketValue;
    }

    /**
     * Getter for total livable area.
     *
     * @return total livable square footage
     */
    public double getTotalLivableArea() {
        return totalLivableArea;
    }

    /**
     * Getter for ZIP code.
     * @return ZIP code as a String
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Returns a tab-separated string representing this property record.
     * @return formatted string with ZIP, market value, and livable area
     */
    @Override
    public String toString() {
        return zipCode + "\t" + marketValue + "\t" + totalLivableArea;
    }
}