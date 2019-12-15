package com.wuxian.janus.cache.source.model;

import com.wuxian.janus.cache.source.ErrorFactory;
import lombok.Getter;
import lombok.Setter;

public abstract class CodeModel<E> extends BaseModel<E> {

    @Getter
    @Setter
    protected String code;

    protected CodeModel() {
    }


    public CodeModel(String code) {
        if (code == null) {
            throw ErrorFactory.createCodeCannotBeNullError();
        }
        this.code = code;
    }

    @Override
    public String toString() {
        return "Model( " + this.getClass().getSimpleName() + ", code = " + this.code + " , id = " + this.id + " )";
    }

    @Override
    public boolean keyFieldsHasValue() {
        return this.code != null;
    }

    @Override
    public String getKeyFieldsHash() {
        return this.code;
    }
}
