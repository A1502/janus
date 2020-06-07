package com.wuxian.janus.core.calculate.error;

import com.wuxian.janus.core.cache.BaseOuterObjectTypeCache;
import com.wuxian.janus.core.critical.NativePermissionTemplateEnum;
import com.wuxian.janus.struct.layer1.OuterObjectStruct;
import com.wuxian.janus.struct.layer1.RoleStruct;
import com.wuxian.janus.struct.layer1.RoleUserXStruct;
import com.wuxian.janus.struct.layer1.UserGroupUserXStruct;
import com.wuxian.janus.struct.layer2.PermissionStruct;
import com.wuxian.janus.struct.layer2.PermissionTemplateStruct;
import com.wuxian.janus.struct.layer2.RolePermissionXStruct;
import com.wuxian.janus.struct.layer6.UserOuterObjectXStruct;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.UserIdType;

import java.util.List;
import java.util.stream.Collectors;

public class ErrorInfoFactory {

    public static class OuterObject {
        private static final String TABLE_NAME = "outer_object";
        private static final String OUTER_OBJECT_TYPE_ID = "outer_object_type_id";
    }

    public static class UserGroup {
        private static final String TABLE_NAME = "user_group";
    }

    public static class UserGroupUserX {
        private static final String TABLE_NAME = "user_group_user_x";
        private static final String USER_GROUP_ID = "user_group_id";

        public static void innerUserGroupConflict(ErrorDataRecorder recorder
                , UserGroupUserXStruct userGroupUser) {
            recorder.add(UserGroupUserX.TABLE_NAME,
                    new IdType(userGroupUser.getId()),
                    USER_GROUP_ID,
                    String.valueOf(userGroupUser.getUserGroupId()),
                    String.format("当前列内容代表的userGroup是内部用户组，而在表%s中是外部用户组，类型不符", UserGroupUserX.TABLE_NAME));
        }

        public static void userGroupNotMatch(ErrorDataRecorder recorder
                , UserGroupUserXStruct userGroupUser) {
            recorder.add(UserGroupUserX.TABLE_NAME,
                    new IdType(userGroupUser.getId()),
                    USER_GROUP_ID,
                    String.valueOf(userGroupUser.getUserGroupId()),
                    String.format("当前列内容代表的userGroup在表%s中并不存在", UserGroupUserX.TABLE_NAME));
        }

        public static void scopeUserGroupNotMatch(ErrorDataRecorder recorder
                , UserGroupUserXStruct userGroupUserXStruct) {
            recorder.add(UserGroupUserX.TABLE_NAME,
                    new IdType(userGroupUserXStruct.getId()),
                    USER_GROUP_ID,
                    String.valueOf(userGroupUserXStruct.getUserGroupId()),
                    String.format("当前列内容代表的scope-userGroup-user关系在表%s中并不存在", ScopeUserGroupUserX.TABLE_NAME));

        }
    }

    public static class Role {
        private static final String TABLE_NAME = "role";
        private static final String CODE = "code";

        public static void roleCodeNotMatch(ErrorDataRecorder recorder, String roleCode) {
            recorder.add(Role.TABLE_NAME, new IdType(null), Role.CODE, String.valueOf(roleCode),
                    String.format("当前列内容代表的role在表%s中并不存在", Role.TABLE_NAME));
        }

        public static void roleCodeNotUnique(ErrorDataRecorder recorder
                , String roleCode, List<RoleStruct> roleStructs, boolean multiple) {
            recorder.add(Role.TABLE_NAME, new IdType(null), CODE, String.valueOf(roleCode),
                    String.format("当前列内容代表的role在表%s中应该唯一，但是实际存在%s条;" +
                                    "且应该multiple=%s,而实际不符",
                            Role.TABLE_NAME,
                            String.valueOf(roleStructs.size()),
                            String.valueOf(multiple)));
        }

        public static void multipleNotMatch(ErrorDataRecorder recorder, String roleCode, boolean multiple) {
            recorder.add(Role.TABLE_NAME, new IdType(null), Role.CODE, String.valueOf(roleCode),
                    String.format("当前列内容代表的role在表%s中应该multiple=%s,而实际不符",
                            Role.TABLE_NAME,
                            String.valueOf(multiple)));
        }

        public static void roleCodeNotUnique(ErrorDataRecorder recorder
                , String roleCode, List<RoleStruct> roleStructs) {
            recorder.add(Role.TABLE_NAME, new IdType(null), Role.CODE, String.valueOf(roleCode),
                    String.format("当前列内容代表的role在表%s中应该唯一，但是实际存在%s条",
                            Role.TABLE_NAME,
                            String.valueOf(roleStructs.size())));
        }
    }

    public static class UserOuterObjectX {
        private static final String TABLE_NAME = "user_outer_object_x";
        private static final String OUTER_GROUP_ID_LIST = "outer_group_id_list";

        public static void outerGroupIdListInvalid(ErrorDataRecorder recorder, UserOuterObjectXStruct userOuterObjectX
                , String groupIdStringList, String groupIdString) {
            recorder.add(UserOuterObjectX.TABLE_NAME,
                    new IdType(userOuterObjectX.getId()),
                    UserOuterObjectX.OUTER_GROUP_ID_LIST,
                    groupIdStringList,
                    String.format("当前列内容按逗号split之后的结果中[%s]无法转换为整数", groupIdString)
            );
        }

        public static void outerGroupIdListNotMatch(ErrorDataRecorder recorder, UserOuterObjectXStruct userOuterObjectX
                , String groupIdStringList, IdType groupId) {
            recorder.add(UserOuterObjectX.TABLE_NAME,
                    new IdType(userOuterObjectX.getId()),
                    UserOuterObjectX.OUTER_GROUP_ID_LIST,
                    groupIdStringList,
                    String.format("当前列内容按逗号split之后的结果中[%s]" +
                                    "在表" + OuterObject.TABLE_NAME + "中找不到对应一行数据",
                            groupId
                    )
            );
        }

        public static void outerObjectTypeIdNotMatch(ErrorDataRecorder recorder, UserOuterObjectXStruct userOuterObjectX
                , OuterObjectStruct outerObjectStruct, String groupIdStringList, IdType groupId) {
            recorder.add(UserOuterObjectX.TABLE_NAME,
                    new IdType(userOuterObjectX.getId()),
                    UserOuterObjectX.OUTER_GROUP_ID_LIST,
                    groupIdStringList,
                    String.format("当前列内容按逗号split之后的结果中[%s]" +
                                    "在表" + OuterObject.TABLE_NAME +
                                    "中找到对应一行数据的列" + OuterObject.OUTER_OBJECT_TYPE_ID + "的值[%s]" +
                                    "与当前数据的同名列的值[%s]不一致",
                            groupId,
                            outerObjectStruct.getOuterObjectTypeId(),
                            userOuterObjectX.getOuterObjectTypeId()
                    )
            );
        }
    }

    public static class OuterObjectType {
        private static final String TABLE_NAME = "outer_object_type";

        public static void outerObjectTypeIdScopeUserIdNotUnique(ErrorDataRecorder recorder
                , BaseOuterObjectTypeCache outerObjectTypeCache
                , String scope, UserIdType userId, List<UserOuterObjectXStruct> list) {
            recorder.add(OuterObjectType.TABLE_NAME,
                    new IdType(null),
                    null,
                    null,
                    String.format("按 outerObjectTypeId = [%s] AND scope = [%s] AND userId = [%s] " +
                                    "条件应最多查到一条记录，而实际是[%d]条",
                            outerObjectTypeCache.getOuterObjectTypeId(),
                            scope,
                            userId,
                            list.size()
                    )
            );
        }
    }

    public static class RoleUserX {
        private static final String TABLE_NAME = "role_user_x";
        private static final String ROLE_ID = "role_id";

        public static void scopeRoleUserNotMatch(ErrorDataRecorder recorder, RoleUserXStruct roleUserXStruct) {
            recorder.add(RoleUserX.TABLE_NAME,
                    new IdType(roleUserXStruct.getId())
                    , ROLE_ID
                    , String.valueOf(roleUserXStruct.getRoleId())
                    , String.format("当前列内容代表的scope-role-user关系在表%s中并不存在",
                            ScopeRoleUserX.TABLE_NAME));

        }
    }

    public static class RoleUserGroupX {
        private static final String TABLE_NAME = "role_user_group_x";
        private static final String ROLE_ID = "role_id";
    }

    public static class RoleOtherX {
        private static final String TABLE_NAME = "role_other_x";
        private static final String ROLE_ID = "role_id";
    }

    public static class RolePermissionX {
        private static final String TABLE_NAME = "role_permission_x";
        private static final String PERMISSION_ID = "permission_id";

        public static void permissionIdNotMatch(ErrorDataRecorder recorder
                , RolePermissionXStruct rolePermissionXStruct) {
            recorder.add(RolePermissionX.TABLE_NAME
                    , new IdType(rolePermissionXStruct.getId())
                    , RolePermissionX.PERMISSION_ID
                    , String.valueOf(rolePermissionXStruct.getPermissionId())
                    , String.format("当前列内容代表的permission在表%s中并不存在", Permission.TABLE_NAME)
            );
        }
    }

    public static class Permission {
        private static final String TABLE_NAME = "permission";

        public static void permissionTemplateIdNotUnique(ErrorDataRecorder recorder
                , PermissionTemplateStruct templateStruct
                , List<PermissionStruct> permissionStructs) {
            recorder.add(Permission.TABLE_NAME
                    , new IdType(null)
                    , null
                    , null
                    , String.format("按permissionTemplateId = %s 条件应查到一条记录，而实际是[%d]条",
                            String.valueOf(templateStruct.getId()),
                            permissionStructs.size())
            );
        }

        public static void permissionIdNotMatch(ErrorDataRecorder recorder
                , IdType templateId
                , List<PermissionStruct> permissions) {
            String ids = permissions.stream().map(p -> String.valueOf(p.getId())).collect(Collectors.joining(","));
            recorder.add(Permission.TABLE_NAME
                    , ids
                    , RolePermissionX.PERMISSION_ID
                    , String.valueOf(templateId)
                    , String.format("当前列内容代表的PermissionTemplate在表%s中并不存在", PermissionTemplate.TABLE_NAME)
            );
        }
    }

    public static class PermissionTemplate {
        private static final String TABLE_NAME = "permission_template";
        private static final String CODE = "code";

        public static void nativePermissionTemplateNotUnique(ErrorDataRecorder recorder, NativePermissionTemplateEnum templateEnum
                , List<PermissionTemplateStruct> permissionTemplateStructs) {
            recorder.add(PermissionTemplate.TABLE_NAME, new IdType(null), CODE, String.valueOf(templateEnum.getCode()),
                    String.format("当前列内容代表的permissionTemplate在表%s中应该存在一条，但是实际是%s条;"
                            , PermissionTemplate.TABLE_NAME
                            , String.valueOf(permissionTemplateStructs.size())
                    ));
        }
    }

    public static class ScopeRoleUserX {
        private static final String TABLE_NAME = "scope_role_user_x";
    }

    public static class ScopeUserGroupUserX {
        private static final String TABLE_NAME = "scope_user_group_user_x";
    }

    /**
     * UserGroupUserOther同时需要复用的逻辑
     */
    public static class RoleUserGroupUserOtherX {

        @SuppressWarnings("all")
        public enum TableNameEnum {
            ROLE_USER_GROUP(RoleUserGroupX.TABLE_NAME),
            ROLE_USER(RoleUserX.TABLE_NAME),
            ROLE_OTHER(RoleOtherX.TABLE_NAME);

            private String text;

            TableNameEnum(String text) {
                this.text = text;
            }
        }

        private static final String ROLE_ID = "role_id";

        public static void roleIdNotMatch(ErrorDataRecorder recorder, TableNameEnum tableName
                , IdType id, IdType roleId) {
            recorder.add(tableName.text, id, RoleUserGroupUserOtherX.ROLE_ID, String.valueOf(roleId),
                    String.format("当前列内容代表的role在表%s中并不存在", Role.TABLE_NAME));
        }

        public static void roleMultipleNotMatch(ErrorDataRecorder recorder, TableNameEnum tableName
                , IdType id, IdType roleId) {
            recorder.add(tableName.text, id, RoleUserGroupUserOtherX.ROLE_ID, String.valueOf(roleId),
                    String.format("当前列内容代表的role的(操作型权限or数据型权限)权限类型和在表%s中对应的权限类型不一致"
                            , Role.TABLE_NAME));
        }

        public static void roleMultipleNotFalse(ErrorDataRecorder recorder, TableNameEnum tableName
                , IdType id, IdType roleId) {
            recorder.add(tableName.text, id, RoleUserGroupUserOtherX.ROLE_ID, String.valueOf(roleId),
                    String.format("当前列内容代表的role在表%s中期望是操作型角色(multiple == false)，但实际为数据型角色"
                            , Role.TABLE_NAME));
        }

        public static void roleMultipleNotTrue(ErrorDataRecorder recorder, TableNameEnum tableName
                , IdType id, IdType roleId) {
            recorder.add(tableName.text, id, RoleUserGroupUserOtherX.ROLE_ID, String.valueOf(roleId),
                    String.format("当前列内容代表的role在表%s中期望是数据型角色(multiple == true)，但实际为操作型角色"
                            , Role.TABLE_NAME));
        }
    }
}
