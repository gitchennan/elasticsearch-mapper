package org.elasticsearch.mapper.parser;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.mapper.annotations.*;
import org.elasticsearch.mapper.utils.StringUtils;

import java.io.IOException;

public class StringFieldDataTypeMapper extends AbstractFieldDataTypeMapper {
    @Override
    protected void internalBuildMapping(XContentBuilder mappingBuilder, org.elasticsearch.mapper.annotations.Field fieldAnnotation) throws IOException {
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
            if (fieldAnnotation.fielddata().format() == FielddataFormat.paged_bytes) {
                FielddataFrequencyFilter fielddataFrequencyFilter = fieldAnnotation.fielddata().frequency();
                FielddataRegexFilter fielddataRegexFilter = fieldAnnotation.fielddata().regex();
                if (fielddataFrequencyFilter.enable() || fielddataRegexFilter.enable()
                        || fieldAnnotation.fielddata().loading() != FielddataLoading.lazy) {
                    mappingBuilder.startObject(FIELD_DATA);
                    if (fielddataFrequencyFilter.enable()) {
                        mappingBuilder.startObject(FIELD_DATA_FILTER).startObject("frequency");
                        mappingBuilder.field("min", fielddataFrequencyFilter.min());
                        mappingBuilder.field("max", fielddataFrequencyFilter.max());
                        mappingBuilder.field("min_segment_size", fielddataFrequencyFilter.minSegmentSize());
                        mappingBuilder.endObject().endObject();
                    }

                    if (!fielddataFrequencyFilter.enable() && fielddataRegexFilter.enable()) {
                        mappingBuilder.startObject(FIELD_DATA_FILTER).startObject("regex");
                        mappingBuilder.field("regex", fielddataRegexFilter.regex());
                        mappingBuilder.endObject().endObject();
                    }
                    if (fieldAnnotation.fielddata().loading() != FielddataLoading.lazy) {
                        mappingBuilder.field(FIELD_DATA_LOADING, fieldAnnotation.fielddata().loading().name());
                    }
                    mappingBuilder.endObject();
                }
            } else {
                mappingBuilder.startObject(FIELD_DATA);
                mappingBuilder.field(FIELD_FORMAT, FielddataFormat.disabled.name());
                mappingBuilder.endObject();
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
        //similarity
        if (fieldAnnotation.similarity() != Similarity._default) {
            mappingBuilder.field(FIELD_SIMILARITY, fieldAnnotation.similarity().name());
        }
    }
}
