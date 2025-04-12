package edu.upenn.cit594.util;

public class Properties {
    private double market_value;
    private double total_livable_area;
    private String zip_code;
    public Properties(double market_value, double total_livable_area, String zip_code) {
        this.market_value = market_value;
        this.total_livable_area = total_livable_area;
        this.zip_code = zip_code;
    }
    public double getMarket_value() {
        return market_value;
    }
    public double getTotal_livable_area() {
        return total_livable_area;
    }
    public String getZip_code() {
        return zip_code;
    }
    public String toString() {
        return zip_code + "\t" + market_value + "\t" + total_livable_area;
    }
}
