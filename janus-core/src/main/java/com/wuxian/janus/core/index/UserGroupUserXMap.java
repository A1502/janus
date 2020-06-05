package com.wuxian.janus.core.index;

import com.wuxian.janus.struct.layer1.UserGroupUserXStruct;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.UserIdType;
import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;

import java.util.List;

/**
 * @author wuxian
 */

public class UserGroupUserXMap extends AutoFillMultipleIndexesMap<IdType, UserGroupUserXStruct> {

    private static final String USER_ID = "userId";
    private static final String EXECUTE_ACCESS = "executeAccess";

    public UserGroupUserXMap(SourceLoader<IdType, UserGroupUserXStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(UserGroupUserXStruct.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0]),
                        safeToString(fields[1])
                }, USER_ID, EXECUTE_ACCESS);
    }

    public List<UserGroupUserXStruct> getByUserIdExecuteAccess(UserIdType userId, boolean executeAccess) {
        return this.getByCondition((UserGroupUserXStruct) new UserGroupUserXStruct()
                        .setUserId(userId.getValue())
                        .setExecuteAccess(executeAccess),
                USER_ID, EXECUTE_ACCESS);
    }
}
