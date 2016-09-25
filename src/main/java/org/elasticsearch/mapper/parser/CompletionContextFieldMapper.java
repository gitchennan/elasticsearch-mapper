package org.elasticsearch.mapper.parser;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.mapper.annotations.CompletionContext;
import org.elasticsearch.mapper.annotations.CompletionContextType;
import org.elasticsearch.mapper.annotations.CompletionField;
import org.elasticsearch.mapper.utils.StringUtils;

import java.io.IOException;

public class CompletionContextFieldMapper {
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_INDEX = "index";

    public static final String FIELD_ANALYZER = "analyzer";
    public static final String FIELD_SEARCH_ANALYZER = "search_analyzer";

    public static final String COMPLETION_CONTEXT = "context";
    public static final String COMPLETION_PAYLOADS = "payloads";
    public static final String COMPLETION_PRESERVE_SEPARATORS = "preserve_separators";
    public static final String COMPLETION_PRESERVE_POSITION_INCREMENTS = "preserve_position_increments";
    public static final String COMPLETION_MAX_INPUT_LENGTH = "max_input_length";

    public static final String TYPE_VALUE_COMPLETION = "completion";

    public void buildMapping(XContentBuilder mappingBuilder, CompletionField annotation) throws IOException {
        mappingBuilder.field(FIELD_TYPE, TYPE_VALUE_COMPLETION);
        if (annotation != null) {
            mappingBuilder.field(COMPLETION_MAX_INPUT_LENGTH, annotation.maxInputLength());
            mappingBuilder.field(COMPLETION_PAYLOADS, annotation.payloads());
            mappingBuilder.field(COMPLETION_PRESERVE_POSITION_INCREMENTS, annotation.preservePositionIncrements());
            mappingBuilder.field(COMPLETION_PRESERVE_SEPARATORS, annotation.preserveSeparators());
            if (StringUtils.isNotBlank(annotation.searchAnalyzer())) {
                mappingBuilder.field(FIELD_SEARCH_ANALYZER, annotation.searchAnalyzer());
            }

            if (StringUtils.isNotBlank(annotation.analyzer())) {
                mappingBuilder.field(FIELD_ANALYZER, annotation.analyzer());
            }

            if (annotation.context() != null && annotation.context().length > 0) {
                mappingBuilder.startObject(COMPLETION_CONTEXT);
                for (CompletionContext context : annotation.context()) {
                    mappingBuilder.startObject(context.name());
                    mappingBuilder.field("type", context.type());
                    if (StringUtils.isNotBlank(context.path())) {
                        mappingBuilder.field("path", context.path());
                    }
                    if (context.defaultVal() != null && context.defaultVal().length > 0) {
                        mappingBuilder.array("default", context.defaultVal());
                    }
                    if (context.type() == CompletionContextType.geo) {
                        for (String precisionItem : context.precision()) {
                            mappingBuilder.array("precision", context.precision());
                        }
                    }
                    if (!context.neighbors()) {
                        mappingBuilder.field("neighbors", false);
                    }
                    mappingBuilder.endObject();
                }
                mappingBuilder.endObject();
            }
        }
    }
}
