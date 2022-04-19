package edu.upenn.cit594.ui;

import edu.upenn.cit594.datamanagement.CovidReader;
import edu.upenn.cit594.datamanagement.PopulationReader;
import edu.upenn.cit594.datamanagement.PropertyReader;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.Processor;

import java.io.IOException;
import java.util.Scanner;

public class UserInterface {
    protected Processor processor;
    protected Scanner in;
    protected Logger logger;

    public UserInterface (Processor processor, Logger logger) throws IOException {
        this.processor = processor;
        this.logger = logger;
    }

    public void possibleActions() { //this is where we capture the user's input, 1, 2, 3, etc.
    }

    public int errorMessage() { //return an integer if the arguments are incorrect
        return 0;
    }

    public int display() { //show the output of the 1-7
        return 0;
    }



}
