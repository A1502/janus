package com.wuxian.janus.cache.model.data;

import com.wuxian.janus.struct.layer1.*;
import com.wuxian.janus.struct.layer2.PermissionStruct;
import com.wuxian.janus.struct.layer2.PermissionTemplateStruct;
import com.wuxian.janus.struct.layer2.RolePermissionXStruct;
import com.wuxian.janus.struct.layer6.UserOuterObjectXStruct;

import java.util.Date;

public final class StructBuilder {

    public static ScopeRoleUserXStruct newScopeRoleUserX(Integer id, String scope, Integer roleId, Integer userId
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate) {
        ScopeRoleUserXStruct result = new ScopeRoleUserXStruct();
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

    public static PermissionStruct newPermission(Integer id
            , Integer permissionTemplateId, Integer tenantId, Integer outerObjectId, String outerObjectRemark
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate, Integer version) {
        PermissionStruct result = new PermissionStruct();
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

    public static RolePermissionXStruct newRolePermissionX(Integer id, Integer roleId, Integer permissionId
            , Integer createdBy, Date createdDate, Integer lastModifiedBy
            , Date lastModifiedDate, Integer version) {
        RolePermissionXStruct result = new RolePermissionXStruct();
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

    public static RoleStruct newRoleStruct(
            Integer id, Integer applicationId, Integer tenantId, String code, Boolean multiple, String name, Boolean enable
            , Integer permissionTemplateId, Integer outerObjectId, String outerObjectRemark, String description
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate, Integer version) {
        RoleStruct result = new RoleStruct();
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


    public static RoleUserXStruct newRoleUserXStruct(Integer id, Integer roleId, Integer userId
            , Boolean viewAccess, Boolean executeAccess, Boolean editAccess, Boolean deleteAccess, Boolean enableAccess
            , Boolean viewControl, Boolean executeControl, Boolean editControl, Boolean deleteControl, Boolean enableControl
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate, Integer version) {
        RoleUserXStruct result = new RoleUserXStruct();
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

    public static PermissionTemplateStruct newPermissionTemplateStruct(Integer id, Integer applicationId, String code, Boolean multiple, String name
            , String description, String permissionLevel, String permissionType, Integer outerObjectTypeId
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate, Integer version) {
        PermissionTemplateStruct result = new PermissionTemplateStruct();
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


    public static RoleOtherXStruct newRoleOtherXStruct(Integer id, Integer roleId
            , Boolean viewAccess, Boolean executeAccess, Boolean editAccess, Boolean deleteAccess, Boolean enableAccess
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate, Integer version) {
        RoleOtherXStruct result = new RoleOtherXStruct();
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


    public static ScopeUserGroupUserXStruct newScopeUserGroupUserXStruct(Integer id, String scope, Integer userGroupId, Integer userId
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate) {
        ScopeUserGroupUserXStruct result = new ScopeUserGroupUserXStruct();
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


    public static UserGroupStruct newUserGroupStruct(Integer id, Integer applicationId, Integer tenantId
            , String code, String name, Boolean enable, String description, Integer outerObjectId
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate, Integer version) {
        UserGroupStruct result = new UserGroupStruct();
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


    public static UserGroupUserXStruct newUserGroupUserXStruct(Integer id, Integer userGroupId, Integer userId
            , Boolean viewAccess, Boolean executeAccess, Boolean editAccess, Boolean deleteAccess, Boolean enableAccess
            , Boolean viewControl, Boolean executeControl, Boolean editControl, Boolean deleteControl, Boolean enableControl
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate, Integer version) {
        UserGroupUserXStruct result = new UserGroupUserXStruct();
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


    public static RoleUserGroupXStruct newRoleUserGroupXStruct(Integer id, Integer roleId, Integer userGroupId
            , Boolean viewAccess, Boolean executeAccess, Boolean editAccess, Boolean deleteAccess, Boolean enableAccess
            , Boolean viewControl, Boolean executeControl, Boolean editControl, Boolean deleteControl, Boolean enableControl
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate, Integer version) {
        RoleUserGroupXStruct result = new RoleUserGroupXStruct();
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

    public static OuterObjectTypeStruct newOuterObjectTypeStruct(Integer id
            , String code, String name, String referenceIdRemark, Boolean usedByUserGroup, Boolean usedByPermission
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate) {
        OuterObjectTypeStruct result = new OuterObjectTypeStruct();
        result.fill(IdBuilder.id(id).getValue()
                , code, name, referenceIdRemark, usedByUserGroup, usedByPermission
                , IdBuilder.uId(createdBy).getValue()
                , createdDate
                , IdBuilder.uId(lastModifiedBy).getValue()
                , lastModifiedDate);
        return result;
    }


    public static OuterObjectStruct newOuterObjectStruct(Integer id
            , Integer outerObjectTypeId, String referenceId, String referenceCode, String referenceName
            , String referenceDescription, Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate) {
        OuterObjectStruct result = new OuterObjectStruct();
        result.fill(IdBuilder.id(id).getValue()
                , IdBuilder.id(outerObjectTypeId).getValue()
                , referenceId, referenceCode, referenceName, referenceDescription
                , IdBuilder.uId(createdBy).getValue()
                , createdDate
                , IdBuilder.uId(lastModifiedBy).getValue()
                , lastModifiedDate);
        return result;
    }


    public static UserOuterObjectXStruct newUserOuterObjectXStruct(Integer id, Integer outerObjectTypeId, String scope, Integer userId, String outerObjectIdList
            , Integer createdBy, Date createdDate, Integer lastModifiedBy, Date lastModifiedDate) {
        UserOuterObjectXStruct result = new UserOuterObjectXStruct();
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


    public static RoleStruct newRoleStruct(){
    RoleStruct result = new RoleStruct();
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
