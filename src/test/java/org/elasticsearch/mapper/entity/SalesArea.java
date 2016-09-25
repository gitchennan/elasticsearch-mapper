package org.elasticsearch.mapper.entity;

import org.elasticsearch.mapper.annotations.*;

public class SalesArea {
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String localtionName;

    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = "ik_max_word",
            fielddata = @Fielddata(format = FielddataFormat.disabled)
    )
    private String description;

    @Field(type = FieldType.Integer)
    private int openDays;
}
