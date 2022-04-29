package edu.upenn.cit594.util;

public class AverageLivableAreaFetcher extends RealEstateAverageableAttributeFetcher {

    public AverageLivableAreaFetcher() {
        super();
    }


    public int fetchAttribute(Property p) {
        int output = 0;
        try {
            output = Integer.parseInt(p.getTotalLiveableArea());
            return output;
        } catch (NumberFormatException e) {
            //do nothing???
        }

        return output;

    }
}