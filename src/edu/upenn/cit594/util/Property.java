package edu.upenn.cit594.util;

import java.sql.Timestamp;

public class Property {
    String marketValue;
    String totalLiveableArea;
    String zip;

    public Property (String marketValue, String totalLiveableArea, String zip) {
        this.marketValue = marketValue;
        this.totalLiveableArea = totalLiveableArea;
        this.zip = zip;

    }

    public String getMarketValue() {
        return marketValue;
    }

    public String getTotalLiveableArea() {
        return totalLiveableArea;
    }

    public String getZip() {
        return zip;
    }



}
