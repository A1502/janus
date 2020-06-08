package com.wuxian.janus.struct.primary;

public class IdType extends BaseIdType<Long> {

    //ID

    public IdType(Long value) {
        super(value);
    }

    @Override
    public void setStringValue(String format) {
        setValue(Long.parseLong(format));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}