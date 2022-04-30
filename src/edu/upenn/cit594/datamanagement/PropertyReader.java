package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.Population;
import edu.upenn.cit594.util.Property;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.lang.*;
import java.util.Arrays;
import java.util.Iterator;

public class PropertyReader {
    protected String filename;
    protected String logFile;
    public PropertyReader(String name, Logger LogName) {
        filename = name;
        this.logFile =LogName.getLogFile();
    }

    public ArrayList<Property> getPropertyList() throws IOException {
        ArrayList <ArrayList<String>> propertyArray = getPropertyArray(); //ArrayList <String []> propertyArray = getPropertyArray();
        ArrayList <Property> propertyList = new ArrayList<>();
        int j = 0;
        String marketValue = null;
        String totalLiveableArea = null;
        String zip = null;
        int marketValueIndex = -1;
        int totalLiveableAreaIndex = -1;
        int zipIndex = -1;
        //TRY CATCH TO IGNORE ANY VALUES THAT GIVE IT AN ERROR

        for (ArrayList<String> outerArray : propertyArray) {
            if (j == 0) {
                int a = 0;
            for (String innerArray : outerArray) {
                        if (innerArray != null) {
                            if (innerArray.equals("market_value"))  {
                                marketValueIndex = a;
                            }
                            if (innerArray.equals("total_livable_area"))  {
                                totalLiveableAreaIndex = a;
                            }
                            if (innerArray.equals( "zip_code"))  {
                                zipIndex = a;
                            }
                        }
                        if (marketValueIndex>=0 && totalLiveableAreaIndex>= 0 && zipIndex>=0) {
                            break;
                        }
                        a++;
                }
                j++;
            }
            else {
                try {marketValue = outerArray.get(marketValueIndex);} catch (Exception e) {marketValue = null;}
                try { totalLiveableArea = outerArray.get(totalLiveableAreaIndex);} catch (Exception e) {totalLiveableArea = null;}
                try { zip = outerArray.get(zipIndex);} catch (Exception e) {zip = null;}
                Property property = new Property (marketValue, totalLiveableArea, zip);
                propertyList.add(property);
            }

        }

        return propertyList;
    }



    //RETURN AN ARRAY LIST WITH THE PROPERTY DATA FROM THE PROVIDED CSV FILE
    public ArrayList<ArrayList<String>> getPropertyArray() throws IOException {
        File file = new File(filename);
        String state = null;
        ArrayList <String[]> propertyArray = new ArrayList<String[]>();
        ArrayList <ArrayList<String>> propertyArray2 = new ArrayList<ArrayList<String>>();
        //String [][] elementArray = new String[600000][100];
        //int c = 0;
        //int r = 0;
        char prior = '\0';
        char current = '\0';
        //String element = null;
        int content=0;
        int priorContent = 0;
        StringBuilder element2 = new StringBuilder();
        ArrayList <String> Row = new ArrayList<String>();
        try (BufferedReader fr = new BufferedReader(new FileReader (file))) {
            if (logFile==null) {
                System.err.println("No Logger File");
            } else {
                Logger l = Logger.getInstance();
                l.setLogFile(logFile);
                l.log(System.currentTimeMillis() + " " + filename + "\n");
            }
            //READ THE CONTENTS OF THE FILE BYTE BY BYTE AND CONVERT IT TO CHAR
            while ((content = fr.read()) != -1) {
                current = (char) content;
                //IF THE PRIOR VALUE IS NULL, SET THE STATE AS NULL
                if (prior=='\0') {
                    state= null;
                } else if (state ==null) { //IF THE PRIOR STATE IS NULL, AND THE PRIOR CONTENT IS A ,: SET STATE TO NEW FIELD AND MOVE 1 COLUMN TO THE RIGHT
                    if (priorContent ==44) {
                        Row.add(null);
                        //c++;
                        state = "new field";
                    }
                    else if (priorContent ==34) { //IF THE PRIOR STATE IS NULL, AND THE PRIOR CONTENT IS A ": SET STATE TO OPEN QUOTE
                        state ="open quote";
                    }
                    else if (priorContent ==10) { //IF THE PRIOR STATE IS NULL, AND THE PRIOR CONTENT IS A NEW LINE: SET STATE TO NEW FIELD AND GO ONE ROW LOWER
                        Row.add(null);
                        propertyArray2.add(Row);
                        Row = new ArrayList<String>();
                        //elementArray = null;
                        //propertyArray.add(elementArray[r]);
                        state = "new field";
                        //r++;
                    } else { //IF THE PRIOR STATE IS NULL, CHANGE THE STATE TO ADD AND UPDATE THE ELEMENT STRING
                        element2.append(prior);
                        /*
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }

                         */
                        state = "add";
                    }
                } else if (state.equals("new field")) { //IF STATE IS NEW FIELD AND THE PRIOR VALUE IS A ,: MOVE 1 COLUMN TO THE RIGHT AND UPDATE STATE TO NEW FIELD
                    if (priorContent == 44) {
                        Row.add(null);
                        //c++;
                        state = "new field";
                    } else if (priorContent == 34) { //IF STATE IS NEW FIELD AND THE PRIOR VALUE IS A ": UPDATE STATE TO OPEN QUOTE
                        state = "open quote";
                    } else if (priorContent == 10) { //IF STATE IS NEW FIELD AND THE PRIOR VALUE IS A NEW LINE: MOVE 1 ROW LOWER AND UPDATE STATE TO NEW FIELD
                        //propertyArray.add(elementArray[r]);
                        propertyArray2.add(Row);
                        Row = new ArrayList<String>();
                        //c = 0;
                        state = "new field";
                        //r++;
                    } else { //IF STATE IS NEW FIELD, CHANGE THE STATE TO ADD AND UPDATE THE ELEMENT STRING
                        element2.append(prior);
                        /*
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }

                         */
                        state = "add";
                    }
                } else if (state.equals("new field/ open quote")) {//IF STATE IS NEW FIELD/OPEN QUOTE AND THE PRIOR VALUE IS A ,: UPDATE THE STATE TO OPEN QUOTE AND ADD TO THE ELEMENT STRING
                    if (priorContent ==44) {
                        element2.append(prior);
                        /*
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }

                         */
                        state = "open quote";
                    } else if (priorContent ==34 && content == 34) { //IF STATE IS NEW FIELD/OPEN QUOTE AND THE PRIOR VALUE/CURRENT IS A ": UPDATE THE STATE TO OPEN QUOTE AND ADD TO THE ELEMENT STRING
                        element2.append(prior);
                        /*
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }

                         */

                        //prior = current;
                        //priorContent = content;
                        state = "open quote";
                    } else if (priorContent ==34 && content != 34) { //IF STATE IS NEW FIELD/OPEN QUOTE AND THE PRIOR VALUE IS A ": UPDATE THE STATE TO CLOSED QUOTE
                        state = "close quote";
                    }  else if (priorContent ==10) { //IF STATE IS NEW FIELD/OPEN QUOTE AND THE PRIOR VALUE IS A NEW LINE: ADD THE ELEMENT TO THE STRING AND CHANGE THE STATE TO OPEN QUOTE
                        element2.append(prior);
                        String s = String.valueOf(prior);
                        /*
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }

                         */
                        state = "open quote";
                    } else {  //IF STATE IS OPEN QUOTE, UPDATE THE ELEMENT STRING
                        element2.append(prior);
                        /*
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }

                         */
                        state = "open quote";
                    }

                } else if (state.equals("open quote")) { // IF THE STATE IS OPEN QUOTE AND THE PRIOR VALUE IS A ,: ADD S TO ELEMENT
                    if (priorContent ==44) {
                        element2.append(prior);
                        /*
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }

                         */
                        state = "open quote";
                    } else if (priorContent ==34 && (content == 34)) {// IF THE STATE IS OPEN QUOTE AND THE PRIOR/CURRENT VALUE IS A ": ADD S TO ELEMENT AND SKIP TO THE NEXT CHAR VALUE
                        element2.append(prior);
                        /*
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }

                         */
                        prior = current;
                        priorContent = content;
                        content = fr.read();
                        current = (char) content;
                        state = "open quote";
                    } else if (priorContent ==34){
                        state = "close quote";
                    } else if (priorContent ==10) {
                        element2.append(prior);
                        /*
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }

                         */
                        state = "open quote";
                    } else { //IF STATE IS OPEN QUOTE, UPDATE THE ELEMENT STRING
                        element2.append(prior);
                        /*
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }

                         */
                        state = "open quote";
                    }


                } else if (state.equals("close quote")) { // IF THE STATE IS CLOSE QUOTE, UPDATE THE STATE AND ARRAY LIST AS APPROPRIATE
                    if (priorContent == 44) {
                        Row.add(String.valueOf(element2));
                        //elementArray[r][c] = element;
                        //element = null;
                        element2 = new StringBuilder();
                        state = "new field";
                        //c++;

                    } else if (priorContent == 10) {
                        //elementArray[r][c] = element;
                        //propertyArray.add(elementArray[r]);
                        //c = 0;
                        //r++;
                        state = "new field";
                        Row.add(String.valueOf(element2));
                        propertyArray2.add(Row);
                        Row = new ArrayList<String>();
                        //element = null;
                        element2 = new StringBuilder();

                    }
                } else if (state.equals("add")) {
                    if (priorContent == 44) {
                        Row.add(String.valueOf(element2));
                        //elementArray[r][c] = element;
                        //c++;
                        //element = null;
                        element2 = new StringBuilder();
                        state = "new field";

                    } else if (priorContent == 10) {
                        Row.add(String.valueOf(element2));
                        propertyArray2.add(Row);
                        Row = new ArrayList<String>();
                        //elementArray[r][c] = element;
                        //propertyArray.add(elementArray[r]);
                        //element = null;
                        element2 = new StringBuilder();
                        state = "new field";
                        //c = 0;
                        //r++;
                    } else {
                        element2.append(prior);
                        String s = String.valueOf(prior);
                        /*
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }

                         */
                        state = "add";
                    }


                }
                prior = current;
                priorContent = content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (content !=10) {
            //elementArray[r][c] = element;
            //propertyArray.add(elementArray[r]);
            Row.add(String.valueOf(element2));
            propertyArray2.add(Row);
            Row = new ArrayList<String>();
        }

        return propertyArray2;
    }

    public String getFileName() {
        return filename;
    }


}

