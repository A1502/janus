package com.wuxian.janus.struct.primary;

public class TenantIdType extends BaseIdType<Long> {

    //TID

    public TenantIdType(Long value) {
        super(value);
    }

    @Override
    public void setStringValue(String format) {
        setValue(Long.parseLong(format));
    }
}