package com.wuxian.janus.entity.primary;

public class UserIdType extends BaseIdType<Long> {

    //UID

    public UserIdType(Long value) {
        super(value);
    }

    @Override
    public void setStringValue(String format) {
        setValue(Long.parseLong(format));
    }
}