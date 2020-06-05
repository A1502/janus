package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.struct.layer1.RoleUserGroupXStruct;
import com.wuxian.janus.struct.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class RoleUserGroupXMap extends AutoFillMultipleIndexesMap<IdType, RoleUserGroupXStruct> {

    private static final String USER_GROUP_ID = "userGroupId";
    private static final String EXECUTE_ACCESS = "executeAccess";

    public RoleUserGroupXMap(SourceLoader<IdType, RoleUserGroupXStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(RoleUserGroupXStruct.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0]),
                        safeToString(fields[1])
                }, USER_GROUP_ID, EXECUTE_ACCESS);
    }

    public List<RoleUserGroupXStruct> getByUserGroupIdExecuteAccess(IdType userGroupId, boolean executeAccess) {

        return this.getByCondition((RoleUserGroupXStruct) new RoleUserGroupXStruct()
                        .setUserGroupId(userGroupId.getValue())
                        .setExecuteAccess(executeAccess),
                USER_GROUP_ID, EXECUTE_ACCESS);
    }
}