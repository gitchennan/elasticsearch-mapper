package org.elasticsearch.mapper.x2.entity;

import org.elasticsearch.mapper.x2.annotations.*;

import java.util.Date;

@Document(type = "book", _timestamp = true, _ttl = @TTL(enabled = true, _default = "5m"))
public class Book {
    @Id
    private Long id;

    @Field(type = FieldType.Double, ignoreMalformed = true)
    private Double price;

    @Field(type = FieldType.Integer)
    private Integer pageCount;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String isnNo;

    @Field(type = FieldType.Boolean, nullValue = "false")
    private Boolean isValid;

    @Field(type = FieldType.Date, format = DateFormat.basic_time_no_millis)
    private Date publishDate;

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

    @CompletionField(analyzer = "ik", payloads = true, context = {
            @CompletionContext(name = "bookType", type = CompletionContextType.category, defaultVal = {"algorithm"}),
            @CompletionContext(name = "bookColor", type = CompletionContextType.category, defaultVal = {"red"})
    })
    private String suggestContextField;

    @Field(type = FieldType.Binary)
    private byte[] pdf;

    @NestedObject(clazz = SalesArea.class)
    private SalesArea salesArea;

}
