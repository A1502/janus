package com.wuxian.janus.core.index;

import com.wuxian.janus.struct.layer6.UserOuterObjectXStruct;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.UserIdType;
import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;

import java.util.List;

/**
 * @author wuxian
 */

public class UserOuterObjectXMap extends SimpleIndexesMap<IdType, UserOuterObjectXStruct> {

    private static final String USER_ID = "userId";

    public UserOuterObjectXMap(SourceLoader<IdType, UserOuterObjectXStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(UserOuterObjectXStruct.class, USER_ID);
    }

    public List<UserOuterObjectXStruct> getByUserId(UserIdType userId) {
        return this.getByCondition((UserOuterObjectXStruct) new UserOuterObjectXStruct()
                        .setUserId(userId.getValue()),
                USER_ID);
    }
}