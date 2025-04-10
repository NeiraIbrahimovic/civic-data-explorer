package edu.upenn.cit594.util;

public class CovidData {
    private String zip_code;
    private String etl_timestamp;
    private int partially_vaccinated;
    private int fully_vaccinated;


    public CovidData(String zip_code, String etl_timestamp,int partially_vaccinated,int fully_vaccinated) {
        this.zip_code = zip_code;
        this.etl_timestamp = etl_timestamp;
        this.partially_vaccinated = partially_vaccinated;
        this.fully_vaccinated = fully_vaccinated;
    }

    public String get_Zip_code() { return zip_code; }
    public String get_etl_timestamp() { return etl_timestamp; }
    public int get_partially_vaccinated() { return partially_vaccinated; }
    public int get_fully_vaccinated() { return fully_vaccinated; }

    @Override
    public String toString() {
        return zip_code + " , " + etl_timestamp + " , " + partially_vaccinated + ", " +  fully_vaccinated;
    }

}