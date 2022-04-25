package edu.upenn.cit594.ui;

import edu.upenn.cit594.datamanagement.ArgumentReader;
import edu.upenn.cit594.datamanagement.CovidReader;
import edu.upenn.cit594.datamanagement.PopulationReader;
import edu.upenn.cit594.datamanagement.PropertyReader;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.Processor;

import java.util.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInterface {
    protected Processor processor;
    protected Logger logger;
    //protected CovidReader covidReader;
    //protected PopulationReader populationReader;
    //protected PropertyReader propertyReader;
    //protected ArgumentReader arugmentReader;

    public UserInterface (Processor processor, Logger logger) throws IOException {
        this.processor = processor;
        this.logger = logger;
    }

    public int possibleActions() { //this is where we capture the user's input, 1, 2, 3, etc.
        System.out.println("Select the action you want to perform\n"+
        "0. Exit the program.\n"+
        "1. Show the available data sets (subsection 3.1).\n"+
        "2. Show the total population for all ZIP Codes (subsection 3.2).\n"+
        "3. Show the total vaccinations per capita for each ZIP Code for the specied date (subsection 3.3).\n"+
        "4. Show the average market value for properties in a specied ZIP Code (subsection 3.4).\n"+
        "5. Show the average total livable area for properties in a specied ZIP Code (subsection 3.5).\n"+
        "6. Show the total market value of properties, per capita, for a specied ZIP Code (subsection 3.6).\n"+
        "7. Show the results of your custom feature (subsection 3.7).");
        int intResponse = 0;
        System.out.flush();
        System.out.println("> ");
        Scanner sc = new Scanner(System.in);
        String response = sc.nextLine();
        try {
            intResponse = Integer.parseInt(response);
            if (intResponse>7 || intResponse<0) {
                System.out.flush();
                System.out.println("Please enter a valid integer (0-7) response\n");
                possibleActions();
            }

        } catch (Exception e) {
            System.out.flush();
            System.out.println("Please enter a valid integer (0-7) response\n");
            possibleActions();
        }
        return intResponse;
    }

    public void display() { //show the output of the 1-7
        int intResponse = possibleActions();
        if (intResponse==0) {
            System.exit(0);
        }
        else if (intResponse==1) {
            ArrayList<String> files = new ArrayList<String>();
            files.add(processor.getCovidArgument());
            files.add(processor.getPropertyArgument());
            files.add(processor.getPopulationArgument());
            Collections.sort(files);
            System.out.println("BEGIN OUTPUT"); System.out.flush();
            System.out.println(files); System.out.flush();
            System.out.println("END OUTPUT"); System.out.flush();
        } else if (intResponse==2) {
            //int totalPopulation = processor.getTotalPopulation(); System.out.flush();
            System.out.println("BEGIN OUTPUT"); System.out.flush();
            //System.out.println(totalPopulation); System.out.flush();
            System.out.println("END OUTPUT"); System.out.flush();
        } else if (intResponse==3) {
            vacPerCapitaReponse();
        } else if (intResponse==4) {
            avgMarketValueResponse();
        } else if (intResponse==5) {
            totalLiveableAreaResponse();
        } else if (intResponse==6) {
            totalMarketValueResponse();
        } else {
            additionalFeature();
        }

    }

    public void vacPerCapitaReponse() {
        System.out.println("Do you want to review the full or partial vaccination status? - please respond with 'full' or 'partial:");
        System.out.flush();
        System.out.println("> "); System.out.flush();
        Scanner sc = new Scanner(System.in);
        String vaccinationResponse = sc.nextLine();
        vaccinationResponse= vaccinationResponse.toLowerCase();
        if (!vaccinationResponse.equals("partial") || !vaccinationResponse.equals("full")) {
            System.out.println("Please enter a valid response"); System.out.flush();
            vacPerCapitaReponse();
        }

        System.out.println("For which day do you want to review the vaccination status? - please respond with a date in the format of YYYY-MM-DD");
        System.out.flush();
        System.out.println("> "); System.out.flush();
        String dateResponse = sc.nextLine();
        Pattern p1 = Pattern.compile("\\d\\d\\d-\\d\\d-\\d\\d");
        Matcher m1 = p1.matcher(dateResponse);
        boolean b1 = m1.find();
        if (b1==false) {
            System.out.println("Please enter a valid response"); System.out.flush();
            vacPerCapitaReponse();
        }
        Map <String, Double> PerCapitaDateMap  = new TreeMap<String, Double>();

        Map <String, Double> PerCapitaMap  = new TreeMap<String, Double>(); //this comes from the processor
        for (Map.Entry<String, Double> entry : PerCapitaMap.entrySet()) {
            //if (date = what is given, then add){
            PerCapitaDateMap.put(entry.getKey(), entry.getValue());
            //}
        }
        System.out.println("BEGIN OUTPUT");
        if (PerCapitaDateMap== null) {
            System.out.println("0");
        } else {
            for (Map.Entry<String, Double> entry : PerCapitaDateMap.entrySet()) {
                System.out.println(entry.getKey()+" "+entry.getValue()); System.out.flush();
            }
        }
        System.out.println("END OUTPUT");
    }

    public void avgMarketValueResponse() {
        int avgMarketValueResponse=0;
        System.out.println("Please enter a 5 digit zip code?");
        System.out.flush();
        System.out.println("> "); System.out.flush();
        Scanner sc = new Scanner(System.in);
        String zipResponse = sc.nextLine();

        Pattern p1 = Pattern.compile("\\d\\d\\d\\d");
        Matcher m1 = p1.matcher(zipResponse);
        boolean b1 = m1.find();
        if (b1==false) {
            System.out.println("Please enter a valid response"); System.out.flush();
            avgMarketValueResponse();
        }
        Map <String, Integer> zipAvgMarketValue  = new TreeMap<String, Integer>();

        for (Map.Entry<String, Integer> entry : zipAvgMarketValue.entrySet()) {
            //if (date = what is given, then add){
            if (entry.getKey() == zipResponse) {
                avgMarketValueResponse = entry.getValue();
            }
            //}
        }
        System.out.println("BEGIN OUTPUT");
        System.out.println(avgMarketValueResponse); System.out.flush();
        System.out.println("END OUTPUT");
    }

    public void totalLiveableAreaResponse() {
        int totalLiveableAreaResponse=0;
        System.out.println("Please enter a 5 digit zip code?");
        System.out.flush();
        System.out.println("> "); System.out.flush();
        Scanner sc = new Scanner(System.in);
        String zipResponse = sc.nextLine();

        Pattern p1 = Pattern.compile("\\d\\d\\d\\d");
        Matcher m1 = p1.matcher(zipResponse);
        boolean b1 = m1.find();
        if (b1==false) {
            System.out.println("Please enter a valid response"); System.out.flush();
            totalLiveableAreaResponse();
        }
        Map <String, Integer> zipTotalLiveableArea  = new TreeMap<String, Integer>();

        for (Map.Entry<String, Integer> entry : zipTotalLiveableArea.entrySet()) {
            if (entry.getKey() == zipResponse) {
                totalLiveableAreaResponse = entry.getValue();
            }
            //}
        }
        System.out.println("BEGIN OUTPUT");
        System.out.println(totalLiveableAreaResponse); System.out.flush();
        System.out.println("END OUTPUT");
    }

    public void totalMarketValueResponse() {
        int totalMarketValueResponse=0;
        System.out.println("Please enter a 5 digit zip code?");
        System.out.flush();
        System.out.println("> "); System.out.flush();
        Scanner sc = new Scanner(System.in);
        String zipResponse = sc.nextLine();

        Pattern p1 = Pattern.compile("\\d\\d\\d\\d");
        Matcher m1 = p1.matcher(zipResponse);
        boolean b1 = m1.find();
        if (b1==false) {
            System.out.println("Please enter a valid response"); System.out.flush();
            totalLiveableAreaResponse();
        }
        Map <String, Integer> zipTotalMarketValue  = new TreeMap<String, Integer>();

        for (Map.Entry<String, Integer> entry : zipTotalMarketValue.entrySet()) {
            if (entry.getKey() == zipResponse) {
                totalMarketValueResponse = entry.getValue();
            }
            //}
        }
        System.out.println("BEGIN OUTPUT");
        System.out.println(totalMarketValueResponse); System.out.flush();
        System.out.println("END OUTPUT");
    }

    public void additionalFeature() {
        System.out.println("Do you want to know the highest market value per capita zip code covid rate or highest market value per capita covid rate?- please enter 'highest' or 'lowest'" );
        System.out.flush();
        System.out.println("> "); System.out.flush();
        Scanner sc = new Scanner(System.in);
        String zipResponse = sc.nextLine();
        zipResponse= zipResponse.toLowerCase();
        if (!zipResponse.equals("highest") || !zipResponse.equals("lowest")) {
            System.out.println("Please enter a valid response"); System.out.flush();
            additionalFeature();
        }
        double valueResponse = 0.0;
        if (zipResponse.equals("highest") ) {
            //valueResponse = processor.getHighestCovidRate()
        } else {
            //valueResponse = processor.getLowestCovidRate()
        }
        System.out.println("BEGIN OUTPUT");
        System.out.println(valueResponse); System.out.flush();
        System.out.println("END OUTPUT");
    }


}
