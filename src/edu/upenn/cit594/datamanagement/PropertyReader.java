package edu.upenn.cit594.datamanagement;

import java.io.IOException;
import java.util.ArrayList;

public class PropertyReader {
    protected String filename;
    public PropertyReader(String name) {
        filename = name;
    }

    public ArrayList<Property> getPropertyList() throws IOException {
        return null;
    }

    public String getFileName() {
        return filename;
    }



}

