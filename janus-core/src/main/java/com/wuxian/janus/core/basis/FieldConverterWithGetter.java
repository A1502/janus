package com.wuxian.janus.core.basis;

import java.lang.reflect.Method;

class FieldConverterWithGetter extends NamedConverter {
    private Method getter;

    public Method getGetter() {
        return getter;
    }

    public FieldConverterWithGetter(NamedConverter converter, Method getter) {
        super(converter.getName(), converter.getAction());
        this.getter = getter;
    }
}
