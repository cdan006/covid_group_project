package edu.upenn.cit594.util;


public class Covid {

    String zip;
    String time;
    String date;
    String partialVac;
    String fullyVac;
    String boosters;

    public Covid (String zip, String time, String partialVac, String fullyVac, String boosters) {
        this.zip = zip;
        this.time = time.substring(1,11);
        this.date = time.substring(12,time.length()-1);
        this.partialVac = partialVac;
        this.fullyVac = fullyVac;
        this.boosters = boosters;
    }

    public String getZip() {
        return this.zip;
    }
    public String getTime() {
        return this.time;
    }
    public String getDate() {
        return this.date;
    }

    public String getPartialVac() {
        return this.partialVac;
    }

    public String getFullyVac() {
        return this.fullyVac;
    }

    public String getBoosters() {
        return this.boosters;
    }

}
