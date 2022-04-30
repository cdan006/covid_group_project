package edu.upenn.cit594.util;

import java.sql.Timestamp;

public class Property {
    String marketValue;
    String totalLiveableArea;
    String zip;

    public Property (String marketValue, String totalLiveableArea, String zip) {
        if (marketValue== null) {
            this.marketValue = marketValue;
        } else {
            this.marketValue = marketValue;
        }
        if (totalLiveableArea== null) {
            this.totalLiveableArea = totalLiveableArea;
        } else {
            this.totalLiveableArea = totalLiveableArea;
        }
        if (zip== null) {
            this.zip = zip;
        } else {
            this.zip = zip.replaceAll(" ","");
        }


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
