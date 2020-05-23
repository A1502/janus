package com.wuxian.janus.core.index;

import com.wuxian.janus.entity.RolePermissionXEntity;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;

import java.util.List;

/**
 * @author wuxian
 */

public class RolePermissionXMap extends AutoFillMultipleIndexesMap<IdType, RolePermissionXEntity> {

    private final static String ROLE_ID = "roleId";

    public RolePermissionXMap(SourceLoader<IdType, RolePermissionXEntity> sourceLoader) {
        super(sourceLoader);
        this.createIndex(RolePermissionXEntity.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, ROLE_ID);
    }

    public List<RolePermissionXEntity> getByRoleId(IdType roleId) {
        return this.getByCondition((RolePermissionXEntity) new RolePermissionXEntity()
                        .setRoleId(roleId.getValue()),
                ROLE_ID);
    }
}