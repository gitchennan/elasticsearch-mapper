package org.elasticsearch.mapper.parser;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.mapper.annotations.FieldIndex;
import org.elasticsearch.mapper.annotations.Fielddata;
import org.elasticsearch.mapper.annotations.Similarity;
import org.elasticsearch.mapper.annotations.TermVector;
import org.elasticsearch.mapper.utils.StringUtils;

import java.io.IOException;

public class StringDataTypeMapper extends AbstractDataTypeMapper {
    @Override
    protected void internalBuildMapping(XContentBuilder mappingBuilder, java.lang.reflect.Field field, org.elasticsearch.mapper.annotations.Field fieldAnnotation) throws IOException {
        if (FieldIndex.analyzed == fieldAnnotation.index()) {
            //analyzer
            if (StringUtils.isNotBlank(fieldAnnotation.analyzer())) {
                mappingBuilder.field(FIELD_ANALYZER, fieldAnnotation.analyzer());
            }
            //search_analyzer
            if (StringUtils.isNotBlank(fieldAnnotation.searchAnalyzer())) {
                mappingBuilder.field(FIELD_SEARCH_ANALYZER, fieldAnnotation.searchAnalyzer());
            }
            //fielddata
            if (fieldAnnotation.fielddata() != Fielddata.paged_bytes) {
                mappingBuilder.field(FIELD_DATA, Fielddata.disabled.name());
            }
            //search_quote_analyzer
            if (StringUtils.isNotBlank(fieldAnnotation.searchQuoteAnalyzer())) {
                mappingBuilder.field(FIELD_QUOTE_ANALYZER, fieldAnnotation.searchQuoteAnalyzer());
            }
            //term_vector
            if (fieldAnnotation.termVector() != TermVector.no) {
                mappingBuilder.field(FIELD_TERM_VECTOR, fieldAnnotation.termVector().name());
            }
        }
        //ignore_above
        if (fieldAnnotation.ignoreAbove() > 0) {
            mappingBuilder.field(FIELD_IGNORE_ABOVE, fieldAnnotation.ignoreAbove());
        }
        //null_value
        if (StringUtils.isNotBlank(fieldAnnotation.nullValue())) {
            mappingBuilder.field(FIELD_NULL_VALUE, fieldAnnotation.nullValue());
        }
        //similarity
        if (fieldAnnotation.similarity() != Similarity._default) {
            mappingBuilder.field(FIELD_SIMILARITY, fieldAnnotation.similarity().name());
        }
    }
}
