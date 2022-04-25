package edu.upenn.cit594.datamanagement;

import java.io.FileReader;
import java.io.IOException;
import edu.upenn.cit594.util.Covid;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

public class CovidJson implements CovidReader {
    protected String filename;
    public CovidJson(String name) {
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
            String filePath = filename;
            JSONArray jArray = (JSONArray) (new JSONParser()).parse(new FileReader(filePath));

            for (int i=0; i <jArray.size(); ++i){
                JSONObject obj = (JSONObject)jArray.get(i);
                try {zip = obj.get("zip_code").toString();} catch (Exception e) {zip = null;} ;
                try {time = obj.get("etl_timestamp").toString();} catch (Exception e) {time = null;}
                try {partialVac = obj.get("partially_vaccinated").toString();} catch (Exception e) {partialVac = "0";} ;
                try {fullyVac = obj.get("fully_vaccinated").toString();} catch (Exception e) {fullyVac = "0";} ;
                try {boosters = obj.get("boosted").toString();} catch (Exception e) {boosters = "0";} ;
                Covid covid = new Covid(zip,time, partialVac,fullyVac, boosters);
                covidList.add(covid);
            }

        } catch (Exception var15) {
            System.out.println("File Read Error");
            return null;
        }
        return covidList;
    }

    public String getFileName() {
        return filename;
    }


}
