package edu.upenn.cit594.util;

import java.util.List;

public class PropertyListWithZipInputKey {

    private String zip;
    private List<Property> propertyList;

    public PropertyListWithZipInputKey(String zip, List<Property> propertyList) {
        this.zip = zip;
        this.propertyList = propertyList;
    }

    public String getZip() {
        return zip;
    }

    public List<Property> getPropertyList() {
        return propertyList;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final PropertyListWithZipInputKey other = (PropertyListWithZipInputKey) o;
        return (getZip().equals(other.getZip())
                && getPropertyList().equals(other.getPropertyList()) );
    }

    @Override
    public int hashCode() {
        int result = getZip().hashCode();
        result = result + getPropertyList().hashCode();
        return result;
    }

}
