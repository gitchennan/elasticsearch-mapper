package org.elasticsearch.mapper.x5.utils;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

public class BeanUtils {
    public static Field[] retrieveFields(Class clazz) {
        List<Field> fields = Lists.newArrayList();
        Class targetClass = clazz;
        do {
            fields.addAll(Lists.newArrayList(targetClass.getDeclaredFields()));
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null && targetClass != Object.class);

        return fields.toArray(new Field[fields.size()]);
    }

    public static boolean isCollectionType(Field field) {
        return field.getType() == List.class || field.getType() == Set.class;
    }

    public static boolean isValidCollectionType(Field field) {
        return isCollectionType(field) &&
                ((ParameterizedType) field.getGenericType()).getActualTypeArguments().length == 1;
    }

    public static Type getCollectionGenericType(Field field) {
        return ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
    }
}
