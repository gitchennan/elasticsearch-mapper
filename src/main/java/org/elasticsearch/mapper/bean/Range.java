package org.elasticsearch.mapper.bean;

public class Range<DateType> {

    DateType gte;

    DateType lte;

    public DateType getGte() {
        return gte;
    }

    public void setGte(DateType gte) {
        this.gte = gte;
    }

    public DateType getLte() {
        return lte;
    }

    public void setLte(DateType lte) {
        this.lte = lte;
    }
}
