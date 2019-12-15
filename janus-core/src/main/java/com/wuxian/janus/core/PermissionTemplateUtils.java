package com.wuxian.janus.core;

import com.wuxian.janus.entity.PermissionTemplateEntity;
import com.wuxian.janus.core.critical.NativePermissionTemplateEnum;

public class PermissionTemplateUtils {
    
    public static PermissionTemplateEntity getNativePermissionTemplate(NativePermissionTemplateEnum permissionTemplate) {
        PermissionTemplateEntity entity = new PermissionTemplateEntity();

        entity.setCode(permissionTemplate.getCode());
        entity.setPermissionLevel(permissionTemplate.getLevel().getCode());
        entity.setPermissionType(permissionTemplate.getPermissionType());
        return entity;
    }
}
