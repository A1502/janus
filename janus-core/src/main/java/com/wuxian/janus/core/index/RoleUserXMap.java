package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.struct.layer1.RoleUserXStruct;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.UserIdType;

import java.util.List;

/**
 * @author wuxian
 */

public class RoleUserXMap extends AutoFillMultipleIndexesMap<IdType, RoleUserXStruct> {

    private static final String USER_ID = "userId";
    private static final String EXECUTE_ACCESS = "executeAccess";

    public RoleUserXMap(SourceLoader<IdType, RoleUserXStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(RoleUserXStruct.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0]),
                        safeToString(fields[1])
                }, USER_ID, EXECUTE_ACCESS);
    }

    public List<RoleUserXStruct> getByUserIdExecuteAccess(UserIdType userId, boolean executeAccess) {
        return this.getByCondition((RoleUserXStruct) new RoleUserXStruct()
                        .setUserId(userId.getValue())
                        .setExecuteAccess(executeAccess),
                USER_ID, EXECUTE_ACCESS);
    }
}