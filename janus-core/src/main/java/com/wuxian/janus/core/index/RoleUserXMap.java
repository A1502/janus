package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.entity.RoleUserXEntity;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.UserIdType;

import java.util.List;

/**
 * @author Solomon
 */

public class RoleUserXMap extends AutoFillMultipleIndexesMap<IdType, RoleUserXEntity> {

    private static final String USER_ID = "userId";
    private static final String EXECUTE_ACCESS = "executeAccess";

    public RoleUserXMap(SourceLoader<IdType, RoleUserXEntity> sourceLoader) {
        super(sourceLoader);
        this.createIndex(RoleUserXEntity.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0]),
                        safeToString(fields[1])
                }, USER_ID, EXECUTE_ACCESS);
    }

    public List<RoleUserXEntity> getByUserIdExecuteAccess(UserIdType userId, boolean executeAccess) {
        return this.getByCondition((RoleUserXEntity) new RoleUserXEntity()
                        .setUserId(userId.getValue())
                        .setExecuteAccess(executeAccess),
                USER_ID, EXECUTE_ACCESS);
    }
}