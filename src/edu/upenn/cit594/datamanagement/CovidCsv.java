package edu.upenn.cit594.datamanagement;

import java.io.IOException;
import java.util.ArrayList;

public class CovidCsv implements CovidReader {
    protected String filename;
    public CovidCsv(String name) {
        filename = name;
    }

    @Override
    public ArrayList<Covid> getCovidList() throws IOException {

    }

    public String getFileName() {
        return filename;
    }




}
