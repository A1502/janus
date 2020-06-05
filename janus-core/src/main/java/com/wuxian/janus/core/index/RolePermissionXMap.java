package com.wuxian.janus.core.index;

import com.wuxian.janus.struct.RolePermissionXStruct;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;

import java.util.List;

/**
 * @author wuxian
 */

public class RolePermissionXMap extends AutoFillMultipleIndexesMap<IdType, RolePermissionXStruct> {

    private final static String ROLE_ID = "roleId";

    public RolePermissionXMap(SourceLoader<IdType, RolePermissionXStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(RolePermissionXStruct.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, ROLE_ID);
    }

    public List<RolePermissionXStruct> getByRoleId(IdType roleId) {
        return this.getByCondition((RolePermissionXStruct) new RolePermissionXStruct()
                        .setRoleId(roleId.getValue()),
                ROLE_ID);
    }
}