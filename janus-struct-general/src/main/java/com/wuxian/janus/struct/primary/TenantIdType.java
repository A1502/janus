package com.wuxian.janus.struct.primary;

public class TenantIdType extends BaseIdType<String> {

    //TID

    public TenantIdType(String value) {
        super(value);
    }

    @Override
    public void setStringValue(String format) {
        //setValue(Long.parseLong(format));
        setValue(format);
    }
}