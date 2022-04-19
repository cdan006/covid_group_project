package edu.upenn.cit594.datamanagement;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import edu.upenn.cit594.util.Covid;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.sql.Timestamp;

public class CovidJson implements CovidReader {
    protected String filename;
    public CovidJson(String name) {
        filename = name;
    }

    @Override
    public ArrayList<Covid> getCovidList() throws IOException {
        ArrayList <Covid> covidList = new ArrayList<>();
        try {
            String filePath = filename;
            JSONArray jArray = (JSONArray) (new JSONParser()).parse(new FileReader(filePath));

            for (int i=0; i <jArray.size(); ++i){
                JSONObject obj = (JSONObject)jArray.get(i);
                int zip = (int) obj.get("zip_code");
                Timestamp time = Timestamp.valueOf(obj.get("2021-03-25 17:20:02").toString());
                int partialVac = (int) obj.get("partially_vaccinated");
                int fullyVac = (int) obj.get("fully_vaccinated");
                int boosters = (int) obj.get("boosted");
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
