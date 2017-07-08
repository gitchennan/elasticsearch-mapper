package org.elasticsearch.mapper.x5.entity;


import org.elasticsearch.mapper.x5.annotations.Document;
import org.elasticsearch.mapper.x5.annotations.enums.NumberType;
import org.elasticsearch.mapper.x5.annotations.enums.StringType;
import org.elasticsearch.mapper.x5.annotations.fieldtype.*;
import org.elasticsearch.mapper.x5.annotations.meta.MetaField_All;
import org.elasticsearch.mapper.x5.annotations.meta.MetaField_Parent;
import org.elasticsearch.mapper.x5.annotations.meta.MetaField_Routing;

import java.math.BigDecimal;

@Document(_type = "book",
        _routing = @MetaField_Routing(required = true),
        _all = @MetaField_All(enabled = false),
        _parent = @MetaField_Parent(parentClass = {SalesArea.class}))
public class Book {
    @MultiField(
            mainField = @StringField(type = StringType.Keyword, boost = 2.0f),
            fields = {
                    @MultiNestedField(name = "pinyin", field = @StringField(type = StringType.Text, analyzer = "lc_pinyin")),
                    @MultiNestedField(name = "cn", field = @StringField(type = StringType.Text, analyzer = "ik_smart", boost = 2.0f, fielddata = @Fielddata(
                            enable = true,
                            frequency = @FielddataFrequencyFilter(
                                    enable = true,
                                    min = 1,
                                    max = 10,
                                    min_segment_size = 15
                            )
                    )))
            },
            tokenFields = {
                    @TokenCountField(name = "length", analyzer = "standard")
            }
    )
    private String title;

    @NumberField(type = NumberType.Double, index = false, coerce = false)
    private BigDecimal price;

    private BigDecimal salePrice;

    @BooleanField(boost = 2.0f)
    private Boolean isValid;

    private SalesArea[] salesAreas;

    @BinaryField(store = true)
    private String binField;

    @PercolatorField
    private String matchAllQuery;

    @CompletionField
    private String suggest;
}
