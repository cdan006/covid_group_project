package edu.upenn.cit594.util;

import java.util.List;

public class vacPerCapitaInputKey {
    private String vacType;
    private String date;
    private List<Covid> covidList;
    private List<Population> populationList;

    public vacPerCapitaInputKey (String vacType, String date, List<Covid> covidList,
                                 List<Population> populationList) {
        this.vacType = vacType;
        this.date = date;
        this.covidList = covidList;
        this.populationList = populationList;
    }

    public String getVacType() {
        return vacType;
    }

    public String getDate() {
        return date;
    }

    public List<Covid> getCovidList() {
        return covidList;
    }

    public List<Population> getPopulationList() {
        return populationList;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final vacPerCapitaInputKey other = (vacPerCapitaInputKey) o;
        return (getVacType().equals(other.getVacType())
                && getDate().equals(other.getDate())
                && getCovidList().equals(other.getCovidList())
                && getPopulationList().equals(other.getPopulationList()) );
    }

    @Override
    public int hashCode() {
        int result = getVacType().hashCode();
        result = result + getDate().hashCode();
        result = result + getCovidList().hashCode();
        result = result + getPopulationList().hashCode();
        return result;
    }


}
