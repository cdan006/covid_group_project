package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.ArugmentReader;
import edu.upenn.cit594.datamanagement.CovidReader;
import edu.upenn.cit594.datamanagement.PopulationReader;
import edu.upenn.cit594.datamanagement.PropertyReader;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.Covid;
import edu.upenn.cit594.util.Population;
import edu.upenn.cit594.util.Property;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Processor {
    protected CovidReader covidReader;
    protected PopulationReader populationReader;
    protected PropertyReader propertyReader;
    protected Logger logger;
    protected ArugmentReader arugmentReader;

    public Processor (CovidReader covidReader,PopulationReader populationReader,PropertyReader propertyReader,  Logger logger) throws IOException {
        this.covidReader = covidReader;
        this.populationReader = populationReader;
        this.propertyReader = propertyReader;
        this.logger = logger;

    }


    public ArrayList<String> availableDataSets(HashMap arugmentReader) { //take in whatever type we define and turn it into array of strings
        ArrayList<String> DataArrayList = new ArrayList<String>();
        return DataArrayList;
    }

    public ArrayList<String> getAvailableDataSets() {
        return availableDataSets;
    }

    public int totalPopulation(List<Population> Population) {
        return 0;
    } //iterate through all of the population and send it through to the UI

    public int getTotalPopulation() {
        return totalPopulation;
    }

    public ArrayList vacPerCapita(String vacType, Date date, List<Covid> covidList, HashMap zipPopulation) { //string comes from the UI method; date field/ obj from UI,
        //SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        //Date timeStampDate = dt1.format(fileDate); //change the 'fileDate' to the date from the zip
        return null;
    }

    public HashMap zipPopulation (List<Population> List) {
        return null;
    }

    public double getVacPerCapita() {
        return this.vacPerCapita;
    }

    public int avgMarketValue(int zip, List<Property> propertyList) { //need to implement strategy design

        return 0;
    }

    public int getAvgMarketValue() {
        return this.avgMarketValue;
    }

    public int avgLivableArea(int zip, List<Property> propertyList) { //need to implement strategy design
        return 0;
    }

    public int getAvgLivableArea() {
        return this.avgMarketValue;
    }

    public int totalMarketPerCapita(int zip, List<Property> propertyList, List<Population> populationList) {
        return 0;
    }

    public int getMarketPerCapita() {
        return this.MarketPerCapita;
    }

    public int extremesCovidRate(int zip, List<Property> propertyList) {
        return 0;
    } //vacation rate of the richest zip code and the poorest zip code

    public int getExtremesCovidRate() {
        return this.highestMarketValueZip;
    } //additional feature

    public Result doCalculation(Input input) {
    }

}
