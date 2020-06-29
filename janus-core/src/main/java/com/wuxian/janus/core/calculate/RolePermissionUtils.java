package com.wuxian.janus.core.calculate;

public class RolePermissionUtils {

    private RolePermissionUtils() {
    }

    public static boolean relationAllowed(boolean multipleOfRole, Object outerObjectTypeIdOfRole, Object outerObjectIdOfRole
            , Object outerObjectTypeIdOfPermissionTemplate, Object outerObjectIdOfPermission) {

        return relationAllowedInner(multipleOfRole, outerObjectTypeIdOfRole, outerObjectIdOfRole
                , outerObjectTypeIdOfPermissionTemplate, outerObjectIdOfPermission);
    }

    public static boolean relationAllowed(boolean multipleOfRole, String outerObjectTypeCodeOfRole, String outerObjectCodeOfRole
            , String outerObjectTypeCodeOfPermissionTemplate, String outerObjectCodeOfPermission) {

        return relationAllowedInner(multipleOfRole, outerObjectTypeCodeOfRole, outerObjectCodeOfRole
                , outerObjectTypeCodeOfPermissionTemplate, outerObjectCodeOfPermission);
    }

    /**
     * 角色和权限是否可以关联
     *
     * @param multipleOfRole                      角色是否multiple
     * @param outerObjectTypeOfRole               角色的outerObjectType
     * @param outerObjectOfRole                   角色的outerObject
     * @param outerObjectTypeOfPermissionTemplate 权限模板的outerObjectType
     * @param outerObjectOfPermission             权限的outerObject
     * @return
     */
    private static <T> boolean relationAllowedInner(boolean multipleOfRole, T outerObjectTypeOfRole, T outerObjectOfRole
            , T outerObjectTypeOfPermissionTemplate, T outerObjectOfPermission) {
        if (multipleOfRole) {

            boolean outerObjectTypeAllowed;

            if (outerObjectTypeOfRole == null) {
                //outerObjectTypeCodeOfRole==null表示不限定outerObjectTypeCode范围，
                //只要outerObjectTypeCodeOfPermissionTemplate!=null即可
                //因为是multiple的Role,必须配multiple的permissionTemplate,也就是outerObjectTypeCodeOfPermission!=null
                outerObjectTypeAllowed = outerObjectTypeOfPermissionTemplate != null;
            } else {
                //outerObjectTypeCodeOfRole即有具体指定时
                //要求PermissionTemplate和Role的outerObjectTypeCode一致
                outerObjectTypeAllowed = outerObjectTypeOfRole.equals(outerObjectTypeOfPermissionTemplate);
            }

            //如果false，提前返回
            if (!outerObjectTypeAllowed) {
                return false;
            }

            if (outerObjectOfRole == null) {
                /*
                outerObjectCodeOfRole==null表示不限定outerObjectCode范围，
                所以outerObjectCodeOfPermission可以是任意

                特别注意outerObjectCodeOfPermission!=null这个条件没有必要
                outerObjectTypeCodeOfPermissionTemplate != null就已经保证了权限是multiple的
                所以下面直接return true;
                */
                return true;
            } else {
                //outerObjectCodeOfRole!=null即有具体指定时
                //要求Permission和Role的outerObjectCode一致
                return outerObjectOfRole.equals(outerObjectOfPermission);
            }
        } else {
            return outerObjectTypeOfPermissionTemplate == null
                    && outerObjectOfPermission == null;
        }
    }
}
