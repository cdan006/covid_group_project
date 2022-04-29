package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.Covid;

import java.io.IOException;
import java.util.*;

//INTERFACED USED FOR THE COVIDCSV AND COVIDJSON READER
public interface CovidReader {
    public ArrayList<Covid> getCovidList() throws IOException;

    public String getFileName();

}

