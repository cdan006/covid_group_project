package edu.upenn.cit594.util;


public class Covid {

    String zip;
    String time;
    String date;
    String partialVac;
    String fullyVac;
    String boosters;

    public Covid (String zip, String time, String date, String partialVac, String fullyVac, String boosters) {
        this.zip = zip;
        this.time = time;
        this.date = date;
        this.partialVac = partialVac;
        this.fullyVac = fullyVac;
        this.boosters = boosters;
    }

    public String getZip() {
        return this.zip;
    }
    public String getTime() {
        return this.time;
    }
    public String getDate() {
        return this.date;
    }
    public String getPartialVac() {
        return this.partialVac;
    }

    public String getFullyVac() {
        return this.fullyVac;
    }

    public String getBoosters() {
        return this.boosters;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Covid other = (Covid) o;
        return (   getZip().equals(other.getZip())
                && getTime().equals(other.getTime())
                && getDate().equals(other.getDate())
                && getPartialVac().equals(other.getPartialVac())
                && getFullyVac().equals(other.getFullyVac())
                && getBoosters().equals(other.getBoosters())   );
    }


}
