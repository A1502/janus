package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.entity.RoleUserGroupXEntity;
import com.wuxian.janus.entity.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class RoleUserGroupXMap extends AutoFillMultipleIndexesMap<IdType, RoleUserGroupXEntity> {

    private static final String USER_GROUP_ID = "userGroupId";
    private static final String EXECUTE_ACCESS = "executeAccess";

    public RoleUserGroupXMap(SourceLoader<IdType, RoleUserGroupXEntity> sourceLoader) {
        super(sourceLoader);
        this.createIndex(RoleUserGroupXEntity.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0]),
                        safeToString(fields[1])
                }, USER_GROUP_ID, EXECUTE_ACCESS);
    }

    public List<RoleUserGroupXEntity> getByUserGroupIdExecuteAccess(IdType userGroupId, boolean executeAccess) {

        return this.getByCondition((RoleUserGroupXEntity) new RoleUserGroupXEntity()
                        .setUserGroupId(userGroupId.getValue())
                        .setExecuteAccess(executeAccess),
                USER_GROUP_ID, EXECUTE_ACCESS);
    }
}