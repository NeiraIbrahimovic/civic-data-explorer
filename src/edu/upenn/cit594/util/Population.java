package edu.upenn.cit594.util;

public class Population {
    private int population;
    private String zip_code;
    public Population(String zip_code, int population) {
        this.zip_code = zip_code;
        this.population = population;

    }
    public double getpopulation() {
        return population;
    }
    public String getZip_code() {
        return zip_code;
    }
    public String toString() {
        return zip_code + "\t" + population;
    }
}
