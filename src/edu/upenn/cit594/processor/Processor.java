package edu.upenn.cit594.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import edu.upenn.cit594.datamanagement.ArgumentReader;
import edu.upenn.cit594.datamanagement.CovidReader;
import edu.upenn.cit594.datamanagement.PopulationReader;
import edu.upenn.cit594.datamanagement.PropertyReader;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.AverageLivableAreaFetcher;
import edu.upenn.cit594.util.Covid;
import edu.upenn.cit594.util.MarketValueFetcher;
import edu.upenn.cit594.util.Population;
import edu.upenn.cit594.util.Property;
import edu.upenn.cit594.util.PropertyListWithZipInputKey;
import edu.upenn.cit594.util.RealEstateAverageableAttributeFetcher;
import edu.upenn.cit594.util.ZipWithTwoListsInputKey;
import edu.upenn.cit594.util.vacPerCapitaInputKey;

public class Processor {
    protected CovidReader covidReader;
    protected PopulationReader populationReader;
    protected PropertyReader propertyReader;
    protected Logger logger;
    protected ArgumentReader argumentReader;

    //results maps for memoization
    private HashMap<List<Population>, Integer> totalPopulationResults = new HashMap<>();
    private HashMap<vacPerCapitaInputKey, ArrayList<String>> vacPerCapitaResults = new HashMap<>();
    private HashMap<PropertyListWithZipInputKey, Integer> avgMarketValueResults = new HashMap<>();
    private HashMap<PropertyListWithZipInputKey, Integer> avgLivableAreaResults = new HashMap<>();
    private HashMap<ZipWithTwoListsInputKey, Integer> totalMarketPerCapitaResults = new HashMap<>();

    public Processor (CovidReader covidReader,PopulationReader populationReader,PropertyReader propertyReader,
                      Logger logger, ArgumentReader argumentReader) throws IOException {
        this.covidReader = covidReader;
        this.populationReader = populationReader;
        this.propertyReader = propertyReader;
        this.logger = logger;
        this.argumentReader = argumentReader;

    }

    public ArrayList<String> availableDataSets() {
        ArrayList<String> dataArrayList = new ArrayList<String>();
        //add the lists

        //QUESTION - DO WE NEED NULL CHECKS HERE?

        dataArrayList.add(argumentReader.getCovidFile());
        dataArrayList.add(argumentReader.getPropertiesFile());
        dataArrayList.add(argumentReader.getPopulationFile());

        //QUESTION - WILL THIS PROPERLY HANDLE SITUATIONS IN WHICH WE DONT GET ALL THREE TYPES OF FILES (PER SPEC)

        //sort them into natural string order
        Collections.sort(dataArrayList);


        //note - per Piazza, no memoization needed
        //send to UI to print each entry on a different line
        return dataArrayList;
    }

    public Integer totalPopulation(List<Population> populationList) {
        if (populationList == null) {
            return -1;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }
        //error check - empty argument
        else if (populationList.isEmpty()) {
            return -1;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }
        //check results map
        else if (totalPopulationResults.containsKey(populationList)) {
            return totalPopulationResults.get(populationList);
        } else {
            //run the calculation
            //error check - null argument

            Integer runningSum = 0;
            for (Population p : populationList) {
                String stringPop = p.getPopulation();
                //parse the String to an Integer - will ignore Strings that do not parse correctly

                //TO DO - test we get the right number per the spec with this strategy

                try {
                    int intPop = (int) Integer.parseInt(stringPop);
                    runningSum = runningSum + intPop;
                }
                catch (NumberFormatException e) {
                    //do nothing - should ignore the non-numeric String
                }
            } //end of for each loop
            //add the entry to the results HashMap
            totalPopulationResults.put(populationList, runningSum);
            return runningSum;
        } //end of the else loop after checking the results map
    }

    //string comes from the UI method; date field/ obj from UI,
    public ArrayList<String> vacPerCapita(String vacType, String date, List<Covid> covidList,
                                          List<Population> populationList) {
        ArrayList<String> output = new ArrayList<>();

        //error handling
        if (populationList == null) {
            return null;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }
        //error check - empty argument
        else if (populationList.isEmpty()) {
            return null;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }
        else if (covidList == null) {
            return null;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }
        //error check - empty argument
        else if (covidList.isEmpty()) {
            return null;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }

        //check the cache
        vacPerCapitaInputKey v = new vacPerCapitaInputKey(vacType, date, covidList, populationList);
        if (vacPerCapitaResults.containsKey(v)) {
            return vacPerCapitaResults.get(v);
        }

        //make a HashMaps from the COVID list, one comparing ZIP to count of partial /full at our date
        HashMap<String, String> vacsByZip = new HashMap<>();
        for (Covid c : covidList) {
            String currentZip = c.getZip();
            String currentDate = c.getDate();
            String currentPartial = c.getPartialVac();
            String currentFully = c.getFullyVac();
            if (currentDate.equals(date)) {
                //have the right ZIP for the date, now need to determine whether we grab partial or full
                if (vacType.toLowerCase().equals("partial")) {
                    vacsByZip.put(currentZip, currentPartial);
                }
                else if (vacType.toLowerCase().equals("full")) {
                    vacsByZip.put(currentZip, currentFully);
                }
            }
        }

        //go through each ZIP in the population list, use the HashMap to compare the vac count to the pop
        //then create a string with the ZIP and rate, add to end list

        for (Population p : populationList) {
            String pZip = p.getZip();
            try {
                float currentZipPopCount = (int) Integer.parseInt(p.getPopulation());
                String vacCountString = vacsByZip.get(pZip); //get the vac count from the hashMap
                float vacCount = (int) Integer.parseInt(vacCountString);
                float vacRate = (vacCount / currentZipPopCount);
                String vacRateString = String.format("%.4f", vacRate);
                String zipOutput = pZip + " " + vacRateString;
                output.add(zipOutput);
                //QUESTION - NEED TO CONFIRM THIS TRY CATCH SETUP WORKS
            }
            catch (NumberFormatException e) {
                //do NOTHING

                //TO DO - PROBABLY NEED TO DO SOMETHING BETTER THAN THIS
            }
        } //end of the population for each loop
        Collections.sort(output); //sort the list - will put the ZIP codes in the right order for printing
        vacPerCapitaResults.put(v, output); //add to the cache
        return output;
    }

    private int avgRealEstateAttribute(String zip, List<Property> propertyList, RealEstateAverageableAttributeFetcher a) {
        //error handling
        if (zip == null) {
            return -1;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }
        //error check - empty argument
        else if (propertyList == null) {
            return -1;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }

        else if (propertyList.isEmpty()) {
            return -1;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }


        int numeratorTotal = 0;
        int denominatorCount = 0;

        for (Property p : propertyList) {
        //int attribute = a.fetchAttribute(p);
        //String zipCheck = p.getZip();
            if (zip.equals(p.getZip()) && a.fetchAttribute(p)>0) {
                denominatorCount++;
                numeratorTotal = numeratorTotal + a.fetchAttribute(p);
            }
        }
        if (denominatorCount == 0) {
            return 0;
        }
        double outputAverage = (double) (numeratorTotal / denominatorCount);
        return (int) outputAverage;
    }


    public int avgMarketValue(String zip, List<Property> propertyList) {
        //error handling
        if (zip == null) {
            return -1;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }
        //error check - empty argument
        else if (propertyList == null) {
            return -1;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }

        else if (propertyList.isEmpty()) {
            return -1;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }

        //check the cache
        PropertyListWithZipInputKey p = new PropertyListWithZipInputKey(zip, propertyList);
        if (avgMarketValueResults.containsKey(p)) {
            return avgMarketValueResults.get(p);
        }

        double output = avgRealEstateAttribute(zip, propertyList, new MarketValueFetcher());
        avgMarketValueResults.put(p, (int) output);
        return (int) output;
    }

    public int avgLivableArea(String zip, List<Property> propertyList) {
        //error handling
        if (zip == null) {
            return -1;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }
        //error check - empty argument
        else if (propertyList == null) {
            return -1;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }

        else if (propertyList.isEmpty()) {
            return -1;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }

        //check the cache
        PropertyListWithZipInputKey p = new PropertyListWithZipInputKey(zip, propertyList);
        if (avgLivableAreaResults.containsKey(p)) {
            return avgLivableAreaResults.get(p);
        }

        double output = avgRealEstateAttribute(zip, propertyList, new AverageLivableAreaFetcher());
        avgLivableAreaResults.put(p, (int) output);
        return (int) output;
    }

    public int totalMarketPerCapita(String zip, List<Property> propertyList,
                                    List<Population> populationList) {
        //error handling
        if (zip == null) {
            return -1;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }
        //error check - empty argument
        else if (propertyList == null) {
            return -1;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }

        else if (propertyList.isEmpty()) {
            return -1;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }
        //error check - empty argument
        else if (populationList == null) {
            return -1;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }

        else if (populationList.isEmpty()) {
            return -1;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }

        //check the cache
        ZipWithTwoListsInputKey z = new ZipWithTwoListsInputKey(zip, propertyList, populationList);
        if (totalMarketPerCapitaResults.containsKey(z)) {
            return totalMarketPerCapitaResults.get(z);
        }

        int numeratorTotalMarketValue = 0;
        int denominatorPopulation = 0;

        //get the numerator
        for (Property p : propertyList) {
            if (zip.equals(p.getZip())) {
                try {
                    int currentMarketValueAsInt = Integer.parseInt(p.getMarketValue());
                    numeratorTotalMarketValue = numeratorTotalMarketValue + currentMarketValueAsInt;
                }
                catch (NumberFormatException e) {
                    //do NOTHING

                    //TO DO - PROBABLY NEED TO DO SOMETHING BETTER THAN THIS
                }

            }
        }

        //get the denominator
        for (Population p : populationList) {
            String pZip = p.getZip();
            if (zip.equals(pZip)) {
                try {
                    int currentPopulationAsInt = Integer.parseInt(p.getPopulation());
                    denominatorPopulation = denominatorPopulation + currentPopulationAsInt;
                }
                catch (NumberFormatException e) {
                    //do NOTHING

                    //TO DO - PROBABLY NEED TO DO SOMETHING BETTER THAN THIS
                }

            }
        }
        if (denominatorPopulation == 0) {
            return 0;
        }
        double average = (double) (numeratorTotalMarketValue / denominatorPopulation);
        //update the cache
        totalMarketPerCapitaResults.put(z, (int) average);
        return (int) average;
    }

    public String[] extremesCovidRate(List<Population> populationList,
                                      List<Covid> covidList,
                                      List<Property> propertyList,
                                      String date) { //vacation rate of the richest zip code and the poorest zip code
        //error handling




        //check the cache



        String[] output = new String[2]; //first is richest vax rate, second is poorest vax rate

        HashMap<String, String> popByZip = new HashMap<>();
        for (Population p : populationList) {
            popByZip.put(p.getZip(), p.getPopulation());
        }

        HashMap<String, String> averageMarketValueByZip = new HashMap<>();
        for (String zip : popByZip.keySet()) {
            int zipMarketValuePerCapita = totalMarketPerCapita(zip, propertyList, populationList);
            averageMarketValueByZip.put(zip, Integer.toString(zipMarketValuePerCapita));
        }

        HashMap<String, String> vaxRateByZip = new HashMap<>();
        String maxMarketValue = "00000000";
        String richestZip = null;
        String minMarketValue = "9999999";
        String poorestZip = null;
        for (Covid c : covidList) {
            String currentZip = c.getZip();
            String currentDate = c.getDate();
            String currentFully = c.getFullyVac();
            String population = popByZip.get(currentZip);
            if (currentDate.equals(date)) {
                try {
                    vaxRateByZip.put(currentZip, String.format("%.4f",
                            (Integer.parseInt(currentFully) /
                                    Integer.parseInt(population))));
                } catch (Exception e) {}
            }



        }
        for (String s : averageMarketValueByZip.keySet()) {
            double maxMarketValueTemp = 00000000;
            double minMarketValueTemp = 9999999;
            double avgMarketValueByZip = Double.parseDouble(averageMarketValueByZip.get(s));
            //if (Double.parseDouble(maxMarketValue) <
            //		Double.parseDouble(averageMarketValueByZip.get(s))) {
            if (maxMarketValueTemp <
                    avgMarketValueByZip) {
                maxMarketValue = averageMarketValueByZip.get(s);
                richestZip = s;
            }
            if (minMarketValueTemp <
                    avgMarketValueByZip) {
                minMarketValue = averageMarketValueByZip.get(s);
                poorestZip = s;
            }
        }

        output[0] = vaxRateByZip.get(richestZip);
        output[1] = vaxRateByZip.get(poorestZip);

        return output;
    }

    public String getCovidFileName (){
        return covidReader.getFileName();
    }

    public String getPropertyFileName (){
        return propertyReader.getFileName();
    }

    public String getPopulationFileName (){
        return populationReader.getFileName();
    }

    public String getCovidArgument (){
        return argumentReader.getCovidArgument();
    }

    public String getPropertyArgument (){
        return argumentReader.getPropertiesArgument();
    }

    public String getPopulationArgument (){
        return argumentReader.getPopulationArgument();
    }

    public ArrayList<Population> getPopulationList () throws IOException {
        return populationReader.getPopulationList();
    }

    public ArrayList<Covid> getCovidList() throws IOException {
        return covidReader.getCovidList();
    }

    public ArrayList<Property> getPropertyList() throws IOException {
        return propertyReader.getPropertyList();
    }

    public ArrayList<String> boosterPerCapita(String date, List<Covid> covidList,
                                              List<Population> populationList) {
        ArrayList<String> output = new ArrayList<>();

        //error handling
        if (populationList == null) {
            return null;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }
        //error check - empty argument
        else if (populationList.isEmpty()) {
            return null;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }
        else if (covidList == null) {
            return null;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }
        //error check - empty argument
        else if (covidList.isEmpty()) {
            return null;
            //QUESTION - IS THIS GOOD ENOUGH FOR UI?
        }
        String vacType = "full";
        //check the cache
        vacPerCapitaInputKey v = new vacPerCapitaInputKey(vacType, date, covidList, populationList);
        if (vacPerCapitaResults.containsKey(v)) {
            return vacPerCapitaResults.get(v);
        }

        //make a HashMaps from the COVID list, one comparing ZIP to count of partial /full at our date
        HashMap<String, String> vacsByZip = new HashMap<>();
        for (Covid c : covidList) {
            String currentZip = c.getZip();
            String currentDate = c.getDate();
            String currentPartial = c.getPartialVac();
            String currentFully = c.getFullyVac();
            String currentBooster = c.getBoosters();
            if (currentDate.equals(date)) {
                //have the right ZIP for the date, now need to determine whether we grab partial or full
                vacsByZip.put(currentZip, currentBooster);
            }
        }

        //go through each ZIP in the population list, use the HashMap to compare the vac count to the pop
        //then create a string with the ZIP and rate, add to end list

        for (Population p : populationList) {
            String pZip = p.getZip();
            try {
                float currentZipPopCount = (int) Integer.parseInt(p.getPopulation());
                String vacCountString = vacsByZip.get(pZip); //get the vac count from the hashMap
                float vacCount = (int) Integer.parseInt(vacCountString);
                float vacRate = (vacCount / currentZipPopCount);
                String vacRateString = String.format("%.4f", vacRate);
                String zipOutput = pZip + " " + vacRateString;
                output.add(zipOutput);
                //QUESTION - NEED TO CONFIRM THIS TRY CATCH SETUP WORKS
            }
            catch (NumberFormatException e) {
                //do NOTHING

                //TO DO - PROBABLY NEED TO DO SOMETHING BETTER THAN THIS
            }
        } //end of the population for each loop
        Collections.sort(output); //sort the list - will put the ZIP codes in the right order for printing
        return output;
    }
}
