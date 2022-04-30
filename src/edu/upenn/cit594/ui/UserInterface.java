package edu.upenn.cit594.ui;

import edu.upenn.cit594.datamanagement.ArgumentReader;
import edu.upenn.cit594.datamanagement.CovidReader;
import edu.upenn.cit594.datamanagement.PopulationReader;
import edu.upenn.cit594.datamanagement.PropertyReader;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.Processor;
import edu.upenn.cit594.util.Covid;
import edu.upenn.cit594.util.Population;
import edu.upenn.cit594.util.Property;
import groovy.io.LineColumnReader;

import java.sql.Array;
import java.util.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInterface {
    protected Processor processor;
    protected Logger logger;
    protected String logFile;
    //protected CovidReader covidReader;
    //protected PopulationReader populationReader;
    //protected PropertyReader propertyReader;
    //protected ArgumentReader arugmentReader;

    public UserInterface (Processor processor, Logger logger) throws IOException {
        this.processor = processor;
        this.logger = logger;
        this.logFile = logger.getLogFile();;
    }


    public void run() throws IOException {
        Scanner sc = new Scanner(System.in);
        int intResponse = display(sc);
        while (intResponse !=0) {
            System.out.flush();
            intResponse = display(sc);
            System.out.flush();
        }
        sc.close();
    }

    public int possibleActions(Scanner sc) throws IOException { //this is where we capture the user's input, 1, 2, 3, etc.
        System.out.println("Select the action you want to perform\n"+
                "0. Exit the program.\n"+
                "1. Show the available data sets (subsection 3.1).\n"+
                "2. Show the total population for all ZIP Codes (subsection 3.2).\n"+
                "3. Show the total vaccinations per capita for each ZIP Code for the specified date (subsection 3.3).\n"+
                "4. Show the average market value for properties in a specified ZIP Code (subsection 3.4).\n"+
                "5. Show the average total livable area for properties in a specified ZIP Code (subsection 3.5).\n"+
                "6. Show the total market value of properties, per capita, for a specified ZIP Code (subsection 3.6).\n"+
                "7. Show the highest/ lowest market value per capita zip code covid rate (subsection 3.7).");
        int intResponse = 0;
        System.out.flush();
        System.out.println("> ");
        String response = sc.nextLine();
        if (logFile==null) {
            System.err.println("No Logger File");
        } else {
            Logger l = Logger.getInstance();
            l.log(System.currentTimeMillis() +" "+ response+"\n");
        }
        try {
            intResponse = Integer.parseInt(response);
            if (intResponse>7 || intResponse<0) {
                System.out.flush();
                System.out.println("Please enter a valid integer (0-7) response\n");
                display(sc);
            }

        } catch (Exception e) {
            System.out.flush();
            System.out.println("Please enter a valid integer (0-7) response\n");
            possibleActions(sc);
        }
        return intResponse;
    }

    public int display(Scanner sc) throws IOException { //show the output of the 1-7
        int intResponse = possibleActions(sc);
        if (intResponse==0) {
            return intResponse;
        }
        else if (intResponse==1) {
            ArrayList<String> dataSets = processor.availableDataSets();
            System.out.println("BEGIN OUTPUT"); System.out.flush();
            System.out.println(dataSets); System.out.flush();
            System.out.println("END OUTPUT"); System.out.flush();
        } else if (intResponse==2) {
            if (processor.getPopulationFileName() == null) {
                System.out.println("The population file is not available"); System.out.flush();
            }
            int totalPopulation = processor.totalPopulation(processor.getPopulationList()); System.out.flush();
            if (totalPopulation==-1) {
                System.out.println("No population data provided"); System.out.flush();
            }
            System.out.println("BEGIN OUTPUT"); System.out.flush();
            System.out.println(totalPopulation); System.out.flush();
            System.out.println("END OUTPUT"); System.out.flush();
        } else if (intResponse==3) {
            vacPerCapitaReponse(sc);

        } else if (intResponse==4) {
            avgMarketValueResponse(sc);
        } else if (intResponse==5) {
            totalLiveableAreaResponse(sc);
        } else if (intResponse==6) {
            totalMarketValueResponse(sc);
        } else if (intResponse==7){
            additionalFeature(sc);
        }
        return intResponse;
    }

    public void vacPerCapitaReponse(Scanner sc) throws IOException {
        if (processor.getPopulationFileName() == null) {
            System.out.println("The population file is not available"); System.out.flush();
            display(sc);
        }
        if (processor.getCovidFileName() == null) {
            System.out.println("The Covid file is not available"); System.out.flush();
            display(sc);
        }

        System.out.println("Do you want to review the full or partial vaccination status? - please respond with 'full' or 'partial:");
        System.out.flush();
        System.out.println("> "); System.out.flush();
        //Scanner sc = new Scanner(System.in);
        String vaccinationResponse = sc.nextLine();
        vaccinationResponse= vaccinationResponse.toLowerCase();

        System.out.println("For which day do you want to review the vaccination status? - please respond with a date in the format of YYYY-MM-DD");
        System.out.flush();
        System.out.println("> "); System.out.flush();
        String dateResponse = sc.nextLine();
        if (logFile==null) {
            System.err.println("No Logger File");
        } else {
            Logger l = Logger.getInstance();
            l.log(System.currentTimeMillis() + " " + vaccinationResponse + "\n");
            l.log(System.currentTimeMillis() + " " + dateResponse + "\n");
        }
        Pattern p1 = Pattern.compile("\\d\\d\\d-\\d\\d-\\d\\d");
        Matcher m1 = p1.matcher(dateResponse);
        boolean b1 = m1.find();
        if (b1==false) {
            System.out.println("Please enter a valid response"); System.out.flush();
            display(sc);
        }
        ArrayList<String> PerCapitaDate = processor.vacPerCapita(vaccinationResponse, dateResponse, processor.getCovidList(),
                processor.getPopulationList());
        System.out.println("BEGIN OUTPUT");
        if (PerCapitaDate== null) {
            System.out.println("0");
        } else {
            for (int i = 0; i < PerCapitaDate.size(); i++) {
                System.out.print(PerCapitaDate.get(i)+"\n");
            }
        }
        System.out.println("END OUTPUT");
    }

    public void avgMarketValueResponse(Scanner sc) throws IOException {
        if (processor.getPropertyFileName() == null) {
            System.out.println("The properties file is not available"); System.out.flush();
            display(sc);
        }

        int avgMarketValue=0;
        System.out.println("Please enter a 5 digit zip code?");
        System.out.flush();
        System.out.println("> "); System.out.flush();
        String zipResponse = sc.nextLine();
        if (logFile==null) {
            System.err.println("No Logger File");
        } else {
            Logger l = Logger.getInstance();
            l.log(System.currentTimeMillis() + " " + zipResponse + "\n");
        }
        Pattern p1 = Pattern.compile("\\d\\d\\d\\d");
        Matcher m1 = p1.matcher(zipResponse);
        boolean b1 = m1.find();
        if (b1==false) {
            System.out.println("Please enter a valid response"); System.out.flush();
            avgMarketValueResponse(sc);
        }
        avgMarketValue = processor.avgMarketValue(zipResponse, processor.getPropertyList());
        System.out.println("BEGIN OUTPUT");
        System.out.println(avgMarketValue); System.out.flush();
        System.out.println("END OUTPUT");
    }

    public void totalLiveableAreaResponse(Scanner sc) throws IOException {
        if (processor.getPropertyFileName() == null) {
            System.out.println("The property file is not available"); System.out.flush();
            display(sc);
        }

        int totalLiveableAreaResponse=0;
        System.out.println("Please enter a 5 digit zip code?");
        System.out.flush();
        System.out.println("> "); System.out.flush();
        String zipResponse = sc.nextLine();
        if (logFile==null) {
            System.err.println("No Logger File");
        } else {
            Logger l = Logger.getInstance();
            l.log(System.currentTimeMillis() + " " + zipResponse + "\n");
        }
        Pattern p1 = Pattern.compile("\\d\\d\\d\\d");
        Matcher m1 = p1.matcher(zipResponse);
        boolean b1 = m1.find();
        if (b1==false) {
            System.out.println("Please enter a valid response"); System.out.flush();
            totalLiveableAreaResponse(sc);
        }
        totalLiveableAreaResponse = processor.avgLivableArea(zipResponse, processor.getPropertyList());
        System.out.println("BEGIN OUTPUT");
        System.out.println(totalLiveableAreaResponse); System.out.flush();
        System.out.println("END OUTPUT");
    }

    public void totalMarketValueResponse(Scanner sc) throws IOException {
        if (processor.getPopulationFileName() == null) {
            System.out.println("The population file is not available"); System.out.flush();
            display(sc);
        }
        if (processor.getPropertyFileName() == null) {
            System.out.println("The property file is not available"); System.out.flush();
            display(sc);
        }
        int totalMarketValueResponse=0;
        System.out.println("Please enter a 5 digit zip code?");
        System.out.flush();
        System.out.println("> "); System.out.flush();
        String zipResponse = sc.nextLine();
        if (logFile==null) {
            System.err.println("No Logger File");
        } else {
            Logger l = Logger.getInstance();
            l.log(System.currentTimeMillis() + " " + zipResponse + "\n");
        }
        Pattern p1 = Pattern.compile("\\d\\d\\d\\d");
        Matcher m1 = p1.matcher(zipResponse);
        boolean b1 = m1.find();
        if (b1==false) {
            System.out.println("Please enter a valid response"); System.out.flush();
            totalLiveableAreaResponse(sc);
        }
        totalMarketValueResponse = processor.totalMarketPerCapita(zipResponse, processor.getPropertyList(), processor.getPopulationList());
        System.out.println("BEGIN OUTPUT");
        System.out.println(totalMarketValueResponse); System.out.flush();
        System.out.println("END OUTPUT");
    }

    public void additionalFeature(Scanner sc) throws IOException {
        String addFeatureResponse = null;
        System.out.println("Do you want to know the highest market value per capita zip code covid rate or lowest market value per capita covid rate?- please enter 'highest' or 'lowest'" );
        System.out.flush();
        System.out.println("> "); System.out.flush();
        String zipResponse = sc.nextLine();
        zipResponse= zipResponse.toLowerCase();

        if (!zipResponse.equals("highest") && !zipResponse.equals("lowest")) {
            System.out.println("Please enter a valid response"); System.out.flush();
            additionalFeature(sc);
        }

        System.out.println("For which day do you want to review the vaccination status? - please respond with a date in the format of YYYY-MM-DD");
        System.out.flush();
        System.out.println("> "); System.out.flush();
        String dateResponse = sc.nextLine();
        if (logFile==null) {
            System.err.println("No Logger File");
        } else {
            Logger l = Logger.getInstance();
            l.log(System.currentTimeMillis() + " " + zipResponse + "\n");
            l.log(System.currentTimeMillis() +" "+ dateResponse+"\n");
        }
        Pattern p1 = Pattern.compile("\\d\\d\\d-\\d\\d-\\d\\d");
        Matcher m1 = p1.matcher(dateResponse);
        boolean b1 = m1.find();
        if (b1==false) {
            System.out.println("Please enter a valid response"); System.out.flush();
            additionalFeature(sc);
        }


        if (zipResponse.equals("highest") ) {
            try {
                String[] highestResponse =  processor.extremesCovidRate(processor.getPopulationList(),processor.getCovidList(),processor.getPropertyList(),dateResponse);
                addFeatureResponse = highestResponse[0];
                if (addFeatureResponse==null){
                    addFeatureResponse="0";
                }
            }
            catch (Exception e) {
                System.out.flush();
                System.out.println("No vaccines on that date\n");System.out.flush();
                display(sc);
            }
        } else {
            try {
                String [] lowestResponse =  processor.extremesCovidRate(processor.getPopulationList(),processor.getCovidList(),processor.getPropertyList(),dateResponse);
                addFeatureResponse = lowestResponse[1];
                if (addFeatureResponse==null){
                    addFeatureResponse="0";
                }
            } catch (Exception e) {
                System.out.flush();
                System.out.println("No vaccines on that date\n");System.out.flush();
                display(sc);
            }
        }
        System.out.println("BEGIN OUTPUT");
        System.out.println(addFeatureResponse); System.out.flush();
        System.out.println("END OUTPUT");
    }

}

