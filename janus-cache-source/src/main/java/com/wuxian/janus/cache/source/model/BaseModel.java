package com.wuxian.janus.cache.source.model;

import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.cache.source.ErrorFactory;
import com.wuxian.janus.core.basis.BeanCopyUtils;
import com.wuxian.janus.core.basis.FieldCopyHandler;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public abstract class BaseModel<E> {

    @Getter
    @Setter
    protected String id;

    @Getter
    @Setter
    protected E struct;

    protected BaseModel() {
    }

    protected abstract Class<E> getStructClass();

    protected abstract <T extends BaseModel> Class<T> getThisClass();

    private FieldCopyHandler structCopyHandler = (fieldName, copyToValue, copyFromValue, copyTo, copyToFieldSetter)
            -> beforeFieldCopy(BaseModel.this.toString(), fieldName,
            copyToValue, copyFromValue, copyTo, copyToFieldSetter);

    private FieldCopyHandler selfCopyHandler = (fieldName, copyToValue, copyFromValue, copyTo, copyToFieldSetter) -> {
        List<String> exclude = getSkipCopyFiledNames();
        if (exclude.contains(fieldName)) {
            return true;
        } else {
            return beforeFieldCopy(BaseModel.this.toString(), fieldName,
                    copyToValue, copyFromValue, copyTo, copyToFieldSetter);
        }
    };

    private List<String> getSkipCopyFiledNames() {
        return Collections.singletonList("struct");
    }

    /**
     * 以下情况提前处理，返回true表示skip后续复制行为
     * 1）copyFromValue是List类型，按merge元素方式处理
     * 2）copyFromValue没有值
     * 3）copyFromValue和copyToValue相等
     */
    private boolean beforeFieldCopy(String model, String fieldName, Object copyToValue, Object copyFromValue, Object copyTo, Method copyToFieldSetter) {
        if (copyFromValue != null) {
            if (mergeListField(model, fieldName, copyToValue, copyFromValue, copyTo, copyToFieldSetter)) {
                return true;
            }
            if (copyToValue == null) {
                return false;
            } else {
                if (!copyFromValue.equals(copyToValue)) {
                    //copyToValue和copyFromValue同时不为null有不相等是异常数据
                    throw ErrorFactory.createDuplicatedFieldError(model, fieldName);
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    /**
     * List类型，采用合并两个List的方式处理
     * 是List返回true
     */
    private boolean mergeListField(String model, String fieldName, Object copyToValue, Object copyFromValue, Object copyTo, Method copyToFieldSetter) {
        if (copyFromValue instanceof List) {
            try {
                List copyToValueList;
                if (copyToValue == null) {
                    copyToValueList = new ArrayList();
                    copyToFieldSetter.invoke(copyTo, copyToValueList);
                } else {
                    copyToValueList = (List) copyToValue;
                }
                copyToValueList.addAll((List) copyFromValue);
            } catch (Exception e) {
                e.printStackTrace();
                throw ErrorFactory.createListStructFieldError(model, fieldName);
            }
            return true;
        }
        return false;
    }


    public void mergeFrom(BaseModel<E> other) {
        if (other != null) {
            if (other.struct != null) {
                if (this.struct == null) {
                    try {
                        this.struct = getStructClass().newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                        throw ErrorFactory.createStructMergingFailedError(this.toString(), e.getMessage());
                    }
                }
                BeanCopyUtils.copy(this.struct, other.getStruct(), getStructClass(), structCopyHandler);
            }
            BeanCopyUtils.copy(this, other, getThisClass(), selfCopyHandler);
        }
    }

    /**
     * 将this.getStruct()中不为null的列复制给tmpStruct的同名列。
     * 其中tmpStruct的获取方式如下:
     * 当otherFieldBuilder为null，tmpStruct为getStructClass().newInstance();
     * 否则为otherFieldBuilder的返回值
     *
     * @param otherFieldBuilder 用以填充struct的builder
     * @return 一个填充好数据的struct
     */
    public E buildStruct(Function<BaseModel<E>, E> otherFieldBuilder) {
        Class<E> eClass = getStructClass();
        E struct;
        if (otherFieldBuilder != null) {
            struct = otherFieldBuilder.apply(this);
        } else {
            try {
                struct = eClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw ErrorFactory.createBuildStructFailedError(this.toString(), e.getMessage());
            }
        }
        BeanCopyUtils.copy(struct, this.getStruct(), eClass, structCopyHandler);
        return struct;
    }

    @Override
    public String toString() {
        return "Model( type = " + this.getClass().getSimpleName() + ", id = " + this.id + " )";
    }

    public IdType buildIdType() {
        IdType result = new IdType(null);
        result.setStringValue(this.id);
        return result;
    }

    /**
     * 除Id外的关键列是否相等。关键列相等的model，若Id为null,可参考不为null的Id值补全自身Id。
     * 所有的子类构造函数都要保证要么有值,要么所有的keyFields都有值
     */
    public abstract boolean keyFieldsEquals(BaseModel<E> other);

    public abstract void fillKeyFields(BaseModel<E> other);

    public abstract boolean keyFieldsHasValue();

    public abstract String getKeyFieldsHash();

    public String toHashString() {
        return this.toString() + "( keyFieldsHash = { " + getKeyFieldsHash() + " } )";
    }
}
