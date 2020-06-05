package com.wuxian.janus.core;

import com.wuxian.janus.core.critical.NativePermissionTemplateEnum;
import com.wuxian.janus.struct.PermissionTemplateStruct;

public class PermissionTemplateUtils {

    public static PermissionTemplateStruct getNativePermissionTemplate(NativePermissionTemplateEnum permissionTemplate) {
        PermissionTemplateStruct struct = new PermissionTemplateStruct();

        struct.setCode(permissionTemplate.getCode());
        struct.setPermissionLevel(permissionTemplate.getLevel().getCode());
        struct.setPermissionType(permissionTemplate.getPermissionType());
        return struct;
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
