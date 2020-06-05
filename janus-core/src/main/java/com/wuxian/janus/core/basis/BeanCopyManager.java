package com.wuxian.janus.core.basis;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class BeanCopyManager<T> {

    private Map<String, Method> fieldGetters;

    private Map<String, Method> fieldSetters;

    private void initGetterSetter(Class<T> structClazz) {
        fieldGetters = new HashMap<>();
        fieldSetters = new HashMap<>();

        List<Field> fields = ReflectUtils.getAllFields(structClazz).stream().filter(
                o -> !Modifier.isStatic(o.getModifiers())
        ).collect(Collectors.toList());

        for (Field field : fields) {

            Method getter = ReflectUtils.findGetterMethodByFieldName(structClazz, field.getName());
            fieldGetters.put(field.getName(), getter);

            Method setter = ReflectUtils.findSetterMethodByFieldName(structClazz, field.getName());
            fieldSetters.put(field.getName(), setter);
        }
    }

    void copy(T copyTo, T copyFrom, Class<T> structClazz, FieldCopyHandler handler) {
        if (fieldGetters == null) {
            initGetterSetter(structClazz);
        }
        for (String fieldName : fieldGetters.keySet()) {
            try {
                Method fieldGetter = fieldGetters.get(fieldName);
                Method fieldSetter = fieldSetters.get(fieldName);

                if (fieldGetter != null) {
                    Object copyFromValue = fieldGetter.invoke(copyFrom);
                    if (handler != null) {
                        Object copyToValue = fieldGetter.invoke(copyTo);
                        if (handler.skipCopy(fieldName, copyToValue, copyFromValue, copyTo, fieldSetter)) {
                            continue;
                        }
                    }
                    if (fieldSetter != null) {
                        fieldSetter.invoke(copyTo, copyFromValue);
                    }
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
