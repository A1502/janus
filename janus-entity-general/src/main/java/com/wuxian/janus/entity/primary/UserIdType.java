package com.wuxian.janus.entity.primary;

public class UserIdType extends BaseIdType<String> {

    //UID

    public UserIdType(String value) {
        super(value);
    }

    @Override
    public void setStringValue(String format) {
        setValue(format);
    }
}