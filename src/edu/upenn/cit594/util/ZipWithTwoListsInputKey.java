package edu.upenn.cit594.util;

import java.util.List;

public class ZipWithTwoListsInputKey {

    private String zip;
    private List<Property> propertyList;
    private List<Population> populationList;

    public ZipWithTwoListsInputKey(String zip, List<Property> propertyList,
                                   List<Population> populationList) {
        this.zip = zip;
        this.propertyList = propertyList;
        this.populationList = populationList;
    }

    public String getZip() {
        return zip;
    }

    public List<Property> getPropertyList() {
        return propertyList;
    }

    public List<Population> getPopulationList() {
        return populationList;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ZipWithTwoListsInputKey other = (ZipWithTwoListsInputKey) o;
        return (getZip().equals(other.getZip())
                && getPropertyList().equals(other.getPropertyList())
                && getPopulationList().equals(other.getPopulationList()) );
    }

    @Override
    public int hashCode() {
        int result = getZip().hashCode();
        result = result + getPropertyList().hashCode();
        result = result + getPopulationList().hashCode();
        return result;
    }




}
