package com.wuxian.janus.core.index;

import com.wuxian.janus.struct.ScopeUserGroupUserXStruct;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.UserIdType;
import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;

import java.util.List;

/**
 * @author wuxian
 */

public class ScopeUserGroupUserXMap extends AutoFillMultipleIndexesMap<IdType, ScopeUserGroupUserXStruct> {

    private static final String USER_ID = "userId";

    public ScopeUserGroupUserXMap(SourceLoader<IdType, ScopeUserGroupUserXStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(ScopeUserGroupUserXStruct.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, USER_ID);
    }

    public List<ScopeUserGroupUserXStruct> getByUserId(UserIdType userId) {
        return this.getByCondition((ScopeUserGroupUserXStruct) new ScopeUserGroupUserXStruct()
                        .setUserId(userId.getValue()),
                USER_ID);
    }
}