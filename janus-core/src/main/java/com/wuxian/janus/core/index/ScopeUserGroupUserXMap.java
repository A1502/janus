package com.wuxian.janus.core.index;

import com.wuxian.janus.entity.ScopeUserGroupUserXEntity;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.UserIdType;
import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;

import java.util.List;

/**
 * @author Solomon
 */

public class ScopeUserGroupUserXMap extends AutoFillMultipleIndexesMap<IdType, ScopeUserGroupUserXEntity> {

    private static final String USER_ID = "userId";

    public ScopeUserGroupUserXMap(SourceLoader<IdType, ScopeUserGroupUserXEntity> sourceLoader) {
        super(sourceLoader);
        this.createIndex(ScopeUserGroupUserXEntity.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, USER_ID);
    }

    public List<ScopeUserGroupUserXEntity> getByUserId(UserIdType userId) {
        return this.getByCondition((ScopeUserGroupUserXEntity) new ScopeUserGroupUserXEntity()
                        .setUserId(userId.getValue()),
                USER_ID);
    }
}