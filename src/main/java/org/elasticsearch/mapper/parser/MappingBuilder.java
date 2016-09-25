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
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_INDEX = "index";
    public static final String FIELD_PROPERTIES = "properties";
    public static final String FIELD_PARENT = "_parent";

    public static final String INDEX_VALUE_NOT_ANALYZED = "not_analyzed";
    public static final String TYPE_VALUE_STRING = "string";
    public static final String TYPE_VALUE_GEO_POINT = "geo_point";

    private MappingBuilder() {

    }

    public static XContentBuilder buildMapping(Class clazz) throws IOException {
        return buildMapping(clazz, null);
    }

    public static XContentBuilder buildMapping(Class clazz, String parentType) throws IOException {
        if (clazz == null || !clazz.isAnnotationPresent(Document.class)) {
            throw new IllegalArgumentException("The clazz cannot be null and annotation present [@Document]");
        }
        Document docType = (Document) clazz.getAnnotation(Document.class);
        String indexType = docType.type();
        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject(indexType);
        if (StringUtils.isNotBlank(parentType)) {
            mapping.startObject(FIELD_PARENT).field(FIELD_TYPE, parentType).endObject();
        }

        MappingSettingMapper mappingSettingMapper = new MappingSettingMapper();
        mappingSettingMapper.buildMapping(mapping, clazz);

        XContentBuilder xContentBuilder = mapping.startObject(FIELD_PROPERTIES);
        mapEntity(xContentBuilder, clazz, true, "", false, FieldType.Auto);
        return xContentBuilder.endObject().endObject().endObject();
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
                    applyCompletionFieldMapping(xContentBuilder, field.getName(), completionField);
                }

                if (isRootObject && isIdField(field)) {
                    applyIdFieldMapping(xContentBuilder, field);
                } else if (multiField != null) {
                    addMultiFieldMapping(xContentBuilder, field, multiField);
                } else if (singleField != null) {
                    addSingleFieldMapping(xContentBuilder, field.getName(), singleField);
                } else if (nestedObject != null) {
                    mapEntity(xContentBuilder, nestedObject.clazz(), false, field.getName(), true, FieldType.Nested);
                }
            }
        }

        if (!isRootObject && isAnyPropertyAnnotatedAsField(fields) || nestedOrObjectField) {
            xContentBuilder.endObject().endObject();
        }
    }

    private static boolean isAnnotated(java.lang.reflect.Field field) {
        return field.getAnnotation(Field.class) != null
                || field.getAnnotation(MultiField.class) != null
                || field.getAnnotation(GeoPointField.class) != null
                || field.getAnnotation(CompletionField.class) != null;
    }

    private static void applyGeoPointFieldMapping(XContentBuilder mappingBuilder, java.lang.reflect.Field field) throws IOException {
        mappingBuilder.startObject(field.getName());
        mappingBuilder.field(FIELD_TYPE, TYPE_VALUE_GEO_POINT);
        mappingBuilder.endObject();
    }

    private static void applyCompletionFieldMapping(XContentBuilder mappingBuilder, String fieldName, CompletionField annotationField) throws IOException {
        mappingBuilder.startObject(fieldName);
        new CompletionContextFieldMapper().buildMapping(mappingBuilder, annotationField);
        mappingBuilder.endObject();
    }

    private static void applyIdFieldMapping(XContentBuilder xContentBuilder, java.lang.reflect.Field field) throws IOException {
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

    private static void addSingleFieldMapping(XContentBuilder mappingBuilder, String fieldName, Field fieldAnnotation) throws IOException {
        mappingBuilder.startObject(fieldName);
        //numeric field setting
        if (fieldAnnotation.type() == FieldType.Long
                || fieldAnnotation.type() == FieldType.Integer
                || fieldAnnotation.type() == FieldType.Short
                || fieldAnnotation.type() == FieldType.Byte
                || fieldAnnotation.type() == FieldType.Double
                || fieldAnnotation.type() == FieldType.Float) {
            new NumericFieldDataTypeMapper().buildMapping(mappingBuilder, fieldAnnotation);
        } else if (fieldAnnotation.type() == FieldType.Date) {
            //date field setting
            new DateFieldDataTypeMapper().buildMapping(mappingBuilder, fieldAnnotation);
        } else if (fieldAnnotation.type() == FieldType.String) {
            //string field setting
            new StringFieldDataTypeMapper().buildMapping(mappingBuilder, fieldAnnotation);
        } else if (fieldAnnotation.type() == FieldType.Ip) {
            //ip field
            new IpFieldDataTypeMapper().buildMapping(mappingBuilder, fieldAnnotation);
        } else {
            //generic mapper
            AbstractFieldDataTypeMapper.commonMapping(mappingBuilder, fieldAnnotation);
        }
        mappingBuilder.endObject();
    }

    private static void addMultiFieldMapping(XContentBuilder builder, java.lang.reflect.Field field, MultiField annotation) throws IOException {
        builder.startObject(field.getName());
        builder.field(FIELD_TYPE, "multi_field");
        builder.startObject("fields");
        addSingleFieldMapping(builder, field.getName(), annotation.mainField());

        for (MultiNestedField multiNestedField : annotation.otherFields()) {
            String nestFieldName = String.format("%s.%s", field.getName(), multiNestedField.dotSuffix());
            addSingleFieldMapping(builder, nestFieldName, multiNestedField.nestedField());
        }
        builder.endObject();
        builder.endObject();
    }

    private static boolean isEntity(java.lang.reflect.Field field) {
        return field.getType().isAnnotationPresent(Document.class);
    }

    private static Class<?> getFieldType(java.lang.reflect.Field field) {
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