package com.wuxian.janus.core;

import com.wuxian.janus.core.critical.NativePermissionTemplateEnum;
import com.wuxian.janus.entity.PermissionTemplateEntity;

public class PermissionTemplateUtils {

    public static PermissionTemplateEntity getNativePermissionTemplate(NativePermissionTemplateEnum permissionTemplate) {
        PermissionTemplateEntity entity = new PermissionTemplateEntity();

        entity.setCode(permissionTemplate.getCode());
        entity.setPermissionLevel(permissionTemplate.getLevel().getCode());
        entity.setPermissionType(permissionTemplate.getPermissionType());
        return entity;
    }


    public static boolean relationAllowed(String outerObjectTypeCodeOfPermissionTemplate, String outerObjectTypeCodeOfPermission) {
        return relationAllowedInner(outerObjectTypeCodeOfPermissionTemplate, outerObjectTypeCodeOfPermission);
    }

    /**
     * 根据outerObjectType判定Permission和PermissionTemplate可否关联
     *
     * @param outerObjectTypeOfPermissionTemplate PermissionTemplate的outerObjectType
     * @param outerObjectTypeOfPermission         Permission的outerObjectType
     * @return 是否允许
     */
    private static <T> boolean relationAllowedInner(T outerObjectTypeOfPermissionTemplate, T outerObjectTypeOfPermission) {
        return outerObjectTypeOfPermissionTemplate == outerObjectTypeOfPermission;
    }
}
