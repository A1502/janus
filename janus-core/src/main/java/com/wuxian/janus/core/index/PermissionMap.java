package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.struct.PermissionStruct;
import com.wuxian.janus.struct.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class PermissionMap extends AutoFillMultipleIndexesMap<IdType, PermissionStruct> {

    private static final String PERMISSION_TEMPLATE_ID = "permissionTemplateId";

    public PermissionMap(SourceLoader<IdType, PermissionStruct> sourceLoader) {
        super(sourceLoader);

        this.createIndex(PermissionStruct.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, PERMISSION_TEMPLATE_ID);
    }

    public List<PermissionStruct> getByPermissionTemplateId(IdType permissionTemplateId) {
        return this.getByCondition((PermissionStruct) new PermissionStruct()
                        .setPermissionTemplateId(permissionTemplateId.getValue()),
                PERMISSION_TEMPLATE_ID);
    }
}