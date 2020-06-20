package com.wuxian.janus.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class ReflectUtils {

    public static List<Field> getAllFields(Class<?> clazz) {
        return getAllFields(null, clazz);
    }

    private static List<Field> getAllFields(List<Field> fields, Class<?> clazz) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

        if (clazz.getSuperclass() != null) {
            getAllFields(fields, clazz.getSuperclass());
        }

        return fields;
    }

    public static Method findGetterMethodByFieldName(Class<?> clazz, String fieldName) {
        return loopClassDeclaredMethods(clazz, (method) -> {
            String methodName = method.getName();
            return StrictUtils.equals(methodName, getFieldMethodName(fieldName, true, true))
                    || StrictUtils.equals(methodName, getFieldMethodName(fieldName, true, false));
        });
    }

    public static Method findSetterMethodByFieldName(Class<?> clazz, String fieldName) {
        String targetSetterMethodName = getFieldMethodName(fieldName, false, false);
        return loopClassDeclaredMethods(clazz, (method) -> {
            String methodName = method.getName();
            return methodName.equals(targetSetterMethodName);
        });
    }

    private static Method loopClassDeclaredMethods(Class<?> clazz, Predicate<Method> predicate) {
        Class<?> tempClass = clazz;

        do {
            Method[] methods = tempClass.getDeclaredMethods();
            for (Method method : methods) {
                if (predicate.test(method)) {
                    return method;
                }
            }
            tempClass = tempClass.getSuperclass();
        } while (!(tempClass.isAssignableFrom(Object.class)));

        return null;
    }

    private static String getFieldMethodName(String name, boolean getter, boolean is) {

        StringBuilder builder = new StringBuilder();
        if (getter) {
            if (is) {
                builder.append("is");
            } else {
                builder.append("get");
            }
        } else {
            builder.append("set");
        }
        builder.append(Character.toUpperCase(name.charAt(0)));
        builder.append(name.substring(1));

        return builder.toString();
    }
}
