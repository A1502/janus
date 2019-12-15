package com.wuxian.janus.core.index;

import com.wuxian.janus.entity.ScopeRoleUserXEntity;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.UserIdType;
import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;

import java.util.List;

/**
 * @author Solomon
 */

public class ScopeRoleUserXMap extends AutoFillMultipleIndexesMap<IdType, ScopeRoleUserXEntity> {

    private static final String USER_ID = "userId";

    public ScopeRoleUserXMap(SourceLoader<IdType, ScopeRoleUserXEntity> sourceLoader) {
        super(sourceLoader);
        this.createIndex(ScopeRoleUserXEntity.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, USER_ID);
    }

    public List<ScopeRoleUserXEntity> getByUserId(UserIdType userId) {
        return this.getByCondition((ScopeRoleUserXEntity) new ScopeRoleUserXEntity()
                        .setUserId(userId.getValue()),
                USER_ID);
    }
}