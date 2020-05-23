package com.wuxian.janus.cache.source.model;

import com.wuxian.janus.entity.primary.IdType;
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
    protected E entity;

    protected BaseModel() {
    }

    protected abstract Class<E> getEntityClass();

    protected abstract <T extends BaseModel> Class<T> getThisClass();

    private FieldCopyHandler entityCopyHandler = (fieldName, copyToValue, copyFromValue, copyTo, copyToFieldSetter)
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
        return Collections.singletonList("entity");
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
                throw ErrorFactory.createListEntityFieldError(model, fieldName);
            }
            return true;
        }
        return false;
    }


    public void mergeFrom(BaseModel<E> other) {
        if (other != null) {
            if (other.entity != null) {
                if (this.entity == null) {
                    try {
                        this.entity = getEntityClass().newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                        throw ErrorFactory.createEntityMergingFailedError(this.toString(), e.getMessage());
                    }
                }
                BeanCopyUtils.copy(this.entity, other.getEntity(), getEntityClass(), entityCopyHandler);
            }
            BeanCopyUtils.copy(this, other, getThisClass(), selfCopyHandler);
        }
    }

    /**
     * 将this.getEntity()中不为null的列复制给tmpEntity的同名列。
     * 其中tmpEntity的获取方式如下:
     * 当otherFieldBuilder为null，tmpEntity为getEntityClass().newInstance();
     * 否则为otherFieldBuilder的返回值
     *
     * @param otherFieldBuilder 用以填充entity的builder
     * @return 一个填充好数据的entity
     */
    public E buildEntity(Function<BaseModel<E>, E> otherFieldBuilder) {
        Class<E> eClass = getEntityClass();
        E entity;
        if (otherFieldBuilder != null) {
            entity = otherFieldBuilder.apply(this);
        } else {
            try {
                entity = eClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw ErrorFactory.createBuildEntityFailedError(this.toString(), e.getMessage());
            }
        }
        BeanCopyUtils.copy(entity, this.getEntity(), eClass, entityCopyHandler);
        return entity;
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
}
