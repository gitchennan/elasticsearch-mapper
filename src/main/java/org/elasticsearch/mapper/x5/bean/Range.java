package org.elasticsearch.mapper.x5.bean;

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
