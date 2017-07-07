package org.elasticsearch.mapper.x2.parser;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.mapper.x2.annotations.Field;
import org.elasticsearch.mapper.x2.annotations.FieldIndex;
import org.elasticsearch.mapper.x2.utils.StringUtils;

import java.io.IOException;

public abstract class AbstractFieldDataTypeMapper {
    public static final String FIELD_DOC_VALUES = "doc_values";
    public static final String FIELD_BOOST = "boost";
    public static final String FIELD_STORE = "store";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_INDEX = "index";
    public static final String FIELD_FORMAT = "format";
    public static final String FIELD_SEARCH_ANALYZER = "search_analyzer";
    public static final String FIELD_ANALYZER = "analyzer";
    public static final String FIELD_PROPERTIES = "properties";
    public static final String FIELD_PARENT = "_parent";
    public static final String FIELD_DATA = "fielddata";
    public static final String FIELD_IGNORE_ABOVE = "ignore_above";
    public static final String FIELD_NULL_VALUE = "null_value";
    public static final String FIELD_TERM_VECTOR = "term_vector";
    public static final String FIELD_SIMILARITY = "similarity";
    public static final String FIELD_QUOTE_ANALYZER = "search_quote_analyzer";
    public static final String FIELD_COERCE = "coerce";
    public static final String FIELD_IGNORE_MALFORMED = "ignore_malformed";
    public static final String FIELD_INCLUDE_IN_ALL = "include_in_all";
    public static final String FIELD_PRECISION_STEP = "precision_step";

    public static final String FIELD_DATA_FILTER = "filter";
    public static final String FIELD_DATA_LOADING = "loading";

    public static final String COMPLETION_CONTEXT = "context";
    public static final String COMPLETION_PAYLOADS = "payloads";
    public static final String COMPLETION_PRESERVE_SEPARATORS = "preserve_separators";
    public static final String COMPLETION_PRESERVE_POSITION_INCREMENTS = "preserve_position_increments";
    public static final String COMPLETION_MAX_INPUT_LENGTH = "max_input_length";

    public static final String INDEX_VALUE_NOT_ANALYZED = "not_analyzed";

    public static final String TYPE_VALUE_STRING = "string";
    public static final String TYPE_VALUE_GEO_POINT = "geo_point";
    public static final String TYPE_VALUE_COMPLETION = "completion";

    public static void commonMapping(XContentBuilder mappingBuilder, Field fieldAnnotation) throws IOException {
        //store
        if (fieldAnnotation.store()) {
            mappingBuilder.field(FIELD_STORE, true);
        }
        //type
        mappingBuilder.field(FIELD_TYPE, fieldAnnotation.type().name().toLowerCase());
        //index
        if (FieldIndex._default != fieldAnnotation.index()) {
            mappingBuilder.field(FIELD_INDEX, fieldAnnotation.index().name().toLowerCase());
        }
        //boost
        if (fieldAnnotation.boost() != 1.0d) {
            mappingBuilder.field(FIELD_BOOST, fieldAnnotation.boost());
        }
        //doc_values
        if (!fieldAnnotation.docValues()) {
            mappingBuilder.field(FIELD_DOC_VALUES, false);
        }
        //include_in_all
        if (!fieldAnnotation.includeInAll()) {
            mappingBuilder.field(FIELD_INCLUDE_IN_ALL, false);
        }
        //null_value
        if (StringUtils.isNotBlank(fieldAnnotation.nullValue())) {
            mappingBuilder.field(FIELD_NULL_VALUE, fieldAnnotation.nullValue());
        }
    }

    protected abstract void internalBuildMapping(XContentBuilder mappingBuilder, Field fieldAnnotation) throws IOException;

    public final void buildMapping(XContentBuilder mappingBuilder, Field fieldAnnotation) throws IOException {
        commonMapping(mappingBuilder, fieldAnnotation);
        internalBuildMapping(mappingBuilder, fieldAnnotation);
    }

}
