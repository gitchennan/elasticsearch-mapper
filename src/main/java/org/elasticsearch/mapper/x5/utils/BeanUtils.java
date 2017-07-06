package org.elasticsearch.mapper.x5.utils;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.List;

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
}
