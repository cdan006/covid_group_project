package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.Population;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class PopulationReader {
    protected String filename;
    protected String logFile;
    public PopulationReader(String name, Logger LogName) {
        filename = name;
        this.logFile =LogName.getLogFile();
    }

    public ArrayList<Population> getPopulationList() throws IOException {
        ArrayList <Population> populationList = new ArrayList<>();
        String zip = null;
        String totalPopulation = null;
        int zipIndex=0;
        int totalPopulationIndex=1;
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int i = 0;
            if (logFile==null) {
                System.err.println("No Logger File");
            } else {
                Logger l = Logger.getInstance();
                l.setLogFile(logFile);
                l.log(System.currentTimeMillis() + " " + filename + "\n");
            }

            //ITERATE THROUGH THE LINES OF THE FILE USING TRY CATCH TO IGNORE ANY VALUES THAT GIVE IT AN ERROR
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineSplit = line.split(",");
                if (i !=0) {
                    try {zip = lineSplit[zipIndex].substring(1,lineSplit[zipIndex].length()-1);} catch (Exception e) {zip = null;}
                    try { totalPopulation = lineSplit[totalPopulationIndex];} catch (Exception e) {totalPopulation = "0";}
                    Population population = new Population(zip, totalPopulation);
                    populationList.add(population);
                } else {
                    int j = 0;
                    while (j<lineSplit.length) {
                        lineSplit[j] = lineSplit[j].replace("\"","");
                        j++;
                    }
                    zipIndex = Arrays.asList(lineSplit).indexOf("zip_code");
                    totalPopulationIndex = Arrays.asList(lineSplit).indexOf("population");
                }
                i++;
            }
            fileReader.close();
            bufferedReader.close();
            return populationList;
        } catch (Exception var15) {
            System.out.println("File Read Error");
            return null;
        }
    }

    public String getFileName() {
        return filename;
    }


}
