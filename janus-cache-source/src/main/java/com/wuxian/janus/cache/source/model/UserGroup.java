package com.wuxian.janus.cache.source.model;

import com.wuxian.janus.cache.source.ErrorFactory;
import com.wuxian.janus.cache.source.model.item.TenantItem;
import com.wuxian.janus.cache.source.model.item.UserGroupItem;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.critical.NativeUserGroupEnum;
import com.wuxian.janus.struct.UserGroupStruct;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class UserGroup extends CodeModel<UserGroupStruct> implements TenantItem {

    @Getter
    @Setter
    private String outerObjectCode;

    @Getter
    @Setter
    private String outerObjectTypeCode;

    @Getter
    private List<User> users = new ArrayList<>();

    @Getter
    private List<Role> roles = new ArrayList<>();

    //---------------------------------------------------------------------------------------------------------------------------------

    protected UserGroup() {
    }

    /**
     * byId的静态构造方式，id不能为null
     */
    public static UserGroup byId(String id) {
        if (id == null) {
            throw ErrorFactory.createIdCannotBeNullError();
        }
        UserGroup result = new UserGroup();
        result.id = id;
        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    /**
     * code作为构造函数参数的时永远不能为null
     */
    public UserGroup(String code) {
        super(code);
    }

    public static UserGroup byId(String id, String code) {
        UserGroup result = byId(id);
        result.code = code;
        return result;
    }

    public static UserGroup byStruct(UserGroupStruct struct) {
        return byStruct(struct, null, null);
    }

    public static UserGroup byStruct(UserGroupStruct struct, String outerObjectTypeCode, String outerObjectCode) {
        UserGroup result = new UserGroup(struct.getCode());
        result.setStruct(struct);

        OuterObject.validateNull(outerObjectTypeCode, outerObjectCode);
        result.outerObjectTypeCode = outerObjectTypeCode;
        result.outerObjectCode = outerObjectCode;

        if (struct.getId() != null) {
            result.setId(struct.getId().toString());
        }
        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    public UserGroup(NativeUserGroupEnum code) {
        if (code == null) {
            throw ErrorFactory.createCodeCannotBeNullError();
        }
        this.code = code.getCode();
    }

    public static UserGroup byId(String id, NativeUserGroupEnum code) {
        String codeArg = (code == null ? null : code.getCode());
        return byId(id, codeArg);
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    public UserGroup(String code, String outerObjectTypeCode, String outerObjectCode) {
        this(code);
        OuterObject.validateNull(outerObjectTypeCode, outerObjectCode);
        this.outerObjectTypeCode = outerObjectTypeCode;
        this.outerObjectCode = outerObjectCode;
    }

    public static UserGroup byId(String id, String code, String outerObjectTypeCode, String outerObjectCode) {
        UserGroup result = byId(id, code);
        OuterObject.validateNull(outerObjectTypeCode, outerObjectCode);
        result.outerObjectTypeCode = outerObjectTypeCode;
        result.outerObjectCode = outerObjectCode;
        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    public UserGroup addItem(UserGroupItem... items) {

        for (UserGroupItem item : items) {
            if (item instanceof User) {
                users.add((User) item);
            } else if (item instanceof Role) {
                roles.add((Role) item);
            } else {
                throw ErrorFactory.createIllegalItemTypeError(item.getClass().toString());
            }
        }
        return this;
    }

    @Override
    protected Class<UserGroupStruct> getStructClass() {
        return UserGroupStruct.class;
    }

    @Override
    protected <T extends BaseModel> Class<T> getThisClass() {
        return (Class<T>) this.getClass();
    }

    @Override
    public UserGroupStruct buildStruct(Function<BaseModel<UserGroupStruct>, UserGroupStruct> otherFieldBuilder) {
        UserGroupStruct struct = super.buildStruct(otherFieldBuilder);
        struct.setId(this.buildIdType().getValue());
        struct.setCode(this.getCode());
        return struct;
    }

    @Override
    public boolean keyFieldsEquals(BaseModel<UserGroupStruct> other) {
        if (other instanceof UserGroup) {
            UserGroup otherModel = (UserGroup) other;
            return StrictUtils.equals(this.code, otherModel.code);
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }

    @Override
    public void fillKeyFields(BaseModel<UserGroupStruct> other) {
        if (other instanceof UserGroup) {
            UserGroup otherModel = (UserGroup) other;
            this.code = otherModel.code;
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }
}
