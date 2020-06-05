package com.wuxian.janus.core.index;

import com.wuxian.janus.struct.layer1.UserGroupStruct;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;

import java.util.List;

/**
 * @author wuxian
 */

public class UserGroupMap extends AutoFillMultipleIndexesMap<IdType, UserGroupStruct> {

    private static final String OUTER_GROUP_ID = "outerObjectId";


    public UserGroupMap(SourceLoader<IdType, UserGroupStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(UserGroupStruct.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, OUTER_GROUP_ID);
    }

    public List<UserGroupStruct> getByOuterGroupId(IdType outerGroupId) {
        return this.getByCondition((UserGroupStruct) new UserGroupStruct()
                        .setOuterObjectId(outerGroupId.getValue()),
                OUTER_GROUP_ID);
    }
}