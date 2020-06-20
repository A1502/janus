package com.wuxian.janus.core.index;

import com.wuxian.janus.index.SimpleIndexesMap;
import com.wuxian.janus.index.SourceLoader;
import com.wuxian.janus.struct.layer2.RolePermissionXStruct;
import com.wuxian.janus.struct.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class RolePermissionXMap extends SimpleIndexesMap<IdType, RolePermissionXStruct> {

    private final static String ROLE_ID = "roleId";

    public RolePermissionXMap(SourceLoader<IdType, RolePermissionXStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(RolePermissionXStruct.class, ROLE_ID);
    }

    public List<RolePermissionXStruct> getByRoleId(IdType roleId) {
        return this.getByCondition((RolePermissionXStruct) new RolePermissionXStruct()
                        .setRoleId(roleId.getValue()),
                ROLE_ID);
    }
}