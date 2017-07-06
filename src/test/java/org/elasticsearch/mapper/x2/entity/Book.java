package org.elasticsearch.mapper.x2.entity;

import org.elasticsearch.mapper.x2.annotations.*;

import java.util.Date;

@Document(type = "book", _timestamp = true, _ttl = @TTL(enabled = true, _default = "5m"))
public class Book {
    /*ID,只能是Long或者String类型*/
    @Id
    private Long id;

    /*数值类型*/
    @Field(type = FieldType.Double, ignoreMalformed = true)
    private Double price;

    /*数值类型*/
    @Field(type = FieldType.Integer)
    private Integer pageCount;

    /*未分词String型*/
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String isnNo;

    /*bool型*/
    @Field(type = FieldType.Boolean, nullValue = "false")
    private Boolean isValid;

    /*日期类型*/
    @Field(type = FieldType.Date, format = DateFormat.basic_time_no_millis)
    private Date publishDate;

    /*分词String类型,并设置fielddata加载限制(当然也可不设置用默认)*/
    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = "ik_max_word",
            searchAnalyzer = "ik_smart",
            termVector = TermVector.with_positions_offsets,
            fielddata = @Fielddata(
                    format = FielddataFormat.paged_bytes,
                    frequency = @FielddataFrequencyFilter(
                            enable = true,
                            min = 0.001,
                            max = 1.2,
                            minSegmentSize = 500
                    ),
                    loading = FielddataLoading.eager_global_ordinals
            )

    )
    private String author;

    /*multi field 类型(用于多字段搜索)*/
    @MultiField(
            mainField = @Field(type = FieldType.String, index = FieldIndex.analyzed, analyzer = "ik_max_word", searchAnalyzer = "ik_smart"),
            otherFields = {
                    @MultiNestedField(dotSuffix = "pinyin", nestedField = @Field(
                            type = FieldType.String,
                            index = FieldIndex.analyzed,
                            analyzer = "lc_index",
                            searchAnalyzer = "lc_search")
                    ),
                    @MultiNestedField(dotSuffix = "english", nestedField = @Field(
                            type = FieldType.String,
                            index = FieldIndex.analyzed,
                            analyzer = "standard")
                    )
            }
    )
    private String title;

    /*Completion Context Suggester配置(如果不配置CompletionContext则是Completion Suggester)*/
    @CompletionField(analyzer = "ik", payloads = true, context = {
            @CompletionContext(name = "bookType", type = CompletionContextType.category, defaultVal = {"algorithm"}),
            @CompletionContext(name = "bookColor", type = CompletionContextType.category, defaultVal = {"red"})
    })
    private String suggestContextField;

    /*二进制类型*/
    @Field(type = FieldType.Binary)
    private byte[] pdf;

    /*内嵌类型*/
    @NestedObject(clazz = SalesArea.class)
    private SalesArea salesArea;

}
