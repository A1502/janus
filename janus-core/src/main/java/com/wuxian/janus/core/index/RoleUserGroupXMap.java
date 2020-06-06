package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.struct.layer1.RoleUserGroupXStruct;
import com.wuxian.janus.struct.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class RoleUserGroupXMap extends SimpleIndexesMap<IdType, RoleUserGroupXStruct> {

    private static final String USER_GROUP_ID = "userGroupId";
    private static final String EXECUTE_ACCESS = "executeAccess";

    public RoleUserGroupXMap(SourceLoader<IdType, RoleUserGroupXStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(RoleUserGroupXStruct.class, USER_GROUP_ID, EXECUTE_ACCESS);
    }

    public List<RoleUserGroupXStruct> getByUserGroupIdExecuteAccess(IdType userGroupId, boolean executeAccess) {

        return this.getByCondition((RoleUserGroupXStruct) new RoleUserGroupXStruct()
                        .setUserGroupId(userGroupId.getValue())
                        .setExecuteAccess(executeAccess),
                USER_GROUP_ID, EXECUTE_ACCESS);
    }
}