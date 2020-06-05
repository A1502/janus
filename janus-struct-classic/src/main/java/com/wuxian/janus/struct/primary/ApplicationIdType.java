package com.wuxian.janus.struct.primary;

public class ApplicationIdType extends BaseIdType<Long> {

    //AID

    public ApplicationIdType(Long value) {
        super(value);
    }

    @Override
    public void setStringValue(String format) {
        setValue(Long.parseLong(format));
    }
}
