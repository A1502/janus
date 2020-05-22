package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.entity.PermissionEntity;
import com.wuxian.janus.entity.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class PermissionMap extends AutoFillMultipleIndexesMap<IdType, PermissionEntity> {

    private static final String PERMISSION_TEMPLATE_ID = "permissionTemplateId";

    public PermissionMap(SourceLoader<IdType, PermissionEntity> sourceLoader) {
        super(sourceLoader);

        this.createIndex(PermissionEntity.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, PERMISSION_TEMPLATE_ID);
    }

    public List<PermissionEntity> getByPermissionTemplateId(IdType permissionTemplateId) {
        return this.getByCondition((PermissionEntity) new PermissionEntity()
                        .setPermissionTemplateId(permissionTemplateId.getValue()),
                PERMISSION_TEMPLATE_ID);
    }
}