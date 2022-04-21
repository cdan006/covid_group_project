package edu.upenn.cit594.util;

public class Population {
    String zip;
    String population;
    String marketValue;

    public Population (String zip, String population) {
        this.zip = zip;
        this.population = population;
    }

    public void setMarketValue(String marketValue) {
        this.marketValue = marketValue;
    }

    public String getMarketValue() {
        return this.marketValue;
    }

}
