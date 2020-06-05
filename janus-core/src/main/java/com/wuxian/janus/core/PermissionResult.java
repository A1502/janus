package com.wuxian.janus.core;

import lombok.Getter;
import com.wuxian.janus.struct.primary.IdType;

import java.util.HashMap;
import java.util.Map;

public class PermissionResult {
    @Getter
    private Map<IdType, Boolean> detail = new HashMap<>();

    public PermissionResult() {

    }

    public PermissionResult(IdType[] permissionTemplateIds, boolean result) {
        for (IdType permissionTemplateId : permissionTemplateIds) {
            detail.put(permissionTemplateId, result);
        }
    }
}
