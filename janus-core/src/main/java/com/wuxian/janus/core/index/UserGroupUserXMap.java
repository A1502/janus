package com.wuxian.janus.core.index;

import com.wuxian.janus.index.SimpleIndexesMap;
import com.wuxian.janus.index.SourceLoader;
import com.wuxian.janus.struct.layer1.UserGroupUserXStruct;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.UserIdType;

import java.util.List;

/**
 * @author wuxian
 */

public class UserGroupUserXMap extends SimpleIndexesMap<IdType, UserGroupUserXStruct> {

    private static final String USER_ID = "userId";
    private static final String EXECUTE_ACCESS = "executeAccess";

    public UserGroupUserXMap(SourceLoader<IdType, UserGroupUserXStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(UserGroupUserXStruct.class, USER_ID, EXECUTE_ACCESS);
    }

    public List<UserGroupUserXStruct> getByUserIdExecuteAccess(UserIdType userId, boolean executeAccess) {
        return this.getByCondition((UserGroupUserXStruct) new UserGroupUserXStruct()
                        .setUserId(userId.getValue())
                        .setExecuteAccess(executeAccess),
                USER_ID, EXECUTE_ACCESS);
    }
}
