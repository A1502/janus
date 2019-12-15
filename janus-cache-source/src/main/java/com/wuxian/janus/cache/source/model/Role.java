package com.wuxian.janus.cache.source.model;

import com.wuxian.janus.cache.source.model.item.RoleItem;
import com.wuxian.janus.cache.source.model.item.TenantItem;
import com.wuxian.janus.cache.source.model.item.UserGroupItem;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.entity.RoleEntity;
import com.wuxian.janus.cache.source.ErrorFactory;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Role extends CodeModel<RoleEntity> implements TenantItem, UserGroupItem {

    @Getter
    @Setter
    private String outerObjectCode;

    @Getter
    @Setter
    private String outerObjectTypeCode;

    @Getter
    @Setter
    private String permissionTemplateCode;

    @Getter
    private List<Permission> permissions = new ArrayList<>();

    @Getter
    private List<User> users = new ArrayList<>();

    protected Role() {
    }

    /**
     * byId的静态构造方式，id不能为null
     */
    public static Role byId(String id) {
        if (id == null) {
            throw ErrorFactory.createIdCannotBeNullError();
        }
        Role result = new Role();
        result.id = id;
        return result;
    }

    /**
     * code作为构造函数参数的时永远不能为null
     */
    public Role(String code) {
        super(code);
    }

    public static Role byEntity(RoleEntity entity) {
        return byEntity(entity, null, null);
    }

    public static Role byEntity(RoleEntity entity, String outerObjectTypeCode, String outerObjectCode) {
        Role result = new Role(entity.getCode());
        result.setEntity(entity);

        OuterObject.validateNull(outerObjectTypeCode, outerObjectCode);
        result.outerObjectTypeCode = outerObjectTypeCode;
        result.outerObjectCode = outerObjectCode;

        if (entity.getId() != null) {
            result.setId(entity.getId().toString());
        }
        return result;
    }

    public static Role byId(String id, String code) {
        Role result = byId(id);
        result.code = code;
        return result;
    }

    public Role(NativeRoleEnum code) {
        this(code == null ? null : code.getCode());
    }

    public static Role byId(String id, NativeRoleEnum code) {
        String codeArg = (code == null ? null : code.getCode());
        return byId(id, codeArg);
    }

    public Role(String code, String outerObjectTypeCode, String outerObjectCode, String permissionTemplateCode) {
        this(code);
        OuterObject.validateNull(outerObjectTypeCode, outerObjectCode);
        this.outerObjectTypeCode = outerObjectTypeCode;
        this.outerObjectCode = outerObjectCode;
        this.permissionTemplateCode = permissionTemplateCode;
    }

    public static Role byId(String id, String code, String outerObjectTypeCode, String outerObjectCode, String permissionTemplateCode) {
        Role result = byId(id, code);
        OuterObject.validateNull(outerObjectTypeCode, outerObjectCode);
        result.outerObjectTypeCode = outerObjectTypeCode;
        result.outerObjectCode = outerObjectCode;
        result.permissionTemplateCode = permissionTemplateCode;
        return result;
    }

    public Role addItem(RoleItem... items) {
        for (RoleItem item : items) {
            if (item instanceof Permission) {
                permissions.add((Permission) item);
            } else if (item instanceof User) {
                users.add((User) item);
            } else {
                throw ErrorFactory.createIllegalItemTypeError(item.getClass().toString());
            }
        }
        return this;
    }

    @Override
    protected Class<RoleEntity> getEntityClass() {
        return RoleEntity.class;
    }

    @Override
    protected <T extends BaseModel> Class<T> getThisClass() {
        return (Class<T>) this.getClass();
    }

    @Override
    public RoleEntity buildEntity(Function<BaseModel<RoleEntity>, RoleEntity> otherFieldBuilder) {
        RoleEntity entity = super.buildEntity(otherFieldBuilder);
        entity.setId(this.buildIdType().getValue());
        entity.setCode(this.getCode());
        return entity;
    }

    @Override
    public boolean keyFieldsEquals(BaseModel<RoleEntity> other) {
        if (other instanceof Role) {
            Role otherModel = (Role) other;
            return StrictUtils.equals(this.code, otherModel.code);
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }

    @Override
    public void fillKeyFields(BaseModel<RoleEntity> other) {
        if (other instanceof Role) {
            Role otherModel = (Role) other;
            this.code = otherModel.code;
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }
}
