package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.Population;
import edu.upenn.cit594.util.Property;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.lang.*;
import java.util.Arrays;

public class PropertyReader {
    protected String filename;
    public PropertyReader(String name) {
        filename = name;
    }

    public ArrayList<Property> getPropertyList() throws IOException {
        ArrayList <String []> propertyArray = getPropertyArray();
        ArrayList <Property> propertyList = new ArrayList<>();
        int c = 0;
        String marketValue = null;
        String totalLiveableArea = null;
        String zip = null;
        int marketValueIndex = -1;
        int totalLiveableAreaIndex = -1;
        int zipIndex = -1;

        for (int r = 0; r < propertyArray.size(); r++) {
            if (r != 0) {
                try {marketValue = propertyArray.get(r)[marketValueIndex];} catch (Exception e) {marketValue = null;}
                try { totalLiveableArea = propertyArray.get(r)[totalLiveableAreaIndex];} catch (Exception e) {totalLiveableArea = null;}
                try { zip = propertyArray.get(r)[zipIndex].substring(0,5);} catch (Exception e) {zip = null;}
                Property property = new Property (marketValue, totalLiveableArea, zip);
                propertyList.add(property);
            } else {
                for (c = 0; c < propertyArray.get(r).length; c++) {
                    String print = propertyArray.get(r)[c];
                    if (propertyArray.get(r)[c] != null) {
                        if (propertyArray.get(r)[c].equals("market_value"))  {
                            marketValueIndex = c;
                        }
                        if (propertyArray.get(r)[c].equals("total_livable_area"))  {
                            totalLiveableAreaIndex = c;
                        }
                        if (propertyArray.get(r)[c].equals( "zip_code"))  {
                            zipIndex = c;
                        }
                    }
                    if (marketValueIndex>=0 && totalLiveableAreaIndex>= 0 && zipIndex>=0) {
                        break;
                    }

                }
            }

        }
        return propertyList;
    }




    public ArrayList<String[]> getPropertyArray() throws IOException {
        File file = new File(filename);
        String state = null;
        ArrayList <String[]> propertyArray = new ArrayList<String[]>();
        String [][] elementArray = new String[600000][100];
        int c = 0;
        int r = 0;
        char prior = '\0';
        char current = '\0';
        String element = null;
        int content=0;
        int priorContent = 0;
        try (FileReader fr = new FileReader(file)) {
            while ((content = fr.read()) != -1) {
                current = (char) content;
                if (prior=='\0') {
                    state= null;
                } else if (state ==null) {
                    if (priorContent ==44) {
                        c++;
                        state = "new field";
                    }
                    else if (priorContent ==34) {
                        state ="open quote";
                    }
                    else if (priorContent ==10) {
                        elementArray = null;
                        propertyArray.add(elementArray[r]);
                        state = "new field";
                        r++;
                    } else {
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }
                        state = "add";
                    }
                } else if (state == "new field") {
                    if (priorContent == 44) {
                        c++;
                        state = "new field";
                    } else if (priorContent == 34) {
                        state = "open quote";
                    } else if (priorContent == 10) {
                        propertyArray.add(elementArray[r]);
                        c = 0;
                        state = "new field";
                        r++;
                    } else {
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }
                        state = "add";
                    }
                } else if (state == "new field/ open quote") {
                    if (priorContent ==44) {
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }
                        state = "open quote";
                    } else if (priorContent ==34 && content == 34) {
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }
                        //prior = current;
                        //priorContent = content;
                        state = "open quote";
                    } else if (priorContent ==34 && content != 34) {
                        state = "close quote";
                    }  else if (priorContent ==10) {
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }
                        state = "open quote";
                    } else {
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }
                        state = "open quote";
                    }

                } else if (state == "open quote") {
                    if (priorContent ==44) {
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }
                        state = "open quote";
                    } else if (priorContent ==34 && (content == 34)) {//|| content == 10 || content == 44
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }
                        prior = current;
                        priorContent = content;
                        content = fr.read();
                        current = (char) content;
                        state = "open quote";
                    } else if (priorContent ==34){
                        state = "close quote";
                    } else if (priorContent ==10) {
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }
                        state = "open quote";
                    } else {
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }
                        state = "open quote";
                    }


                } else if (state == "close quote") {
                   if (priorContent == 44) {
                       elementArray[r][c] = element;
                       element = null;
                       state = "new field";
                       c++;
                   } else if (priorContent == 10) {
                       elementArray[r][c] = element;
                       propertyArray.add(elementArray[r]);
                       state = "new field";
                       c = 0;
                       r++;
                   }
                } else if (state == "add") {
                    if (priorContent == 44) {
                        elementArray[r][c] = element;
                        element = null;
                        state = "new field";
                        c++;
                    } else if (priorContent == 10) {
                        elementArray[r][c] = element;
                        propertyArray.add(elementArray[r]);
                        element = null;
                        state = "new field";
                        c = 0;
                        r++;
                    } else {
                        String s = String.valueOf(prior);
                        if (element == null) {
                            element = s;
                        } else {
                            element = element + s;
                        }
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
            elementArray[r][c] = element;
            propertyArray.add(elementArray[r]);
        }

        return propertyArray;
    }

    public String getFileName() {
        return filename;
    }

    /*
    public String state(int twoPrior, int prior, int current, String state) {
        if (state == null) {
            return "new field";
        } else if () {

        }


        if (prior ==0){
            return "start";
        }
        else if (twoPrior == 34 && prior==34 && current==34) {
            return "add";
        } else if (twoPrior == 34 && prior==34 && current==34) {
            return "add";
        }
        else if (prior == 44 && state!= "escape") {
            return "new_field";
        } else if (prior == 10 && state!= "escape") {
            return "new_row";
        } else if (current == 10 && state== "escape") {
            return "add";
        } else {
            return "add";
        }

        return state;
    }

    else if (state ==null && priorContent ==44) {//44 = ,
                    c++;
                    state = "new field";
                } else if (state ==null && priorContent ==34){//34 = "
                    state ="open quote";
                } else if (state ==null && priorContent ==10){//10 = LF
                    r++;
                    state = "new field";
                } else if (state ==null) {
                    String s = String.valueOf(prior);
                    element = s;
                    state = "add";
                } else if (state == "new field" && priorContent ==44) {
                    c++;
                    state = "new field";
                } else if (state == "new field" && priorContent ==34) {
                    state ="open quote";
                } else if (state == "new field" && priorContent ==10) {
                    r++;
                    c=0;
                    state = "new field";
                } else if (state == "new field") {
                    String s = String.valueOf(prior);
                    element = s;
                    state = "add";
                } else if (state == "new field/ open quote" && priorContent ==44) {
                    String s = String.valueOf(prior);
                    element = s;
                    state = "open quote";
                } else if (state == "new field/ open quote" && priorContent ==34 && content == 34) {
                    String s = String.valueOf(prior);
                    element = s;
                    prior = current;
                    priorContent = content;
                    state = "open quote";
                } else if (state == "new field/ open quote" && priorContent ==34 && content != 34) {
                    state = "close quote";
                }else if (state == "new field/ open quote" && priorContent ==10) {
                    String s = String.valueOf(prior);
                    element = s;
                    state = "open quote";
                } else if (state == "new field/ open quote") {
                    String s = String.valueOf(prior);
                    element = s;
                    state = "open quote";
                }
                else if (state == "open quote" && priorContent ==44) {
                    String s = String.valueOf(prior);
                    element = s;
                    state = "open quote";
                } else if (state == "open quote" && priorContent ==34 && content == 34) {
                    String s = String.valueOf(prior);
                    element = s;
                    prior = current;
                    priorContent = content;
                    state = "open quote";
                } else if (state == "open quote" && priorContent ==34 && content != 34) {
                    state = "close quote";
                } else if (state == "open quote" && priorContent ==10) {
                    String s = String.valueOf(prior);
                    element = s;
                    state = "close quote";
                } else if (state == "open quote") {
                    String s = String.valueOf(prior);
                    element = s;
                    state = "open quote";
                } else if (state == "close quote" && priorContent == 44) {
                    propertyArray[r][c] = element;
                    element = null;
                    state = "new field";
                    c++;
                } else if (state == "close quote" && priorContent == 10) {
                    propertyArray[r][c] = element;
                    element = null;
                    state = "new field";
                    r++;
                    c = 0;
                } else if (state == "add" && priorContent == 44) {
                    propertyArray[r][c] = element; // check how to update the matrix of a 2x2 array
                    element = null;
                    state = "new field";
                    c++;
                } else if (state == "add" && priorContent == 10) {
                    propertyArray[r][c] = element;
                    element = null;
                    state = "new field";
                    r++;
                    c = 0;
                } else if (state == "add") {
                    String s = String.valueOf(prior);
                    element = element+s;
                    state = "add";

     public String [][] getPropertyArray() throws IOException {
        File file = new File(filename);
        String state = null;
        String[][] propertyArray = new String[100][];
        int r = 0;
        int c = 0;
        char prior = '\0';
        char current = '\0';
        String element = null;
        try (FileReader fr = new FileReader(file)) {
            int content;
            int priorContent = 0;
            while ((content = fr.read()) != -1) {
                current = (char) content;
                if (prior=='\0') {
                    state= null;
                } else if (state ==null) {
                    if (priorContent ==44) {
                        c++;
                        state = "new field";
                    }
                    else if (priorContent ==34) {
                        state ="open quote";
                    }
                    else if (priorContent ==10) {
                        r++;
                        state = "new field";
                    } else {
                        String s = String.valueOf(prior);
                        element = s;
                        state = "add";
                    }
                } else if (state == "new field") {
                    if (priorContent == 44) {
                        c++;
                        state = "new field";
                    } else if (priorContent == 34) {
                        state = "open quote";
                    } else if (priorContent == 10) {
                        r++;
                        c = 0;
                        state = "new field";
                    } else {
                        String s = String.valueOf(prior);
                        element = s;
                        state = "add";
                    }
                } else if (state == "new field/ open quote") {
                    if (priorContent ==44) {
                        String s = String.valueOf(prior);
                        element = s;
                        state = "open quote";
                    } else if (priorContent ==34 && content == 34) {
                        String s = String.valueOf(prior);
                        element = s;
                        prior = current;
                        priorContent = content;
                        state = "open quote";
                    } else if (priorContent ==34 && content != 34) {
                        state = "close quote";
                    }  else if (priorContent ==10) {
                        String s = String.valueOf(prior);
                        element = s;
                        state = "open quote";
                    } else {
                        String s = String.valueOf(prior);
                        element = s;
                        state = "open quote";
                    }

                } else if (state == "open quote") {
                    if (priorContent ==44) {
                        String s = String.valueOf(prior);
                        element = s;
                        state = "open quote";
                    } else if (priorContent ==34 && content == 34) {
                        String s = String.valueOf(prior);
                        element = s;
                        prior = current;
                        priorContent = content;
                        state = "open quote";
                } else if (priorContent ==34 && content != 34){
                        state = "close quote";
                    } else if (priorContent ==10) {
                        String s = String.valueOf(prior);
                        element = s;
                        state = "close quote";
                    } else {
                        String s = String.valueOf(prior);
                        element = s;
                        state = "open quote";
                    }


                } else if (state == "close quote") {
                   if (priorContent == 44) {
                       propertyArray[r][c] = element;
                       element = null;
                       state = "new field";
                       c++;
                   } else if (priorContent == 10) {
                       propertyArray[r][c] = element;
                       element = null;
                       state = "new field";
                       r++;
                       c = 0;
                   }
                } else if (state == "add") {
                    if (priorContent == 44) {
                        propertyArray[r][c] = element; // check how to update the matrix of a 2x2 array
                        element = null;
                        state = "new field";
                        c++;
                    } else if (priorContent == 10) {
                        propertyArray[r][c] = element;
                        element = null;
                        state = "new field";
                        r++;
                        c = 0;
                    } else {
                        String s = String.valueOf(prior);
                        element = element+s;
                        state = "add";
                    }


                }
                prior = current;
                priorContent = content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propertyArray;
    }

     */

}

