package edu.upenn.cit594.datamanagement;
import edu.upenn.cit594.util.CovidData.java;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CovidFileReader {
    protected List<TweetInfo> covid_data = new ArrayList<>();

    // each subclass will implement readFile
    protected abstract void readFile(String fileName) throws IOException, ParseException;

    // it will return the list of parsed tweets
    public List<TweetInfo> get_covid_data() {
        return covid_data;
    }
}
