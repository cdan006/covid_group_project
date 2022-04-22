package edu.upenn.cit594.datamanagement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;

public class ArgumentReader {
    protected String covidFile;
    protected String propertiesFile;
    protected String populationFile;
    protected String logFile;



    public int argumenthandling (String [] args) {
        if (args.length>0) {
            fileSelection(args[0]);
            if (fileRegex(args[0]) == false ) {
                return 0;
            }
        }
        if (args.length>1) {
            fileSelection(args[1]);
            if (fileRegex(args[1]) == false) {
                return 0;
            }
        }
        if (args.length>2) {
            fileSelection(args[2]);
            if (fileRegex(args[2]) == false) {
                return 0;
            }
        }
        if (args.length>3) {
            fileSelection(args[3]);
            if (fileRegex(args[3]) == false) {
                return 0;
            }
        }
        if (this.covidFile !=null) {
            if (fileExistOpen(this.covidFile) == false){
                return 0;
            }
        }
        if (this.propertiesFile !=null) {
            if (fileExistOpen(this.propertiesFile) == false){
                return 0;
            }
        }
        if (this.populationFile !=null) {
            if (fileExistOpen(this.populationFile) == false){
                return 0;
            }
        }
        if (this.logFile !=null) {
            if (fileExistOpen(this.logFile) == false){
                return 0;
            }
        }
        if ((covidFile ==  propertiesFile && covidFile!=null && propertiesFile!=null)||
                (covidFile == populationFile && covidFile!=null && populationFile!=null)||
                (covidFile == logFile && covidFile!=null && logFile!=null)||
                (propertiesFile == populationFile && propertiesFile!=null && populationFile!=null)||
                (propertiesFile == logFile && propertiesFile!=null && logFile!=null)||
                (populationFile == logFile && populationFile!=null && logFile!=null)||
        args.length>4 || (!covidFile.toLowerCase().endsWith(".json") && !covidFile.toLowerCase().endsWith(".csv") && covidFile!=null)
        ) {
            return 0;
        } else {
            return 1;
        }
    }

    public void fileSelection (String arg) {
        String[] argSplit = arg.substring(2).split("=");
        String Name = argSplit[0];
        String file = argSplit[1];
        if (Name.equals("covid")) {
            this.covidFile = file;
        } else if (Name.equals("properties")) {
            this.propertiesFile = file;
        } else if (Name.equals("population")) {
            this.populationFile = file;
        } else if (Name.equals("log")) {
            this.logFile = file;
        }
    }

    public boolean fileRegex (String file) {
        Pattern pRegex = Pattern.compile("^--(?<name>.+?)=(?<value>.+)$");
        Matcher mRegex = pRegex.matcher(file);
        boolean fileRegex = mRegex.find();
        return fileRegex;
    }

    public boolean fileExistOpen (String file) {
        File fileType = new File(file);
        if (fileType.exists() == false || fileType.canRead()==false) {
            return false;
        }
        return true;
    }

    public String getCovidFile() {
        return this.covidFile;
    }
    public String getPropertiesFile() {
        return this.propertiesFile;
    }
    public String getPopulationFile() {
        return this.populationFile;
    }
    public String getLogFile() {return this.logFile;}


}
  /*
        if (zeroName == "covid") {
            covidFile = args[0];
        } else if (zeroName == "properties") {
            propertiesFile = args[0];
        } else if (zeroName == "population") {
            populationFile = args[0];
        } else if (zeroName == "log") {
            logFile = args[0];
        } else {
            return 0;
        }

        Pattern pPropertiesRegex = Pattern.compile("^--(?<name>.+?)=(?<value>.+)$");
        Matcher mPropertiesRegex = pPropertiesRegex.matcher(propertiesFile);
        boolean propertiesRegex = mPropertiesRegex.find();

        Pattern pPopulationRegex = Pattern.compile("^--(?<name>.+?)=(?<value>.+)$");
        Matcher mPopulationRegex = pPopulationRegex.matcher(populationFile);
        boolean populationRegex = mPopulationRegex.find();

        Pattern plogFileRegex = Pattern.compile("^--(?<name>.+?)=(?<value>.+)$");
        Matcher mlogFileRegex = plogFileRegex.matcher(logFile);
        boolean logFileRegex = mlogFileRegex.find();

         */