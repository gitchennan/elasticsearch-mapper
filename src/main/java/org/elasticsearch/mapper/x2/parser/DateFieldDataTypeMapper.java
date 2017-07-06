package org.elasticsearch.mapper.x2.parser;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.mapper.x2.annotations.DateFormat;
import org.elasticsearch.mapper.x2.annotations.Field;

import java.io.IOException;

public class DateFieldDataTypeMapper extends AbstractFieldDataTypeMapper {
    @Override
    protected void internalBuildMapping(XContentBuilder mappingBuilder, Field fieldAnnotation) throws IOException {
        //format
        if (fieldAnnotation.format() != DateFormat.none) {
            mappingBuilder.field(FIELD_FORMAT, fieldAnnotation.format());
        }
        //ignore_malformed
        if (fieldAnnotation.ignoreMalformed()) {
            mappingBuilder.field(FIELD_IGNORE_MALFORMED, true);
        }
        //precision_step
        if (fieldAnnotation.precisionStep() > 0) {
            mappingBuilder.field(FIELD_PRECISION_STEP, fieldAnnotation.precisionStep());
        }
    }
}
