package com.wuxian.janus.cache.source.model;

import com.wuxian.janus.cache.source.ErrorFactory;
import com.wuxian.janus.cache.source.model.item.RoleItem;
import com.wuxian.janus.cache.source.model.item.TenantItem;
import com.wuxian.janus.cache.source.model.item.UserGroupItem;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.entity.RoleEntity;
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
    @Setter
    private Boolean multiple;

    @Getter
    private List<Permission> permissions = new ArrayList<>();

    @Getter
    private List<User> users = new ArrayList<>();

    //---------------------------------------------------------------------------------------------------------------------------------
    protected Role(boolean multiple) {
        this.multiple = multiple;
    }

    /**
     * byId的静态构造方式，id不能为null
     */
    public static Role byId(String id, boolean multiple) {
        if (id == null) {
            throw ErrorFactory.createIdCannotBeNullError();
        }
        Role result = new Role(multiple);
        result.id = id;
        return result;
    }
    //---------------------------------------------------------------------------------------------------------------------------------

    /**
     * code作为构造函数参数的时永远不能为null
     */
    public Role(String code, boolean multiple) {
        super(code);
        this.multiple = multiple;
    }

    public static Role byId(String id, String code, boolean multiple) {
        Role result = byId(id, multiple);
        result.code = code;
        return result;
    }

    public static Role byEntity(RoleEntity entity) {
        return byEntity(entity, null, null);
    }

    public static Role byEntity(RoleEntity entity, String outerObjectTypeCode, String outerObjectCode) {
        boolean multiple = (outerObjectTypeCode != null);
        Role result = new Role(entity.getCode(), multiple);
        result.setEntity(entity);

        validateMultipleAndNull(multiple, outerObjectTypeCode, outerObjectCode);
        result.outerObjectTypeCode = outerObjectTypeCode;
        result.outerObjectCode = outerObjectCode;

        if (entity.getId() != null) {
            result.setId(entity.getId().toString());
        }
        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------
    public Role(NativeRoleEnum code) {
        this(code == null ? null : code.getCode(), false);
    }

    public static Role byId(String id, NativeRoleEnum code) {
        String codeArg = (code == null ? null : code.getCode());
        return byId(id, codeArg, false);
    }

    //---------------------------------------------------------------------------------------------------------------------------------
    public Role(String code, boolean multiple, String outerObjectTypeCode, String outerObjectCode, String permissionTemplateCode) {
        this(code, multiple);
        validateMultipleAndNull(multiple, outerObjectTypeCode, outerObjectCode);
        this.outerObjectTypeCode = outerObjectTypeCode;
        this.outerObjectCode = outerObjectCode;
        this.permissionTemplateCode = permissionTemplateCode;
    }

    public static Role byId(String id, String code, boolean multiple, String outerObjectTypeCode, String outerObjectCode, String permissionTemplateCode) {
        Role result = byId(id, code, multiple);
        validateMultipleAndNull(multiple, outerObjectTypeCode, outerObjectCode);
        result.outerObjectTypeCode = outerObjectTypeCode;
        result.outerObjectCode = outerObjectCode;
        result.permissionTemplateCode = permissionTemplateCode;
        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------
    private static void validateMultipleAndNull(boolean multiple, String outerObjectTypeCode, String outerObjectCode) {
        OuterObject.validateNull(outerObjectTypeCode, outerObjectCode);
        if (!multiple && outerObjectTypeCode != null) {
            throw ErrorFactory.createOuterObjectTypeCodeOfMultipleRoleMustBeNullError(outerObjectTypeCode);
        }
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
        entity.setMultiple(this.getMultiple());
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
