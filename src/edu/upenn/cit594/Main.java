package edu.upenn.cit594;

import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.Processor;
import edu.upenn.cit594.ui.UserInterface;
import edu.upenn.cit594.util.Covid;
import edu.upenn.cit594.util.Population;
import edu.upenn.cit594.util.Property;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {

        ArgumentReader argumentReader = new ArgumentReader();
        if (argumentReader.argumenthandling(args) == 0){
            System.out.println("Invalid Argument files");
            return;
        }
        CovidReader covidReader = null;
        //ArrayList<Covid> covidList = null;
        if (argumentReader.getCovidFile().endsWith(".csv")) {
            covidReader = new CovidCsv(argumentReader.getCovidFile());
            //covidList = covidReader.getCovidList();
        } else {
            covidReader = new CovidJson(argumentReader.getCovidFile());
            //covidList = covidReader.getCovidList();
        }


        PopulationReader populationReader = new PopulationReader(argumentReader.getPopulationFile());
        //ArrayList<Population> populationList = populationReader.getPopulationList();

        PropertyReader propertyReader = new PropertyReader(argumentReader.getPropertiesFile());
        //ArrayList<Property> propertyList = propertyReader.getPropertyList();

        Logger loggingFile = Logger.getInstance();
        //loggingFile.setLogFile(argumentReader.getLogFile());

        Processor processor = new Processor(argumentReader,covidReader,populationReader,propertyReader, loggingFile);

        UserInterface ui = new UserInterface(processor, loggingFile);
        ui.display();
    }

}
