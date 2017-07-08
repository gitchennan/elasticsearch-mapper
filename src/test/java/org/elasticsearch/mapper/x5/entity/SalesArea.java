package org.elasticsearch.mapper.x5.entity;

import org.elasticsearch.mapper.x5.annotations.Document;
import org.elasticsearch.mapper.x5.annotations.enums.RangeType;
import org.elasticsearch.mapper.x5.annotations.fieldtype.IPField;
import org.elasticsearch.mapper.x5.annotations.fieldtype.RangeField;
import org.elasticsearch.mapper.x5.bean.Range;

import java.util.Date;

@Document(_type = "salesAreaParent")
public class SalesArea {

    private int openDays;

    @RangeField(type = RangeType.DateRange, boost = 3.0f)
    private Range<Date> publishDateRange;

    @IPField
    private String webSiteHost;
}
