package org.elasticsearch.mapper.mapper;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.mapper.annotations.fieldtype.GeoPointField;
import org.elasticsearch.mapper.bean.GeoPoint;

import java.io.IOException;
import java.lang.reflect.Field;

public class GeoPointFieldMapper {

    public static void main(String[] args) {
        Class cls = String.class;
    }

    public static boolean isValidGeoPointFieldType(Field field) {
        Class<?> fieldClass = field.getType();

        if (!field.isAnnotationPresent(GeoPointField.class)) {
            return false;
        }

        return String.class.isAssignableFrom(fieldClass) || fieldClass == GeoPoint.class;

    }

    public static void mapDataType(XContentBuilder mappingBuilder, Field field) throws IOException {
        if (!isValidGeoPointFieldType(field)) {
            throw new IllegalArgumentException(
                    String.format("field type[%s] is invalid type of percolator.", field.getType()));
        }

        mappingBuilder.field("type", "geo_point");
    }
}
