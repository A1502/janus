package com.wuxian.janus.cache.model.source;

import com.wuxian.janus.cache.model.ErrorFactory;
import com.wuxian.janus.cache.model.source.item.RoleItem;
import com.wuxian.janus.cache.model.source.item.TenantItem;
import com.wuxian.janus.core.basis.StrictUtils;
import com.wuxian.janus.core.basis.StringUtils;
import com.wuxian.janus.core.critical.NativePermissionTemplateEnum;
import com.wuxian.janus.struct.layer2.PermissionStruct;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

public class Permission extends BaseModel<PermissionStruct> implements TenantItem, RoleItem {

    @Getter
    @Setter
    private String permissionTemplateCode;

    @Getter
    @Setter
    private String outerObjectCode;

    @Getter
    @Setter
    private String outerObjectTypeCode;

    //---------------------------------------------------------------------------------------------------------------------------------

    protected Permission() {
    }

    /**
     * byId的静态构造方式，id不能为null
     */
    public static Permission byId(String id) {
        if (id == null) {
            throw ErrorFactory.createIdCannotBeNullError();
        }
        Permission result = new Permission();
        result.id = id;
        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    /**
     * code作为构造函数参数的时永远不能为null
     */
    public Permission(String permissionTemplateCode) {
        if (permissionTemplateCode == null) {
            throw ErrorFactory.createPermissionTemplateCodeCannotBeNullError();
        }
        this.permissionTemplateCode = permissionTemplateCode;
    }

    public static Permission byId(String id, String permissionTemplateCode) {
        if (id == null) {
            throw ErrorFactory.createIdCannotBeNullError();
        }
        Permission result = byId(id);
        result.permissionTemplateCode = permissionTemplateCode;
        return result;
    }

    public static Permission byStruct(String permissionTemplateCode, PermissionStruct struct) {
        Permission result = new Permission(permissionTemplateCode);
        result.setStruct(struct);
        if (struct.getId() != null) {
            result.setId(struct.getId().toString());
        }
        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    public Permission(NativePermissionTemplateEnum permissionTemplateCode) {
        this(permissionTemplateCode == null ? null : permissionTemplateCode.getCode());
    }

    public static Permission byId(String id, NativePermissionTemplateEnum permissionTemplateCode) {
        String codeArg = (permissionTemplateCode == null ? null : permissionTemplateCode.getCode());
        return byId(id, codeArg);
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    public Permission(String permissionTemplateCode, String outerObjectTypeCode, String outerObjectCode) {
        this(permissionTemplateCode);
        OuterObject.validateNull(outerObjectTypeCode, outerObjectCode);
        this.outerObjectTypeCode = outerObjectTypeCode;
        this.outerObjectCode = outerObjectCode;
    }

    public static Permission byId(String id, String permissionTemplateCode, String outerObjectTypeCode, String outerObjectCode) {
        Permission result = byId(id, permissionTemplateCode);
        OuterObject.validateNull(outerObjectTypeCode, outerObjectCode);
        result.outerObjectTypeCode = outerObjectTypeCode;
        result.outerObjectCode = outerObjectCode;
        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected Class<PermissionStruct> getStructClass() {
        return PermissionStruct.class;
    }

    @Override
    protected <T extends BaseModel> Class<T> getThisClass() {
        return (Class<T>) this.getClass();
    }

    @Override
    public PermissionStruct buildStruct(Function<BaseModel<PermissionStruct>, PermissionStruct> otherFieldBuilder) {
        PermissionStruct struct = super.buildStruct(otherFieldBuilder);
        struct.setId(this.buildIdType().getValue());
        return struct;
    }

    @Override
    public boolean keyFieldsEquals(BaseModel<PermissionStruct> other) {
        if (other instanceof Permission) {
            Permission otherModel = (Permission) other;
            return StrictUtils.equals(this.permissionTemplateCode, otherModel.permissionTemplateCode)
                    && StrictUtils.equals(this.outerObjectTypeCode, otherModel.outerObjectTypeCode)
                    && StrictUtils.equals(this.outerObjectCode, otherModel.outerObjectCode);
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }

    @Override
    public void fillKeyFields(BaseModel<PermissionStruct> other) {
        if (other instanceof Permission) {
            Permission otherModel = (Permission) other;
            this.permissionTemplateCode = otherModel.permissionTemplateCode;
            this.outerObjectTypeCode = otherModel.outerObjectTypeCode;
            this.outerObjectCode = otherModel.outerObjectCode;
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }

    @Override
    public boolean keyFieldsHasValue() {
        return this.permissionTemplateCode != null;
        //outerObjectTypeCode outerObjectCode可以为null
    }

    @Override
    public String getKeyFieldsHash() {
        return StringUtils.safeJoinStrings(this.permissionTemplateCode, this.outerObjectTypeCode, this.outerObjectCode);
    }
}
