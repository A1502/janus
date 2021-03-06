package com.wuxian.janus.core.index;

import com.wuxian.janus.index.SimpleIndexesMap;
import com.wuxian.janus.index.SourceLoader;
import com.wuxian.janus.struct.layer1.RoleUserXStruct;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.UserIdType;

import java.util.List;

/**
 * @author wuxian
 */

public class RoleUserXMap extends SimpleIndexesMap<IdType, RoleUserXStruct> {

    private static final String USER_ID = "userId";
    private static final String EXECUTE_ACCESS = "executeAccess";

    public RoleUserXMap(SourceLoader<IdType, RoleUserXStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(RoleUserXStruct.class, USER_ID, EXECUTE_ACCESS);
    }

    public List<RoleUserXStruct> getByUserIdExecuteAccess(UserIdType userId, boolean executeAccess) {
        return this.getByCondition((RoleUserXStruct) new RoleUserXStruct()
                        .setUserId(userId.getValue())
                        .setExecuteAccess(executeAccess),
                USER_ID, EXECUTE_ACCESS);
    }
}