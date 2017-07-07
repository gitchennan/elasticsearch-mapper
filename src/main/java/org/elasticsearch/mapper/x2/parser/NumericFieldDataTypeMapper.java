package org.elasticsearch.mapper.x2.parser;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.mapper.x2.annotations.Field;

import java.io.IOException;

public class NumericFieldDataTypeMapper extends AbstractFieldDataTypeMapper {
    @Override
    protected void internalBuildMapping(XContentBuilder mappingBuilder, Field fieldAnnotation) throws IOException {
        //coerce
        if (!fieldAnnotation.coerce()) {
            mappingBuilder.field(FIELD_COERCE, false);
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
