package com.wuxian.janus.cache.source.model;

import com.wuxian.janus.cache.source.model.item.TenantItem;
import com.wuxian.janus.cache.source.model.item.UserGroupItem;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.entity.UserGroupEntity;
import com.wuxian.janus.cache.source.ErrorFactory;
import com.wuxian.janus.core.critical.NativeUserGroupEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class UserGroup extends CodeModel<UserGroupEntity> implements TenantItem {

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

    /**
     * code作为构造函数参数的时永远不能为null
     */
    public UserGroup(String code) {
        super(code);
    }

    public static UserGroup byEntity(UserGroupEntity entity) {
        return byEntity(entity, null, null);
    }

    public static UserGroup byEntity(UserGroupEntity entity, String outerObjectTypeCode, String outerObjectCode) {
        UserGroup result = new UserGroup(entity.getCode());
        result.setEntity(entity);

        OuterObject.validateNull(outerObjectTypeCode, outerObjectCode);
        result.outerObjectTypeCode = outerObjectTypeCode;
        result.outerObjectCode = outerObjectCode;

        if (entity.getId() != null) {
            result.setId(entity.getId().toString());
        }
        return result;
    }

    public static UserGroup byId(String id, String code) {
        UserGroup result = byId(id);
        result.code = code;
        return result;
    }

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
    protected Class<UserGroupEntity> getEntityClass() {
        return UserGroupEntity.class;
    }

    @Override
    protected <T extends BaseModel> Class<T> getThisClass() {
        return (Class<T>) this.getClass();
    }

    @Override
    public UserGroupEntity buildEntity(Function<BaseModel<UserGroupEntity>, UserGroupEntity> otherFieldBuilder) {
        UserGroupEntity entity = super.buildEntity(otherFieldBuilder);
        entity.setId(this.buildIdType().getValue());
        entity.setCode(this.getCode());
        return entity;
    }

    @Override
    public boolean keyFieldsEquals(BaseModel<UserGroupEntity> other) {
        if (other instanceof UserGroup) {
            UserGroup otherModel = (UserGroup) other;
            return StrictUtils.equals(this.code, otherModel.code);
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }

    @Override
    public void fillKeyFields(BaseModel<UserGroupEntity> other) {
        if (other instanceof UserGroup) {
            UserGroup otherModel = (UserGroup) other;
            this.code = otherModel.code;
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }
}
