package com.wuxian.janus.core.index;

import com.wuxian.janus.index.SimpleIndexesMap;
import com.wuxian.janus.index.SourceLoader;
import com.wuxian.janus.struct.layer2.PermissionStruct;
import com.wuxian.janus.struct.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class PermissionMap extends SimpleIndexesMap<IdType, PermissionStruct> {

    private static final String PERMISSION_TEMPLATE_ID = "permissionTemplateId";

    public PermissionMap(SourceLoader<IdType, PermissionStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(PermissionStruct.class, PERMISSION_TEMPLATE_ID);
    }

    public List<PermissionStruct> getByPermissionTemplateId(IdType permissionTemplateId) {
        return this.getByCondition((PermissionStruct) new PermissionStruct()
                        .setPermissionTemplateId(permissionTemplateId.getValue()),
                PERMISSION_TEMPLATE_ID);
    }
}