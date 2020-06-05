package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.struct.layer1.UserGroupOtherXStruct;
import com.wuxian.janus.struct.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class UserGroupOtherXMap extends SimpleIndexesMap<IdType, UserGroupOtherXStruct> {

    private static final String EXECUTE_ACCESS = "executeAccess";

    public UserGroupOtherXMap(SourceLoader<IdType, UserGroupOtherXStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(UserGroupOtherXStruct.class, EXECUTE_ACCESS);
    }

    public List<UserGroupOtherXStruct> getByExecuteAccess(boolean executeAccess) {
        return this.getByCondition((UserGroupOtherXStruct) new UserGroupOtherXStruct()
                        .setExecuteAccess(executeAccess),
                EXECUTE_ACCESS);
    }
}
