package com.wuxian.janus.entity.primary;

public class ApplicationIdType extends BaseIdType<String> {

    //AID

    public ApplicationIdType(String value) {
        super(value);
    }

    @Override
    public void setStringValue(String format) {
        //setValue(Long.parseLong(format));
        setValue(format);
    }
}
