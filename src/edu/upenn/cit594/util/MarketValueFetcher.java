package edu.upenn.cit594.util;

public class MarketValueFetcher extends RealEstateAverageableAttributeFetcher {

    public MarketValueFetcher() {
        super();
    }


    public int fetchAttribute(Property p) {
        int output = 0;
        try {
            output = Integer.parseInt(p.getMarketValue());
            return output;
        } catch (NumberFormatException e) {
            output = 0;
        }

        return output;
    }

}
