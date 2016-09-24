package org.elasticsearch.mapper.parser;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.mapper.annotations.DateFormat;
import org.elasticsearch.mapper.annotations.Field;
import org.elasticsearch.mapper.utils.StringUtils;

import java.io.IOException;
import java.util.Date;

public class DateDataTypeMapper extends AbstractDataTypeMapper {
    @Override
    protected void internalBuildMapping(XContentBuilder mappingBuilder, java.lang.reflect.Field field, Field fieldAnnotation) throws IOException {
        //format
        if (fieldAnnotation.format() != DateFormat.none) {
            mappingBuilder.field(FIELD_FORMAT, fieldAnnotation.format());
        }
        //ignore_malformed
        if (fieldAnnotation.ignoreMalformed()) {
            mappingBuilder.field(FIELD_IGNORE_MALFORMED, true);
        }
        //null_values
        if (StringUtils.isNotBlank(fieldAnnotation.nullValue())) {
            mappingBuilder.field(FIELD_NULL_VALUE, new Date(fieldAnnotation.nullValue()));
        }
        //precision_step
        if (fieldAnnotation.precisionStep() > 0) {
            mappingBuilder.field(FIELD_PRECISION_STEP, fieldAnnotation.precisionStep());
        }
    }
}
