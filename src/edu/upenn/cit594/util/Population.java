package edu.upenn.cit594.util;

public class Population {
    String zip;
    String population;


    public Population (String zip, String population) {
        this.zip = zip;
        this.population = population;
    }

    public String getZip() {
        return zip;
    }

    public String getPopulation() {
        return population;
    }


}
