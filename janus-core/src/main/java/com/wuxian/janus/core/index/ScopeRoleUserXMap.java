package com.wuxian.janus.core.index;

import com.wuxian.janus.index.SimpleIndexesMap;
import com.wuxian.janus.index.SourceLoader;
import com.wuxian.janus.struct.layer1.ScopeRoleUserXStruct;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.UserIdType;

import java.util.List;

/**
 * @author wuxian
 */

public class ScopeRoleUserXMap extends SimpleIndexesMap<IdType, ScopeRoleUserXStruct> {

    private static final String USER_ID = "userId";

    public ScopeRoleUserXMap(SourceLoader<IdType, ScopeRoleUserXStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(ScopeRoleUserXStruct.class, USER_ID);
    }

    public List<ScopeRoleUserXStruct> getByUserId(UserIdType userId) {
        return this.getByCondition((ScopeRoleUserXStruct) new ScopeRoleUserXStruct()
                        .setUserId(userId.getValue()),
                USER_ID);
    }
}