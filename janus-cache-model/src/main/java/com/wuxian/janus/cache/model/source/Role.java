package com.wuxian.janus.cache.model.source;

import com.wuxian.janus.cache.model.ErrorFactory;
import com.wuxian.janus.cache.model.source.item.RoleItem;
import com.wuxian.janus.cache.model.source.item.TenantItem;
import com.wuxian.janus.cache.model.source.item.UserGroupItem;
import com.wuxian.janus.core.basis.StrictUtils;
import com.wuxian.janus.core.critical.Access;
import com.wuxian.janus.core.critical.AccessControl;
import com.wuxian.janus.core.critical.LevelEnum;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.struct.layer1.RoleStruct;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Role extends CodeModel<RoleStruct> implements TenantItem, UserGroupItem {

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

    /**
     *  的确是默认null
     */
    @Getter
    private Access access;

    @Getter
    private List<Permission> permissions = new ArrayList<>();

    @Getter
    private Map<User, AccessControl> users = new HashMap<>();

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

    public static Role byStruct(RoleStruct struct) {
        return byStruct(struct, null, null);
    }

    public static Role byStruct(RoleStruct struct, String outerObjectTypeCode, String outerObjectCode) {
        boolean multiple = (outerObjectTypeCode != null);
        Role result = new Role(struct.getCode(), multiple);
        result.setStruct(struct);

        validateMultipleAndNull(multiple, outerObjectTypeCode, outerObjectCode);
        result.outerObjectTypeCode = outerObjectTypeCode;
        result.outerObjectCode = outerObjectCode;

        if (struct.getId() != null) {
            result.setId(struct.getId().toString());
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

    //---------------------------------------------------------------------------------------------------------------------------------

    public Role addItem(RoleItem... items) {
        for (RoleItem item : items) {
            if (item instanceof Permission) {
                permissions.add((Permission) item);
            } else if (item instanceof User) {
                addItem((User) item);
            } else {
                throw ErrorFactory.createIllegalItemTypeError(item.getClass().toString());
            }
        }
        return this;
    }

    public Role addItem(User user, AccessControl accessControl) {
        this.users.put(user, accessControl);
        return this;
    }

    public Role addItem(User user, LevelEnum level) {
        return addItem(user, level.toAccessControl());
    }

    public Role addItem(User user) {
        return addItem(user, LevelEnum.THREE.toAccessControl());
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    public Role setAccess(Access access) {
        this.access = access;
        return this;
    }

    @Override
    protected Class<RoleStruct> getStructClass() {
        return RoleStruct.class;
    }

    @Override
    protected <T extends BaseModel> Class<T> getThisClass() {
        return (Class<T>) this.getClass();
    }

    @Override
    public RoleStruct buildStruct(Function<BaseModel<RoleStruct>, RoleStruct> otherFieldBuilder) {
        RoleStruct struct = super.buildStruct(otherFieldBuilder);
        struct.setId(this.buildIdType().getValue());
        struct.setCode(this.getCode());
        struct.setMultiple(this.getMultiple());
        return struct;
    }

    @Override
    public boolean keyFieldsEquals(BaseModel<RoleStruct> other) {
        if (other instanceof Role) {
            Role otherModel = (Role) other;
            return StrictUtils.equals(this.code, otherModel.code);
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }

    @Override
    public void fillKeyFields(BaseModel<RoleStruct> other) {
        if (other instanceof Role) {
            Role otherModel = (Role) other;
            this.code = otherModel.code;
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
