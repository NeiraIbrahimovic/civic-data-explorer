package edu.upenn.cit594.datamanagement;
import edu.upenn.cit594.util.CovidData;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public abstract class CovidFileReader {
    protected List<CovidData> covid_data = new ArrayList<>();

    // each subclass will implement readFile
    protected abstract void readFile(String fileName) throws IOException, ParseException;

    // it will return the list of parsed tweets
    public List<CovidData> get_covid_data() {
        return covid_data;
    }
}
