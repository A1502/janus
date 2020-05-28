package com.wuxian.janus;

import com.wuxian.janus.entity.*;

import java.util.Date;

public final class EntityBuilder {

    public static ScopeRoleUserXEntity newScopeRoleUserX(Integer id, String scope, Integer roleId, Integer userId
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate) {
        ScopeRoleUserXEntity result = new ScopeRoleUserXEntity();
        result.fill(IdBuilder.id(id).getValue()
                , scope
                , IdBuilder.id(roleId).getValue()
                , IdBuilder.uId(userId).getValue()
                , IdBuilder.uId(createdBy).getValue()
                , createdDate
                , IdBuilder.uId(lastModifiedBy).getValue()
                , lastModifiedDate);

        return result;
    }

    public static PermissionEntity newPermission(Integer id
            , Integer permissionTemplateId, Integer tenantId, Integer outerObjectId, String outerObjectRemark
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate, Integer version) {
        PermissionEntity result = new PermissionEntity();
        result.fill(IdBuilder.id(id).getValue()
                , IdBuilder.id(permissionTemplateId).getValue()
                , IdBuilder.tId(tenantId).getValue()
                , IdBuilder.id(outerObjectId).getValue()
                , outerObjectRemark
                , IdBuilder.uId(createdBy).getValue()
                , createdDate
                , IdBuilder.uId(lastModifiedBy).getValue()
                , lastModifiedDate
                , version);
        return result;
    }

    public static RolePermissionXEntity newRolePermissionX(Integer id, Integer roleId, Integer permissionId
            , Integer createdBy, Date createdDate, Integer lastModifiedBy
            , Date lastModifiedDate, Integer version) {
        RolePermissionXEntity result = new RolePermissionXEntity();
        result.fill(IdBuilder.id(id).getValue()
                , IdBuilder.id(roleId).getValue()
                , IdBuilder.id(permissionId).getValue()
                , IdBuilder.uId(createdBy).getValue()
                , createdDate
                , IdBuilder.uId(lastModifiedBy).getValue()
                , lastModifiedDate
                , version);
        return result;
    }

    //todo
    public static RoleEntity newRoleEntity(
            Integer id, Integer applicationId, Integer tenantId, String code, Boolean multiple, String name, Boolean enable
            , Integer permissionTemplateId, Integer outerObjectId, String outerObjectRemark, String description
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate, Integer version) {
        RoleEntity result = new RoleEntity();
        result.fill(IdBuilder.id(id).getValue()
                , IdBuilder.aId(applicationId).getValue()
                , IdBuilder.tId(tenantId).getValue()
                , code
                , multiple
                , name
                , enable
                , IdBuilder.id(permissionTemplateId).getValue()
                , IdBuilder.id(outerObjectId).getValue()
                , outerObjectRemark
                , description
                , IdBuilder.uId(createdBy).getValue()
                , createdDate
                , IdBuilder.uId(lastModifiedBy).getValue()
                , lastModifiedDate
                , version);
        return result;
    }


    public static RoleUserXEntity newRoleUserXEntity(Integer id, Integer roleId, Integer userId
            , Boolean viewAccess, Boolean executeAccess, Boolean editAccess, Boolean deleteAccess, Boolean enableAccess
            , Boolean viewControl, Boolean executeControl, Boolean editControl, Boolean deleteControl, Boolean enableControl
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate, Integer version) {
        RoleUserXEntity result = new RoleUserXEntity();
        result.fill(IdBuilder.id(id).getValue()
                , IdBuilder.id(roleId).getValue()
                , IdBuilder.uId(userId).getValue()
                , viewAccess, executeAccess, editAccess, deleteAccess, enableAccess
                , viewControl, executeControl, editControl, deleteControl, enableControl
                , IdBuilder.uId(createdBy).getValue()
                , createdDate
                , IdBuilder.uId(lastModifiedBy).getValue()
                , lastModifiedDate
                , version);
        return result;
    }

    public static PermissionTemplateEntity newPermissionTemplateEntity(Integer id, Integer applicationId, String code, Boolean multiple, String name
            , String description, String permissionLevel, String permissionType, Integer outerObjectTypeId
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate, Integer version) {
        PermissionTemplateEntity result = new PermissionTemplateEntity();
        result.fill(IdBuilder.id(id).getValue()
                , IdBuilder.aId(applicationId).getValue()
                , code, multiple, name
                , description, permissionLevel, permissionType
                , IdBuilder.id(outerObjectTypeId).getValue()
                , IdBuilder.uId(createdBy).getValue()
                , createdDate
                , IdBuilder.uId(lastModifiedBy).getValue()
                , lastModifiedDate
                , version);
        return result;
    }


    public static RoleOtherXEntity newRoleOtherXEntity(Integer id, Integer roleId
            , Boolean viewAccess, Boolean executeAccess, Boolean editAccess, Boolean deleteAccess, Boolean enableAccess
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate, Integer version) {
        RoleOtherXEntity result = new RoleOtherXEntity();
        result.fill(IdBuilder.id(id).getValue()
                , IdBuilder.id(roleId).getValue()
                , viewAccess, executeAccess, editAccess, deleteAccess, enableAccess
                , IdBuilder.uId(createdBy).getValue()
                , createdDate
                , IdBuilder.uId(lastModifiedBy).getValue()
                , lastModifiedDate
                , version);
        return result;
    }


    public static ScopeUserGroupUserXEntity newScopeUserGroupUserXEntity(Integer id, String scope, Integer userGroupId, Integer userId
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate) {
        ScopeUserGroupUserXEntity result = new ScopeUserGroupUserXEntity();
        result.fill(IdBuilder.id(id).getValue()
                , scope
                , IdBuilder.id(userGroupId).getValue()
                , IdBuilder.uId(userId).getValue()
                , IdBuilder.uId(createdBy).getValue()
                , createdDate
                , IdBuilder.uId(lastModifiedBy).getValue()
                , lastModifiedDate);
        return result;
    }


    public static UserGroupEntity newUserGroupEntity(Integer id, Integer applicationId, Integer tenantId
            , String code, String name, Boolean enable, String description, Integer outerObjectId
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate, Integer version) {
        UserGroupEntity result = new UserGroupEntity();
        result.fill(IdBuilder.id(id).getValue()
                , IdBuilder.aId(applicationId).getValue()
                , IdBuilder.tId(tenantId).getValue()
                , code, name, enable, description
                , IdBuilder.id(outerObjectId).getValue()
                , IdBuilder.uId(createdBy).getValue()
                , createdDate
                , IdBuilder.uId(lastModifiedBy).getValue()
                , lastModifiedDate
                , version);
        return result;
    }


    public static UserGroupUserXEntity newUserGroupUserXEntity(Integer id, Integer userGroupId, Integer userId
            , Boolean viewAccess, Boolean executeAccess, Boolean editAccess, Boolean deleteAccess, Boolean enableAccess
            , Boolean viewControl, Boolean executeControl, Boolean editControl, Boolean deleteControl, Boolean enableControl
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate, Integer version) {
        UserGroupUserXEntity result = new UserGroupUserXEntity();
        result.fill(IdBuilder.id(id).getValue()
                , IdBuilder.id(userGroupId).getValue()
                , IdBuilder.uId(userId).getValue()
                , viewAccess, executeAccess, editAccess, deleteAccess, enableAccess
                , viewControl, executeControl, editControl, deleteControl, enableControl
                , IdBuilder.uId(createdBy).getValue()
                , createdDate
                , IdBuilder.uId(lastModifiedBy).getValue()
                , lastModifiedDate
                , version);
        return result;
    }


    public static RoleUserGroupXEntity newRoleUserGroupXEntity(Integer id, Integer roleId, Integer userGroupId
            , Boolean viewAccess, Boolean executeAccess, Boolean editAccess, Boolean deleteAccess, Boolean enableAccess
            , Boolean viewControl, Boolean executeControl, Boolean editControl, Boolean deleteControl, Boolean enableControl
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate, Integer version) {
        RoleUserGroupXEntity result = new RoleUserGroupXEntity();
        result.fill(IdBuilder.id(id).getValue()
                , IdBuilder.id(roleId).getValue()
                , IdBuilder.id(userGroupId).getValue()
                , viewAccess, executeAccess, editAccess, deleteAccess, enableAccess
                , viewControl, executeControl, editControl, deleteControl, enableControl
                , IdBuilder.uId(createdBy).getValue()
                , createdDate
                , IdBuilder.uId(lastModifiedBy).getValue()
                , lastModifiedDate
                , version);
        return result;
    }

    public static OuterObjectTypeEntity newOuterObjectTypeEntity(Integer id
            , String code, String name, String referenceIdRemark, Boolean usedByUserGroup, Boolean usedByPermission
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate) {
        OuterObjectTypeEntity result = new OuterObjectTypeEntity();
        result.fill(IdBuilder.id(id).getValue()
                , code, name, referenceIdRemark, usedByUserGroup, usedByPermission
                , IdBuilder.uId(createdBy).getValue()
                , createdDate
                , IdBuilder.uId(lastModifiedBy).getValue()
                , lastModifiedDate);
        return result;
    }


    public static OuterObjectEntity newOuterObjectEntity(Integer id
            , Integer outerObjectTypeId, String referenceId, String referenceCode, String referenceName
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate) {
        OuterObjectEntity result = new OuterObjectEntity();
        result.fill(IdBuilder.id(id).getValue()
                , IdBuilder.id(outerObjectTypeId).getValue()
                , referenceId, referenceCode, referenceName
                , IdBuilder.uId(createdBy).getValue()
                , createdDate
                , IdBuilder.uId(lastModifiedBy).getValue()
                , lastModifiedDate);
        return result;
    }


    public static UserOuterObjectXEntity newUserOuterObjectXEntity(Integer id, Integer outerObjectTypeId, String scope, Integer userId, String outerObjectIdList
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate) {
        UserOuterObjectXEntity result = new UserOuterObjectXEntity();
        result.fill(IdBuilder.id(id).getValue()
                , IdBuilder.id(outerObjectTypeId).getValue()
                , scope
                , IdBuilder.uId(userId).getValue()
                , outerObjectIdList
                , IdBuilder.uId(createdBy).getValue()
                , createdDate
                , IdBuilder.uId(lastModifiedBy).getValue()
                , lastModifiedDate);
        return result;
    }
    /*
    Integer

    下面是方法模板

    , IdBuilder.id(outerObjectTypeId).getValue()
    , IdBuilder.id(outerObjectId).getValue()
    , IdBuilder.id(userGroupId).getValue()
    , IdBuilder.uId(userId).getValue()
    , IdBuilder.id(roleId).getValue()
    , IdBuilder.aId(applicationId).getValue()
    , IdBuilder.id(roleId).getValue()
    , IdBuilder.id(permissionId).getValue()
    , IdBuilder.tId(tenantId).getValue()


    public static RoleEntity newRoleEntity(){
    RoleEntity result = new RoleEntity();
    result.fill(IdBuilder.id(id).getValue()




            , IdBuilder.uId(createdBy).getValue()
            , createdDate
            , IdBuilder.uId(lastModifiedBy).getValue()
            , lastModifiedDate
            , version);
        return result;
    }



     */
}
