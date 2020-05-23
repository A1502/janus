package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.entity.RoleOtherXEntity;
import com.wuxian.janus.entity.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class RoleOtherXMap extends AutoFillMultipleIndexesMap<IdType, RoleOtherXEntity> {

    private static final String EXECUTE_ACCESS = "executeAccess";

    public RoleOtherXMap(SourceLoader<IdType, RoleOtherXEntity> sourceLoader) {
        super(sourceLoader);
        this.createIndex(RoleOtherXEntity.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, EXECUTE_ACCESS);
    }

    public List<RoleOtherXEntity> getByExecuteAccess(boolean executeAccess) {
        return this.getByCondition((RoleOtherXEntity) new RoleOtherXEntity()
                        .setExecuteAccess(executeAccess),
                EXECUTE_ACCESS);
    }
}
