package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.Covid;

import java.io.IOException;
import java.util.*;

//Reader interface used for the JSONReader and TxtReader
public interface CovidReader {
    public ArrayList<Covid> getCovidList() throws IOException;

    public String getFileName();

}

