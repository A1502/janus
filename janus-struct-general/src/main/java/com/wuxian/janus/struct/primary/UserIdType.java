package com.wuxian.janus.struct.primary;

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