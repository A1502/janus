package com.wuxian.janus.cache.source.model;

import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.struct.OuterObjectTypeStruct;
import com.wuxian.janus.cache.source.ErrorFactory;

import java.util.function.Function;

public class OuterObjectType extends CodeModel<OuterObjectTypeStruct> {

    //---------------------------------------------------------------------------------------------------------------------------------

    protected OuterObjectType() {
    }

    /**
     * byId的静态构造方式，id不能为null
     */
    public static OuterObjectType byId(String id) {
        if (id == null) {
            throw ErrorFactory.createIdCannotBeNullError();
        }
        OuterObjectType result = new OuterObjectType();
        result.id = id;
        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    /**
     * code作为构造函数参数的时永远不能为null
     */
    public OuterObjectType(String code) {
        super(code);
    }

    public static OuterObjectType byId(String id, String code) {
        OuterObjectType result = byId(id);
        result.id = id;
        result.code = code;
        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected Class<OuterObjectTypeStruct> getStructClass() {
        return OuterObjectTypeStruct.class;
    }

    @Override
    protected <T extends BaseModel> Class<T> getThisClass() {
        return (Class<T>) this.getClass();
    }

    @Override
    public OuterObjectTypeStruct buildStruct(Function<BaseModel<OuterObjectTypeStruct>, OuterObjectTypeStruct> otherFieldBuilder) {
        OuterObjectTypeStruct struct = super.buildStruct(otherFieldBuilder);
        struct.setId(this.buildIdType().getValue());
        struct.setCode(this.getCode());
        return struct;
    }

    @Override
    public boolean keyFieldsEquals(BaseModel<OuterObjectTypeStruct> other) {
        if (other instanceof OuterObjectType) {
            OuterObjectType otherModel = (OuterObjectType) other;
            return StrictUtils.equals(this.code, otherModel.code);
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }

    @Override
    public void fillKeyFields(BaseModel<OuterObjectTypeStruct> other) {
        if (other instanceof OuterObjectType) {
            OuterObjectType otherModel = (OuterObjectType) other;
            this.code = otherModel.code;
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }
}
