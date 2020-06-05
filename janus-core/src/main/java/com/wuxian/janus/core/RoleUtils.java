package com.wuxian.janus.core;

import com.wuxian.janus.core.cache.BaseOuterObjectTypeCachePool;
import com.wuxian.janus.core.critical.AccessControlLevel;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.core.critical.NativeUserGroupEnum;
import com.wuxian.janus.core.index.*;
import com.wuxian.janus.struct.*;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.UserIdType;

import java.util.*;
import java.util.stream.Collectors;

public class RoleUtils {

    @SuppressWarnings("all")
    static ExecuteAccessRolePackage getExecuteAccessRolePackage(UserIdType userId,
                                                                List<String> scopes,
                                                                ScopeRoleUserXMap scopeRoleUserSource,
                                                                RoleUserXMap roleUserSource,
                                                                RoleUserGroupXMap roleUserGroupSource,
                                                                RoleOtherXMap roleOtherSource,
                                                                ScopeUserGroupUserXMap scopeUserGroupUserXSource,
                                                                UserGroupUserXMap userGroupUserSource,
                                                                RoleMap singleRoleSource,
                                                                RoleMap multipleRoleSource,
                                                                UserGroupMap userGroupSource,
                                                                BaseOuterObjectTypeCachePool outerObjectCache,
                                                                boolean useSingleRoles,
                                                                boolean nativeRoleOnly,
                                                                ErrorDataRecorder recorder) {

        ExecuteAccessRolePackage result = new ExecuteAccessRolePackage();

        //(STEP 1)Role-User关系查找,并经过scope过滤
        List<RoleUserXStruct> roleUsers = roleUserSource
                .getByUserIdExecuteAccess(userId, true);
        //scope过滤
        roleUsers = filterRoleUserByScopes(scopes, roleUsers, scopeRoleUserSource.getByUserId(userId), recorder);


        //(STEP 2) Role-Other关系查找
        List<RoleOtherXStruct> roleOthers;
        if (nativeRoleOnly) {
            roleOthers = new ArrayList<>();
        } else {
            roleOthers = roleOtherSource.getByExecuteAccess(true);
        }

        //(STEP 3)InnerUserGroup-User关系查找
        List<UserGroupUserXStruct> innerUserGroupUsers = userGroupUserSource
                .getByUserIdExecuteAccess(userId, true);
        //scope过滤
        innerUserGroupUsers = filterUserGroupUserByScopes(scopes, innerUserGroupUsers, scopeUserGroupUserXSource.getByUserId(userId), recorder);

        //(STEP 4) 汇总实体：InnerUserGroup
        List<UserGroupStruct> innerUserGroupList = new ArrayList<>();
        for (UserGroupUserXStruct innerUserGroupUser : innerUserGroupUsers) {
            UserGroupStruct userGroup = userGroupSource.getByKey(new IdType(innerUserGroupUser.getUserGroupId()));
            if (userGroup != null) {
                if (userGroup.getOuterObjectId() != null) {
                    recorder.add(ErrorDataRecorder.TableSchema.UserGroupUserX.TABLE_NAME,
                            new IdType(innerUserGroupUser.getId()),
                            ErrorDataRecorder.TableSchema.UserGroupUserX.USER_GROUP_ID,
                            String.valueOf(innerUserGroupUser.getUserGroupId()),
                            String.format("当前列内容代表的userGroup是内部用户组，而在表%s中是外部用户组，类型不符", ErrorDataRecorder.TableSchema.UserGroup.TABLE_NAME));
                } else {
                    innerUserGroupList.add(userGroup);
                }
            } else {
                recorder.add(ErrorDataRecorder.TableSchema.UserGroupUserX.TABLE_NAME,
                        new IdType(innerUserGroupUser.getId()),
                        ErrorDataRecorder.TableSchema.UserGroupUserX.USER_GROUP_ID,
                        String.valueOf(innerUserGroupUser.getUserGroupId()),
                        String.format("当前列内容代表的userGroup在表%s中并不存在", ErrorDataRecorder.TableSchema.UserGroup.TABLE_NAME));
            }
        }

        //(STEP 5) 汇总实体：OuterUserGroup
        List<UserGroupStruct> outerUserGroupList;
        if (nativeRoleOnly) {
            outerUserGroupList = new ArrayList<>();
        } else {
            outerUserGroupList = UserGroupUtils.getExecuteAccessOuterUserGroups(userId,
                    scopes,
                    outerObjectCache,
                    userGroupSource,
                    recorder
            );
        }

        //(STEP 7) InnerUserGroup和OuterUserGroup合并
        List<UserGroupStruct> allUserGroupList = new ArrayList<>();
        if (nativeRoleOnly) {
            allUserGroupList.addAll(UserGroupUtils.getNativeUserGroups(innerUserGroupList));
        } else {
            allUserGroupList.addAll(innerUserGroupList);
            allUserGroupList.addAll(outerUserGroupList);
        }

        //(STEP 8)关系查找：allUserGroup加入的role
        List<RoleUserGroupXStruct> roleUserGroups = new ArrayList<>();
        for (UserGroupStruct userGroup : allUserGroupList) {
            List<RoleUserGroupXStruct> list = roleUserGroupSource
                    .getByUserGroupIdExecuteAccess(new IdType(userGroup.getId()), true);
            roleUserGroups.addAll(list);
        }

        //(STEP 9)实体转化：来自roleUsers,roleOthers和roleUserGroups
        Map<IdType, RoleStruct> roleMap = new HashMap<>();

        //来自roleUsers
        roleUsers.forEach(o -> {
            RoleStruct roleStruct = getRoleStruct(new IdType(o.getRoleId()),
                    ErrorDataRecorder.TableSchema.RoleUserX.TABLE_NAME,
                    new IdType(o.getId()),
                    ErrorDataRecorder.TableSchema.RoleUserX.ROLE_ID,
                    singleRoleSource,
                    multipleRoleSource,
                    useSingleRoles,
                    recorder);

            fillRole(roleMap, roleStruct, nativeRoleOnly);
        });

        //来自roleOther
        if (!nativeRoleOnly) {
            roleOthers.forEach(o -> {
                RoleStruct roleStruct = getRoleStruct(new IdType(o.getRoleId()),
                        ErrorDataRecorder.TableSchema.RoleOtherX.TABLE_NAME,
                        new IdType(o.getId()),
                        ErrorDataRecorder.TableSchema.RoleOtherX.ROLE_ID,
                        singleRoleSource,
                        multipleRoleSource,
                        useSingleRoles,
                        recorder);
                fillRole(roleMap, roleStruct, nativeRoleOnly);
            });
        }

        //来自roleUserGroups
        roleUserGroups.forEach(o -> {
            RoleStruct roleStruct = getRoleStruct(new IdType(o.getRoleId()),
                    ErrorDataRecorder.TableSchema.RoleUserGroupX.TABLE_NAME,
                    new IdType(o.getId()),
                    ErrorDataRecorder.TableSchema.RoleUserGroupX.ROLE_ID,
                    singleRoleSource,
                    multipleRoleSource,
                    useSingleRoles,
                    recorder);
            fillRole(roleMap, roleStruct, nativeRoleOnly);
        });

        //STEP 10 补充来自Native UserGroup的Native Role
        Set<NativeUserGroupEnum> nativeUserGroups = UserGroupUtils.getNativeUserGroupEnums(innerUserGroupList);
        List<NativeRoleEnum> nativeRoles = getExecuteAccessNativeRole(nativeUserGroups);

        //nativeRole必须是Single的
        if (useSingleRoles) {
            nativeRoles.forEach(role -> {
                //multiple类型匹配的才提取
                //转为role struct添加到roleMap
                RoleStruct struct = getNativeRoleStruct(singleRoleSource, multipleRoleSource, role, recorder);
                if (struct != null && !StrictUtils.containsKey(roleMap, new IdType(struct.getId()))) {
                    roleMap.put(new IdType(struct.getId()), struct);
                }
            });
        }

        //roleMap即是最后结果
        result.getRoles().addAll(roleMap.values());

        //STEP 11 计算hasAllTenantCustomExecuteAccessRoles
        for (NativeUserGroupEnum userGroup : nativeUserGroups) {
            if (userGroup.getAccessControlAbility().getTenantCustomRoleAbility().getExecuteAccess()) {
                //目前2019-9-18，这个条件不会成立。没有设计任何内置用户组能exec所有自定义角色
                //但是这个逻辑应保留
                result.setExecuteAccessOfTenantCustomRoles(true);
                break;
            }
        }
        return result;
    }

    //scopes = null 表示全部
    private static List<RoleUserXStruct> filterRoleUserByScopes(List<String> scopes, List<RoleUserXStruct> executeAccessRoleUsers, List<ScopeRoleUserXStruct> scopeRoleUsers, ErrorDataRecorder recorder) {

        boolean hasError = false;

        List<RoleUserXStruct> filtered = new ArrayList<>();
        //数据检查 roleId在executeAccessRoleUsers中有，但是在scopeRoleUsers中没有;反向检查不用做,因为executeAccess=false的role-user关系在executeAccessRoleUsers变量里本身就不存在
        for (RoleUserXStruct roleUserXStruct : executeAccessRoleUsers) {
            List<String> foundScopes = scopeRoleUsers.stream().filter(o -> StrictUtils.equals(roleUserXStruct.getRoleId(), o.getRoleId())).map(ScopeRoleUserXStruct::getScope).collect(Collectors.toList());
            if (foundScopes.size() == 0) {
                recorder.add(ErrorDataRecorder.TableSchema.RoleUserX.TABLE_NAME,
                        new IdType(roleUserXStruct.getId()),
                        ErrorDataRecorder.TableSchema.RoleUserX.ROLE_ID,
                        String.valueOf(roleUserXStruct.getRoleId()),
                        String.format("当前列内容代表的scope-user-role关系在表%s中并不存在", ErrorDataRecorder.TableSchema.ScopeRoleUserX.TABLE_NAME));
                hasError = true;
                //不中断for循环是因为要收集完整所有的error到recorder
            }
            //foundScopes和scopes有交集的加入到filtered
            if (scopes != null) {
                long hitCount = scopes.stream().filter(foundScopes::contains).count();
                if (hitCount > 0) {
                    filtered.add(roleUserXStruct);
                }
            }
        }
        if (hasError) {
            return new ArrayList<>();
        }
        if (scopes == null) {
            return executeAccessRoleUsers;
        } else {
            return filtered;
        }
    }

    //scope = null表示全部
    private static List<UserGroupUserXStruct> filterUserGroupUserByScopes(List<String> scopes, List<UserGroupUserXStruct> executeAccessUserGroupUsers,
                                                                          List<ScopeUserGroupUserXStruct> scopeUserGroupUsers, ErrorDataRecorder recorder) {

        boolean hasError = false;

        List<UserGroupUserXStruct> filtered = new ArrayList<>();

        for (UserGroupUserXStruct userGroupUserXStruct : executeAccessUserGroupUsers) {
            List<String> foundScopes = scopeUserGroupUsers.stream().filter(o ->
                    StrictUtils.equals(userGroupUserXStruct.getUserGroupId(), o.getUserGroupId()))
                    .map(ScopeUserGroupUserXStruct::getScope).collect(Collectors.toList());
            if (foundScopes.size() == 0) {
                recorder.add(ErrorDataRecorder.TableSchema.UserGroupUserX.TABLE_NAME,
                        new IdType(userGroupUserXStruct.getId()),
                        ErrorDataRecorder.TableSchema.UserGroupUserX.USER_GROUP_ID,
                        String.valueOf(userGroupUserXStruct.getUserGroupId()),
                        String.format("当前列内容代表的scope-userGroup-user关系在表%s中并不存在", ErrorDataRecorder.TableSchema.ScopeUserGroupUserX.TABLE_NAME));
                hasError = true;
                //不中断for循环是因为要收集完整所有的error到recorder
            }
            //foundScopes和scopes有交集的加入到filtered
            if (scopes != null) {
                long hitCount = scopes.stream().filter(foundScopes::contains).count();
                if (hitCount > 0) {
                    filtered.add(userGroupUserXStruct);
                }
            }
        }
        if (hasError) {
            return new ArrayList<>();
        }
        if (scopes == null) {
            return executeAccessUserGroupUsers;
        } else {
            return filtered;
        }
    }

    private static void fillRole(Map<IdType, RoleStruct> roleMap, RoleStruct roleStruct, boolean nativeRoleOnly) {
        if (roleStruct != null) {
            if (!StrictUtils.containsKey(roleMap, new IdType(roleStruct.getId()))) {
                if (nativeRoleOnly) {
                    if (NativeRoleEnum.getByCode(roleStruct.getCode()) != null) {
                        roleMap.put(new IdType(roleStruct.getId()), roleStruct);
                    }
                } else {
                    roleMap.put(new IdType(roleStruct.getId()), roleStruct);
                }
            }
        }
    }

    private static RoleStruct getNativeRoleStruct(RoleMap singleRoleSource,
                                                  RoleMap multipleRoleSource,
                                                  NativeRoleEnum nativeRole,
                                                  ErrorDataRecorder recorder) {

        return getNativeRoleStructByRoleCode(singleRoleSource,
                multipleRoleSource,
                nativeRole.getCode(),
                recorder);
    }


    private static RoleStruct getNativeRoleStructByRoleCode(RoleMap singleRoleSource,
                                                            RoleMap multipleRoleSource,
                                                            String roleCode,
                                                            ErrorDataRecorder recorder) {
        //native role都是single的。但是这里为了扩展性，把useSingleRoles作为变量
        boolean useSingleRoles = true;
        RoleMap roleSource = useSingleRoles ? singleRoleSource : multipleRoleSource;
        RoleMap backUpRoleSource = !useSingleRoles ? singleRoleSource : multipleRoleSource;

        List<RoleStruct> roleEntities = roleSource.getByCode(roleCode);
        if (roleEntities.size() == 0) {
            List<RoleStruct> backUpRoleEntities = backUpRoleSource.getByCode(roleCode);
            if (backUpRoleEntities.size() == 0) {
                //找不到
                recorder.add(ErrorDataRecorder.TableSchema.Role.TABLE_NAME, new IdType(null), ErrorDataRecorder.TableSchema.Role.CODE, String.valueOf(roleCode),
                        String.format("当前列内容代表的role在表%s中并不存在", ErrorDataRecorder.TableSchema.Role.TABLE_NAME));
                return null;
            } else if (backUpRoleEntities.size() > 1) {
                //不唯一
                recorder.add(ErrorDataRecorder.TableSchema.Role.TABLE_NAME, new IdType(null), ErrorDataRecorder.TableSchema.Role.CODE, String.valueOf(roleCode),
                        String.format("当前列内容代表的role在表%s中应该唯一，但是实际存在%s条;" +
                                        "且应该multiple=%s,而实际不符",
                                ErrorDataRecorder.TableSchema.Role.TABLE_NAME,
                                String.valueOf(backUpRoleEntities.size()),
                                String.valueOf(!useSingleRoles)
                        ));
                return null;
            } else {
                //multiple错误
                recorder.add(ErrorDataRecorder.TableSchema.Role.TABLE_NAME, new IdType(null), ErrorDataRecorder.TableSchema.Role.CODE, String.valueOf(roleCode),
                        String.format("当前列内容代表的role在表%s中应该multiple=%s,而实际不符",
                                ErrorDataRecorder.TableSchema.Role.TABLE_NAME,
                                String.valueOf(!useSingleRoles)));
                return null;
            }
        } else if (roleEntities.size() > 1) {
            //不唯一
            recorder.add(ErrorDataRecorder.TableSchema.Role.TABLE_NAME, new IdType(null), ErrorDataRecorder.TableSchema.Role.CODE, String.valueOf(roleCode),
                    String.format("当前列内容代表的role在表%s中应该唯一，但是实际存在%s条",
                            ErrorDataRecorder.TableSchema.Role.TABLE_NAME,
                            String.valueOf(roleEntities.size())));
            return null;
        } else {
            return StrictUtils.get(roleEntities, 0);
        }
    }

    private static List<NativeRoleEnum> getExecuteAccessNativeRole(Set<NativeUserGroupEnum> nativeUserGroups) {

        List<NativeRoleEnum> result = new ArrayList<>();
        AccessControlLevel level = new AccessControlLevel(
                null, true, null, null, null,
                null, null, null, null, null);

        for (NativeUserGroupEnum nativeUserGroupEnum : nativeUserGroups) {
            List<NativeRoleEnum> tmpList = nativeUserGroupEnum.getMatchedNativeRoleEnum(level);
            for (NativeRoleEnum item : tmpList) {
                if (!result.contains(item)) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    private static RoleStruct getRoleStruct(IdType roleId,
                                            String tableName,
                                            IdType id,
                                            String columnName,
                                            RoleMap singleRoleSource,
                                            RoleMap multipleRoleSource,
                                            boolean useSingleRoles,
                                            ErrorDataRecorder recorder) {

        RoleMap roleSource = useSingleRoles ? singleRoleSource : multipleRoleSource;
        RoleMap backUpRoleSource = !useSingleRoles ? singleRoleSource : multipleRoleSource;

        RoleStruct role = roleSource.getByKey(roleId);
        if (role == null) {
            RoleStruct backUpRole = backUpRoleSource.getByKey(roleId);
            if (backUpRole == null) {
                recorder.add(tableName, id, columnName, String.valueOf(roleId),
                        String.format("当前列内容代表的role在表%s中并不存在", ErrorDataRecorder.TableSchema.Role.TABLE_NAME));
            } else {
                recorder.add(tableName, id, columnName, String.valueOf(roleId),
                        String.format("当前列内容代表的role的(操作型权限or数据型权限)权限类型和在表%s中对应的权限类型不一致", ErrorDataRecorder.TableSchema.Role.TABLE_NAME));
            }
            return null;
        } else {
            if (useSingleRoles && role.getOuterObjectId() != null) {
                recorder.add(tableName, id, columnName, String.valueOf(roleId),
                        String.format("当前列内容代表的role在表%s中期望是操作型角色(outer_object_id == null)，但实际为数据型角色", ErrorDataRecorder.TableSchema.Role.TABLE_NAME));
                return null;
            } else if (!useSingleRoles && role.getOuterObjectId() == null) {
                recorder.add(tableName, id, columnName, String.valueOf(roleId),
                        String.format("当前列内容代表的role在表%s中期望是数据型角色(outer_object_id!= null)，但实际为操作型角色\"", ErrorDataRecorder.TableSchema.Role.TABLE_NAME));
                return null;
            } else {
                return role;
            }
        }
    }

}
