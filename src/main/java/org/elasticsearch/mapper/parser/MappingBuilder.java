package org.elasticsearch.mapper.parser;

import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.mapper.annotations.*;
import org.elasticsearch.mapper.utils.BeanUtils;
import org.elasticsearch.mapper.utils.StringUtils;

import java.io.IOException;
import java.util.Arrays;

public class MappingBuilder {
    public static final String FIELD_STORE = "store";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_INDEX = "index";
    public static final String FIELD_FORMAT = "format";
    public static final String FIELD_SEARCH_ANALYZER = "search_analyzer";
    public static final String FIELD_ANALYZER = "analyzer";
    public static final String FIELD_PROPERTIES = "properties";
    public static final String FIELD_PARENT = "_parent";
    public static final String COMPLETION_CONTEXT = "context";
    public static final String COMPLETION_PAYLOADS = "payloads";
    public static final String COMPLETION_PRESERVE_SEPARATORS = "preserve_separators";
    public static final String COMPLETION_PRESERVE_POSITION_INCREMENTS = "preserve_position_increments";
    public static final String COMPLETION_MAX_INPUT_LENGTH = "max_input_length";
    public static final String INDEX_VALUE_NOT_ANALYZED = "not_analyzed";
    public static final String TYPE_VALUE_STRING = "string";
    public static final String TYPE_VALUE_GEO_POINT = "geo_point";
    public static final String TYPE_VALUE_COMPLETION = "completion";

    private MappingBuilder() {

    }

    public static XContentBuilder buildMapping(Class clazz) throws IOException {
        if (clazz == null || !clazz.isAnnotationPresent(Document.class)) {
            throw new IllegalArgumentException("The clazz cannot be null and annotation present [@Document]");
        }
        Document docType = (Document) clazz.getAnnotation(Document.class);
        return buildMapping(clazz, docType.type(), null);
    }

    public static XContentBuilder buildMapping(Class clazz, String indexType, String parentType) throws IOException {
        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject(indexType);
        if (StringUtils.isNotBlank(parentType)) {
            mapping.startObject(FIELD_PARENT).field(FIELD_TYPE, parentType).endObject();
        }
        if (clazz.isAnnotationPresent(MappingSetting.class)) {
            mapSetting(mapping, clazz);
        }
        XContentBuilder xContentBuilder = mapping.startObject(FIELD_PROPERTIES);
        mapEntity(xContentBuilder, clazz, true, "", false, FieldType.Auto);
        return xContentBuilder.endObject().endObject().endObject();
    }

    private static void mapSetting(XContentBuilder mapping, Class clazz) throws IOException {
        MappingSetting setting = (MappingSetting) clazz.getAnnotation(MappingSetting.class);
        if (setting.timestamp()) {
            mapping.startObject("_timestamp").field("store", true).field("enabled", true).endObject();
        }
    }

    private static void mapEntity(XContentBuilder xContentBuilder, Class clazz, boolean isRootObject,
                                  String nestedObjectFieldName, boolean nestedOrObjectField, FieldType fieldType) throws IOException {
        java.lang.reflect.Field[] fields = BeanUtils.retrieveFields(clazz);
        if (!isRootObject && (isAnyPropertyAnnotatedAsField(fields) || nestedOrObjectField)) {
            String type = FieldType.Object.toString().toLowerCase();
            if (nestedOrObjectField) {
                type = fieldType.toString().toLowerCase();
            }

            XContentBuilder t = xContentBuilder.startObject(nestedObjectFieldName).field(FIELD_TYPE, type);
            t.startObject(FIELD_PROPERTIES);
        }

        for (java.lang.reflect.Field field : fields) {
            if (!isInIgnoreFields(field)) {
                boolean isGeoField = isGeoField(field);
                boolean isCompletionField = isCompletionField(field);

                Field singleField = field.getAnnotation(Field.class);
                if (!isGeoField && !isCompletionField && isEntity(field) && isAnnotated(field)) {
                    if (singleField == null) {
                        continue;
                    }

                    boolean multiField = isNestedOrObjectField(field);
                    mapEntity(xContentBuilder, getFieldType(field), false, field.getName(), multiField, singleField.type());
                    if (multiField) {
                        continue;
                    }
                }

                MultiField multiField = field.getAnnotation(MultiField.class);
                NestedObject nestedObject = field.getAnnotation(NestedObject.class);

                if (isGeoField) {
                    applyGeoPointFieldMapping(xContentBuilder, field);
                }

                if (isCompletionField) {
                    CompletionField completionField = field.getAnnotation(CompletionField.class);
                    applyCompletionFieldMapping(xContentBuilder, field, completionField);
                }

                if (isRootObject && isIdField(field)) {
                    applyIdFieldMapping(xContentBuilder, field);
                } else if (multiField != null) {
                    addMultiFieldMapping(xContentBuilder, field, multiField);
                } else if (singleField != null) {
                    addSingleFieldMapping(xContentBuilder, field, singleField);
                } else if (nestedObject != null) {
                    mapEntity(xContentBuilder, nestedObject.clazz(), false, nestedObject.name(), true, FieldType.Nested);
                }
            }
        }

        if (!isRootObject && isAnyPropertyAnnotatedAsField(fields) || nestedOrObjectField) {
            xContentBuilder.endObject().endObject();
        }
    }

    private static boolean isAnnotated(java.lang.reflect.Field field) {
        return field.getAnnotation(Field.class) != null || field.getAnnotation(MultiField.class) != null || field.getAnnotation(GeoPointField.class) != null || field.getAnnotation(CompletionField.class) != null;
    }

    private static void applyGeoPointFieldMapping(XContentBuilder xContentBuilder, java.lang.reflect.Field field) throws IOException {
        xContentBuilder.startObject(field.getName());
        xContentBuilder.field(FIELD_TYPE, TYPE_VALUE_GEO_POINT).endObject();
    }

    private static void applyCompletionFieldMapping(XContentBuilder xContentBuilder, java.lang.reflect.Field field, CompletionField annotation) throws IOException {
        xContentBuilder.startObject(field.getName());
        xContentBuilder.field(FIELD_TYPE, TYPE_VALUE_COMPLETION);
        if (annotation != null) {
            xContentBuilder.field(COMPLETION_MAX_INPUT_LENGTH, annotation.maxInputLength());
            xContentBuilder.field(COMPLETION_PAYLOADS, annotation.payloads());
            xContentBuilder.field(COMPLETION_PRESERVE_POSITION_INCREMENTS, annotation.preservePositionIncrements());
            xContentBuilder.field(COMPLETION_PRESERVE_SEPARATORS, annotation.preserveSeparators());
            if (StringUtils.isNotBlank(annotation.searchAnalyzer())) {
                xContentBuilder.field(FIELD_SEARCH_ANALYZER, annotation.searchAnalyzer());
            }

            if (StringUtils.isNotBlank(annotation.analyzer())) {
                xContentBuilder.field(FIELD_ANALYZER, annotation.analyzer());
            }

            if (annotation.context() != null && annotation.context().length > 0) {
                xContentBuilder.startObject(COMPLETION_CONTEXT);
                for (CompletionContext context : annotation.context()) {
                    xContentBuilder.startObject(context.name());
                    xContentBuilder.field("type", context.type());
                    if (StringUtils.isNotBlank(context.path())) {
                        xContentBuilder.field("path", context.path());
                    }
                    if (context.defaultVal() != null && context.defaultVal().length > 0) {
                        xContentBuilder.array("default", context.defaultVal());
                    }
                    xContentBuilder.endObject();
                }
                xContentBuilder.endObject();
            }
        }

        xContentBuilder.endObject();
    }

    private static void applyIdFieldMapping(XContentBuilder xContentBuilder, java.lang.reflect.Field field)
            throws IOException {
        if (String.class.equals(field.getType())) {
            xContentBuilder.startObject(field.getName())
                    .field(FIELD_TYPE, TYPE_VALUE_STRING)
                    .field(FIELD_INDEX, INDEX_VALUE_NOT_ANALYZED);
            xContentBuilder.endObject();
        } else if (Long.class.equals(field.getType())
                || (field.getType().isPrimitive() && "long".equals(field.getType().getName()))) {
            xContentBuilder.startObject(field.getName()).field(FIELD_TYPE, "long");
            xContentBuilder.endObject();
        } else {
            throw new IllegalArgumentException("@Id data type must Long or String");
        }
    }

    private static void addSingleFieldMapping(XContentBuilder xContentBuilder, java.lang.reflect.Field field, Field fieldAnnotation) throws IOException {
        xContentBuilder.startObject(field.getName());
        xContentBuilder.field(FIELD_STORE, fieldAnnotation.store());
        //data type
        if (FieldType.Auto != fieldAnnotation.type()) {
            xContentBuilder.field(FIELD_TYPE, fieldAnnotation.type().name().toLowerCase());
            if (FieldType.Date == fieldAnnotation.type() && DateFormat.none != fieldAnnotation.format()) {
                Object dateFormat = DateFormat.custom == fieldAnnotation.format() ? fieldAnnotation.pattern() : fieldAnnotation.format();
                xContentBuilder.field(FIELD_FORMAT, dateFormat);
            }
        }
        if (FieldIndex.not_analyzed == fieldAnnotation.index() || FieldIndex.no == fieldAnnotation.index()) {
            xContentBuilder.field(FIELD_INDEX, fieldAnnotation.index().name().toLowerCase());
        }
        if (String.class.equals(field.getType()) && FieldIndex.analyzed == fieldAnnotation.index()) {
            if (StringUtils.isNotBlank(fieldAnnotation.searchAnalyzer())) {
                xContentBuilder.field(FIELD_SEARCH_ANALYZER, fieldAnnotation.searchAnalyzer());
            }
            if (StringUtils.isNotBlank(fieldAnnotation.analyzer())) {
                xContentBuilder.field(FIELD_ANALYZER, fieldAnnotation.analyzer());
            }
        }
        xContentBuilder.endObject();
    }

    private static void addNestedFieldMapping(XContentBuilder builder, java.lang.reflect.Field field, NestedField annotation) throws IOException {
        builder.startObject(field.getName() + "." + annotation.dotSuffix());
        builder.field(FIELD_STORE, annotation.store());
        if (FieldType.Auto != annotation.type()) {
            builder.field(FIELD_TYPE, annotation.type().name().toLowerCase());
        }
        if (FieldIndex.not_analyzed == annotation.index()) {
            builder.field(FIELD_INDEX, annotation.index().name().toLowerCase());
        }
        if (StringUtils.isNotBlank(annotation.searchAnalyzer())) {
            builder.field(FIELD_SEARCH_ANALYZER, annotation.searchAnalyzer());
        }
        if (StringUtils.isNotBlank(annotation.indexAnalyzer())) {
            builder.field(FIELD_ANALYZER, annotation.indexAnalyzer());
        }
        builder.endObject();
    }

    private static void addMultiFieldMapping(XContentBuilder builder, java.lang.reflect.Field field, MultiField annotation) throws IOException {
        builder.startObject(field.getName());
        builder.field(FIELD_TYPE, "multi_field");
        builder.startObject("fields");
        addSingleFieldMapping(builder, field, annotation.mainField());

        for (NestedField nestedField : annotation.otherFields()) {
            addNestedFieldMapping(builder, field, nestedField);
        }
        builder.endObject();
        builder.endObject();
    }

    private static boolean isEntity(java.lang.reflect.Field field) {
        return field.getType().isAnnotationPresent(Document.class);
    }

    protected static Class<?> getFieldType(java.lang.reflect.Field field) {
        return field.getType();
    }

    private static boolean isAnyPropertyAnnotatedAsField(java.lang.reflect.Field[] fields) {
        if (fields != null) {
            for (java.lang.reflect.Field field : fields) {
                if (field.isAnnotationPresent(Field.class)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isIdField(java.lang.reflect.Field field) {
        return field.isAnnotationPresent(Id.class);
    }

    private static boolean isInIgnoreFields(java.lang.reflect.Field field) {
        Field fieldAnnotation = field.getAnnotation(Field.class);
        if (null != fieldAnnotation) {
            String[] ignoreFields = fieldAnnotation.ignoreFields();
            return Arrays.asList(ignoreFields).contains(field.getName());
        }
        return false;
    }

    private static boolean isNestedOrObjectField(java.lang.reflect.Field field) {
        Field fieldAnnotation = field.getAnnotation(Field.class);
        return fieldAnnotation != null && (FieldType.Nested == fieldAnnotation.type() || FieldType.Object == fieldAnnotation.type());
    }

    private static boolean isGeoField(java.lang.reflect.Field field) {
        return field.getType() == GeoPoint.class || field.getAnnotation(GeoPointField.class) != null;
    }

    private static boolean isCompletionField(java.lang.reflect.Field field) {
        return field.isAnnotationPresent(CompletionField.class);
    }
}
