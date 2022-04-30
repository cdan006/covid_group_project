package edu.upenn.cit594.util;

public class MarketValueFetcher extends RealEstateAverageableAttributeFetcher {

    public MarketValueFetcher() {
        super();
    }


    public double fetchAttribute(Property p) {
        double output = 0;
        try {
            output = Double.parseDouble(p.getMarketValue());
            return output;
        } catch (NumberFormatException e) {
            output = 0;
        }

        return output;
    }

}
