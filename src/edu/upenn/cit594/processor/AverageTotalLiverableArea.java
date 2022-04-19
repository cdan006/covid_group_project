package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.Property;

import java.util.List;

public class AverageTotalLiverableArea implements Average {
    @Override
    public double average(List<Property> propertyList, String livableArea) {
        return 0;
    }

    //good example of Strategy - https://www.tutorialspoint.com/design_pattern/strategy_pattern.htm
}
