package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.Population;

import java.io.IOException;
import java.util.ArrayList;

public class PopulationReader {
    protected String filename;
    public PopulationReader(String name) {
        filename = name;
    }

    public ArrayList<Population> getPropertyList() throws IOException {
        return null;
    }

    public String getFileName() {
        return filename;
    }
    //set up intellji and set up a new responsitory. Create a local folder and copy your code.  Set up git on the folder.  Need credentials, etc.
}
