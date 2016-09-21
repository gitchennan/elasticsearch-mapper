package org.elasticsearch.mapper.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class BeanUtils {
    public static Field[] retrieveFields(Class clazz) {
        ArrayList fields = new ArrayList();
        Class targetClass = clazz;
        do {
            fields.addAll(Arrays.asList(targetClass.getDeclaredFields()));
            targetClass = targetClass.getSuperclass();
        } while (targetClass != null && targetClass != Object.class);

        return (Field[]) fields.toArray(new Field[fields.size()]);
    }
}
