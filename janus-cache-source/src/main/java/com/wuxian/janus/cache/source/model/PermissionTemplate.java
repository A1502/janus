package com.wuxian.janus.cache.source.model;

import com.wuxian.janus.cache.source.model.item.ApplicationItem;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.entity.PermissionTemplateEntity;
import com.wuxian.janus.cache.source.ErrorFactory;
import com.wuxian.janus.core.critical.NativePermissionTemplateEnum;

import java.util.function.Function;

public class PermissionTemplate extends CodeModel<PermissionTemplateEntity> implements ApplicationItem {

    protected PermissionTemplate() {
    }

    /**
     * byId的静态构造方式，id不能为null
     */
    public static PermissionTemplate byId(String id) {
        if (id == null) {
            throw ErrorFactory.createIdCannotBeNullError();
        }
        PermissionTemplate result = new PermissionTemplate();
        result.id = id;
        return result;
    }

    /**
     * code作为构造函数参数的时永远不能为null
     */
    public PermissionTemplate(String code) {
        super(code);
    }

    public PermissionTemplate(NativePermissionTemplateEnum code) {
        this(code == null ? null : code.getCode());
    }

    public static PermissionTemplate byEntity(PermissionTemplateEntity entity) {
        PermissionTemplate result = new PermissionTemplate(entity.getCode());
        result.setEntity(entity);
        if (entity.getId() != null) {
            result.setId(entity.getId().toString());
        }
        return result;
    }

    public static PermissionTemplate byId(String id, String code) {
        PermissionTemplate result = byId (id);
        result.code = code;
        return result;
    }

    @Override
    protected Class<PermissionTemplateEntity> getEntityClass() {
        return PermissionTemplateEntity.class;
    }

    @Override
    protected <T extends BaseModel> Class<T> getThisClass() {
        return (Class<T>) this.getClass();
    }

    @Override
    public PermissionTemplateEntity buildEntity(Function<BaseModel<PermissionTemplateEntity>, PermissionTemplateEntity> otherFieldBuilder) {
        PermissionTemplateEntity entity = super.buildEntity(otherFieldBuilder);
        entity.setId(this.buildIdType().getValue());
        entity.setCode(this.getCode());
        return entity;
    }

    @Override
    public boolean keyFieldsEquals(BaseModel<PermissionTemplateEntity> other) {
        if (other instanceof PermissionTemplate) {
            PermissionTemplate otherModel = (PermissionTemplate) other;
            return StrictUtils.equals(this.code, otherModel.code);
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }

    @Override
    public void fillKeyFields(BaseModel<PermissionTemplateEntity> other) {
        if (other instanceof PermissionTemplate) {
            PermissionTemplate otherModel = (PermissionTemplate) other;
            this.code = otherModel.code;
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }
}
