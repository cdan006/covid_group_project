package edu.upenn.cit594.util;

import static java.lang.Integer.parseInt;

public class AverageLivableAreaFetcher extends RealEstateAverageableAttributeFetcher {

    public AverageLivableAreaFetcher() {
        super();
    }


    public double fetchAttribute(Property p) {
        double output = 0;
        if (p.getTotalLiveableArea()==null) {
            return output;
        }
        try {
            output = Double.parseDouble(p.getTotalLiveableArea());
            return output;
        } catch (NumberFormatException e) {
            output = 0;
        }

        return output;

    }
}