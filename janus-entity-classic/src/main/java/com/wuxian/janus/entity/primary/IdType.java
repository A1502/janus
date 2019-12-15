package com.wuxian.janus.entity.primary;

public class IdType extends BaseIdType<Long> {

    //ID

    public IdType(Long value) {
        super(value);
    }

    @Override
    public void setStringValue(String format) {
        setValue(Long.parseLong(format));
    }
}