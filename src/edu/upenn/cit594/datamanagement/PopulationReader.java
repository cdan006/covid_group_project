package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.Population;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class PopulationReader {
    protected String filename;
    public PopulationReader(String name) {
        filename = name;
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
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineSplit = line.split(",");
                if (i !=0) {
                    try {zip = lineSplit[zipIndex];} catch (Exception e) {zip = null;}
                    try { totalPopulation = lineSplit[totalPopulationIndex];} catch (Exception e) {totalPopulation = "0";}
                    Population population = new Population(zip, totalPopulation);
                    populationList.add(population);
                } else {
                    int l = 0;
                    while (l<lineSplit.length) {
                        lineSplit[l] = lineSplit[l].replace("\"","");
                        l++;
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
