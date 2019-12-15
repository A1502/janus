package com.wuxian.janus.core;

import com.wuxian.janus.entity.primary.IdType;

import java.util.ArrayList;
import java.util.List;

public class ErrorDataRecorder {

    public List<String> errorList = new ArrayList<>();

    public void add(String tableName, IdType id, String columnName, String data, String message) {
        add(tableName, id.toString(), columnName, data, message);
    }

    public void add(String tableName, String id, String columnName, String data, String message) {
        String errLog = String.format("tableName= %s ,id= %s , columnName= %s , data = %s , message = %s", tableName, id, columnName, data, message);
        errorList.add(errLog);
    }

    public boolean hasError() {
        return errorList.size() > 0;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static class TableSchema {

        public static class OuterObject {
            public static final String TABLE_NAME = "outer_object";
            public static final String OUTER_OBJECT_TYPE_ID = "outer_object_type_id";
        }

        public static class UserGroup {
            public static final String TABLE_NAME = "user_group";
        }

        public static class UserGroupUserX {
            public static final String TABLE_NAME = "user_group_user_x";
            public static final String USER_GROUP_ID = "user_group_id";
        }

        public static class Role {
            public static final String TABLE_NAME = "role";
            public static final String CODE = "code";
        }

        public static class UserOuterObjectX {
            public static final String TABLE_NAME = "user_outer_object_x";
            public static final String OUTER_GROUP_ID_LIST = "outer_group_id_list";
        }

        public static class OuterObjectType {
            public static final String TABLE_NAME = "outer_object_type";
        }

        public static class RoleUserX {
            public static final String TABLE_NAME = "role_user_x";
            public static final String ROLE_ID = "role_id";
        }

        public static class RoleUserGroupX {
            public static final String TABLE_NAME = "role_user_group_x";
            public static final String ROLE_ID = "role_id";
        }

        public static class RoleOtherX {
            public static final String TABLE_NAME = "role_other_x";
            public static final String ROLE_ID = "role_id";
        }

        public static class RolePermissionX {
            public static final String TABLE_NAME = "role_permission_x";
            public static final String PERMISSION_ID = "permission_id";
        }

        public static class Permission {
            public static final String TABLE_NAME = "permission";
        }

        public static class PermissionTemplate {
            public static final String TABLE_NAME = "permission_template";
            public static final String CODE = "code";
        }

        public static class ScopeRoleUserX {
            public static final String TABLE_NAME = "scope_role_user_x";
        }

        public static class ScopeUserGroupUserX {
            public static final String TABLE_NAME = "scope_user_group_user_x";
        }

    }
}
