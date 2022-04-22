package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.Covid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CovidCsv implements CovidReader {
    protected String filename;
    public CovidCsv(String name) {
        filename = name;
    }

    @Override
    public ArrayList<Covid> getCovidList() throws IOException {
        ArrayList <Covid> covidList = new ArrayList<>();
        String zip = null;
        String time = null;
        String partialVac = null;
        String fullyVac = null;
        String boosters = null;
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int i = 0;
            int zipIndex = 0;
            int timeIndex = 0;
            int partialVacIndex=0;
            int fullyVacIndex = 0;
            int boostersIndex = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String []lineSplit = line.split(",");
                if (i !=0) {
                    try {zip = lineSplit[zipIndex];} catch (Exception e) {zip = null;}
                    try { time = lineSplit[timeIndex];} catch (Exception e) {time = null;}
                    try {partialVac = lineSplit[partialVacIndex];} catch (Exception e) {partialVac = "0";}
                    try {fullyVac = lineSplit[fullyVacIndex];} catch (Exception e) {fullyVac = "0";}
                    try {boosters = lineSplit[boostersIndex];} catch (Exception e) {boosters = "0";}
                    Covid covid = new Covid(zip, time,partialVac,  fullyVac, boosters);
                    covidList.add(covid);
                } else {
                    int l = 0;
                    while (l<lineSplit.length) {
                        lineSplit[l] = lineSplit[l].replace("\"","");
                        l++;
                    }
                    zipIndex = Arrays.asList(lineSplit).indexOf("zip_code");
                    timeIndex = Arrays.asList(lineSplit).indexOf("etl_timestamp");
                    partialVacIndex=Arrays.asList(lineSplit).indexOf("partially_vaccinated");
                    fullyVacIndex = Arrays.asList(lineSplit).indexOf("fully_vaccinated");
                    boostersIndex = Arrays.asList(lineSplit).indexOf("boosted");
                }
                i++;
            }
            fileReader.close();
            bufferedReader.close();
            return covidList;
        } catch (Exception var15) {
            System.out.println("File Read Error");
            return null;
        }
    }

    public String getFileName() {
        return filename;
    }



}
