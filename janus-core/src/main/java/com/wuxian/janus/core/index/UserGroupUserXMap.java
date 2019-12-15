package com.wuxian.janus.core.index;

import com.wuxian.janus.entity.UserGroupUserXEntity;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.UserIdType;
import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;

import java.util.List;

/**
 * @author Solomon
 */

public class UserGroupUserXMap extends AutoFillMultipleIndexesMap<IdType, UserGroupUserXEntity> {

    private static final String USER_ID = "userId";
    private static final String EXECUTE_ACCESS = "executeAccess";

    public UserGroupUserXMap(SourceLoader<IdType, UserGroupUserXEntity> sourceLoader) {
        super(sourceLoader);
        this.createIndex(UserGroupUserXEntity.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0]),
                        safeToString(fields[1])
                }, USER_ID, EXECUTE_ACCESS);
    }

    public List<UserGroupUserXEntity> getByUserIdExecuteAccess(UserIdType userId, boolean executeAccess) {
        return this.getByCondition((UserGroupUserXEntity) new UserGroupUserXEntity()
                        .setUserId(userId.getValue())
                        .setExecuteAccess(executeAccess),
                USER_ID, EXECUTE_ACCESS);
    }
}
