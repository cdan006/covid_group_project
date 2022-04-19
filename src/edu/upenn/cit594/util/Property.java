package edu.upenn.cit594.util;

import java.sql.Timestamp;

public class Property {
    int marketValue;
    int totalLiveableArea;
    int zip;

    public Property (int marketValue, int totalLiveableArea, int zip) {
        this.marketValue = marketValue;
        this.totalLiveableArea = totalLiveableArea;
        this.zip = zip;

    }

}
