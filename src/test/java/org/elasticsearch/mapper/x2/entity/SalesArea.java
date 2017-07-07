package org.elasticsearch.mapper.x2.entity;

import org.elasticsearch.mapper.x2.annotations.*;

public class SalesArea {
    /*未分词String*/
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String localtionName;

    /*分词String且禁用fielddata*/
    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = "ik_max_word",
            fielddata = @Fielddata(format = FielddataFormat.disabled)
    )
    private String description;

    /*数值型*/
    @Field(type = FieldType.Integer)
    private int openDays;
}
