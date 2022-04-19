package edu.upenn.cit594.util;

public class Population {
    int zip;
    int population;
    int marketValue;

    public Population (int zip, int population) {
        this.zip = zip;
        this.population = population;
    }

    public void setMarketValue(int marketValue) {
        this.marketValue = marketValue;
    }

    public int getMarketValue() {
        return this.marketValue;
    }

}
