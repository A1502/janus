package com.wuxian.janus.core.index;

import com.wuxian.janus.entity.UserGroupEntity;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;

import java.util.List;

/**
 * @author wuxian
 */

public class UserGroupMap extends AutoFillMultipleIndexesMap<IdType, UserGroupEntity> {

    private static final String OUTER_GROUP_ID = "outerObjectId";


    public UserGroupMap(SourceLoader<IdType, UserGroupEntity> sourceLoader) {
        super(sourceLoader);
        this.createIndex(UserGroupEntity.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, OUTER_GROUP_ID);
    }

    public List<UserGroupEntity> getByOuterGroupId(IdType outerGroupId) {
        return this.getByCondition((UserGroupEntity) new UserGroupEntity()
                        .setOuterObjectId(outerGroupId.getValue()),
                OUTER_GROUP_ID);
    }
}