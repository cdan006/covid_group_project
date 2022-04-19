package edu.upenn.cit594.util;

import java.sql.Timestamp;

public class Covid {

    int zip;
    Timestamp time;
    int partialVac;
    int fullyVac;
    int boosters;

    public Covid (int zip, Timestamp time, int partialVac, int fullyVac, int boosters) {
        this.zip = zip;
        this.time = time;
        this.partialVac = partialVac;
        this.fullyVac = fullyVac;
        this.boosters = boosters;
    }


}
