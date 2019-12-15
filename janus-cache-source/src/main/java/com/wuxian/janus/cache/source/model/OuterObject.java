package com.wuxian.janus.cache.source.model;

import com.wuxian.janus.cache.source.ErrorFactory;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.basis.StringUtils;
import com.wuxian.janus.entity.OuterObjectEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * model包下的BaseModel子类都有共同特点：有构造函数以及对应的byId静态方法。对于nullable有固定规则，查看具体方法注释
 */
public class OuterObject extends CodeModel<OuterObjectEntity> {

    @Getter
    @Setter
    private String outerObjectTypeCode;

    @Getter
    private List<User> users = new ArrayList<>();

    protected OuterObject() {
    }

    /**
     * byId的静态构造方式，id不能为null
     */
    public static OuterObject byId(String id) {
        if (id == null) {
            throw ErrorFactory.createIdCannotBeNullError();
        }
        OuterObject result = new OuterObject();
        result.id = id;
        return result;
    }

    /**
     * code作为构造函数参数的时永远不能为null
     * 这里简化设计,继承了CodeModel,code即是referenceCode
     */
    public OuterObject(String code) {
        super(code);
    }

    public static OuterObject byId(String id, String code) {
        OuterObject result = byId(id);
        if (code == null) {
            throw ErrorFactory.createCodeCannotBeNullError();
        }
        result.code = code;
        return result;
    }

    public OuterObject(String code, String outerObjectTypeCode) {
        this(code);
        if (outerObjectTypeCode == null) {
            throw ErrorFactory.createOuterObjectTypeCodeCannotBeNullError();
        }
        this.outerObjectTypeCode = outerObjectTypeCode;
    }

    public static OuterObject byId(String id, String code, String outerObjectTypeCode) {
        OuterObject result = byId(id, code);
        if (outerObjectTypeCode == null) {
            throw ErrorFactory.createOuterObjectTypeCodeCannotBeNullError();
        }
        result.outerObjectTypeCode = outerObjectTypeCode;
        return result;
    }

    public OuterObject addItem(User... users) {
        this.users.addAll(Arrays.asList(users));
        return this;
    }

    @Override
    protected Class<OuterObjectEntity> getEntityClass() {
        return OuterObjectEntity.class;
    }

    @Override
    protected <T extends BaseModel> Class<T> getThisClass() {
        return (Class<T>) this.getClass();
    }

    @Override
    public OuterObjectEntity buildEntity(Function<BaseModel<OuterObjectEntity>, OuterObjectEntity> otherFieldBuilder) {
        OuterObjectEntity entity = super.buildEntity(otherFieldBuilder);
        entity.setId(this.buildIdType().getValue());
        entity.setReferenceCode(this.getCode());
        return entity;
    }

    @Override
    public boolean keyFieldsEquals(BaseModel<OuterObjectEntity> other) {
        if (other instanceof OuterObject) {
            OuterObject otherModel = (OuterObject) other;
            return StrictUtils.equals(this.code, otherModel.code)
                    && StrictUtils.equals(this.outerObjectTypeCode, otherModel.outerObjectTypeCode);
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }

    @Override
    public void fillKeyFields(BaseModel<OuterObjectEntity> other) {
        if (other instanceof OuterObject) {
            OuterObject otherModel = (OuterObject) other;
            this.code = otherModel.code;
            this.outerObjectTypeCode = otherModel.outerObjectTypeCode;
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }

    @Override
    public boolean keyFieldsHasValue() {
        return this.code != null && this.outerObjectTypeCode != null;
    }

    @Override
    public String getKeyFieldsHash() {
        return StringUtils.safeJoinStrings(this.code, this.outerObjectTypeCode);
    }

    public static void validateNull(String outerObjectCode, String outerObjectTypeCode) {
        if (outerObjectCode != null && outerObjectTypeCode == null) {
            throw ErrorFactory.createOuterObjectTypeCodeCannotBeNullError();
        } else if (outerObjectCode == null && outerObjectTypeCode != null) {
            throw ErrorFactory.createOuterObjectCodeCannotBeNullError();
        }
    }
}
