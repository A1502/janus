package com.wuxian.janus.core.index;

import com.wuxian.janus.struct.layer1.ScopeRoleUserXStruct;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.UserIdType;
import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;

import java.util.List;

/**
 * @author wuxian
 */

public class ScopeRoleUserXMap extends AutoFillMultipleIndexesMap<IdType, ScopeRoleUserXStruct> {

    private static final String USER_ID = "userId";

    public ScopeRoleUserXMap(SourceLoader<IdType, ScopeRoleUserXStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(ScopeRoleUserXStruct.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, USER_ID);
    }

    public List<ScopeRoleUserXStruct> getByUserId(UserIdType userId) {
        return this.getByCondition((ScopeRoleUserXStruct) new ScopeRoleUserXStruct()
                        .setUserId(userId.getValue()),
                USER_ID);
    }
}