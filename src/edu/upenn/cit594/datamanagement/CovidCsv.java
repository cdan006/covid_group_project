package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.Covid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CovidCsv implements CovidReader {
    protected String filename;
    protected String logFile;
    public CovidCsv(String name, Logger logName) {
        filename = name;
        this.logFile =logName.getLogFile();
    }

    @Override
    public ArrayList<Covid> getCovidList() throws IOException {
        ArrayList <Covid> covidList = new ArrayList<>();
        String zip = null;
        String time = null;
        String date = null;
        String partialVac = null;
        String fullyVac = null;
        String boosters = null;
        try {
            //READ THE FILE
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            if (logFile==null) {
                System.err.println("No Logger File");
            } else {
                Logger l = Logger.getInstance();
                l.setLogFile(logFile);
                l.log(System.currentTimeMillis() + " " + filename + "\n");
            }
            String line;
            int i = 0;
            int zipIndex = 0;
            int timeIndex = 0;
            int partialVacIndex=0;
            int fullyVacIndex = 0;
            int boostersIndex = 0;
            //ITERATE THROUGH THE LINES OF THE FILE USING TRY CATCH TO IGNORE ANY VALUES THAT GIVE IT AN ERROR
            while ((line = bufferedReader.readLine()) != null) {
                String []lineSplit = line.split(",");
                if (i !=0) {
                    try {zip = lineSplit[zipIndex];} catch (Exception e) {zip = null;}
                    try {date = lineSplit[timeIndex].substring(1,11);} catch (Exception e) {date = null;} //USE SUBSTRING TO PARSE THE TIME AND DATE
                    try {time = lineSplit[timeIndex].substring(11,lineSplit[timeIndex].length()-1);} catch (Exception e) {time = null;}
                    try {partialVac = lineSplit[partialVacIndex];} catch (Exception e) {partialVac = "0";}
                    try {fullyVac = lineSplit[fullyVacIndex];} catch (Exception e) {fullyVac = "0";}
                    try {boosters = lineSplit[boostersIndex];} catch (Exception e) {boosters = "0";}
                    Covid covid = new Covid(zip, time,date, partialVac,  fullyVac, boosters);
                    covidList.add(covid);
                } else {
                    int j = 0;
                    while (j<lineSplit.length) { //DEFINE THE INDEX OF THE COLUMNS WE NEED TO INCLUDE IN OUR COVID CLASS
                        lineSplit[j] = lineSplit[j].replace("\"","");
                        j++;
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
