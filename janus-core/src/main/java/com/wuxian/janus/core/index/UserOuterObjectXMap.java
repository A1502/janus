package com.wuxian.janus.core.index;

import com.wuxian.janus.entity.UserOuterObjectXEntity;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.UserIdType;
import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;

import java.util.List;

/**
 * @author wuxian
 */

public class UserOuterObjectXMap extends AutoFillMultipleIndexesMap<IdType, UserOuterObjectXEntity> {

    private static final String USER_ID = "userId";

    public UserOuterObjectXMap(SourceLoader<IdType, UserOuterObjectXEntity> sourceLoader) {
        super(sourceLoader);
        this.createIndex(UserOuterObjectXEntity.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, USER_ID);
    }

    public List<UserOuterObjectXEntity> getByUserId(UserIdType userId) {
        return this.getByCondition((UserOuterObjectXEntity) new UserOuterObjectXEntity()
                        .setUserId(userId.getValue()),
                USER_ID);
    }
}