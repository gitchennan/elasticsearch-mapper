package org.elasticsearch.mapper.parser;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.mapper.annotations.DateFormat;
import org.elasticsearch.mapper.annotations.Field;

import java.io.IOException;

public class IpFieldDataTypeMapper extends AbstractFieldDataTypeMapper {
    @Override
    protected void internalBuildMapping(XContentBuilder mappingBuilder, Field fieldAnnotation) throws IOException {
        //precision_step
        if (fieldAnnotation.precisionStep() > 0) {
            mappingBuilder.field(FIELD_PRECISION_STEP, fieldAnnotation.precisionStep());
        }
    }
}
