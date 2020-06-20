package com.wuxian.janus.util;

import java.lang.reflect.Method;

public interface FieldCopyHandler {
    boolean skipCopy(String fieldName, Object copyToValue, Object copyFromValue, Object copyTo, Method copyToFieldSetter);
}
