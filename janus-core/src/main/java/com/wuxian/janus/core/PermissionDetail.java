package com.wuxian.janus.core;

import lombok.Data;
import com.wuxian.janus.entity.OuterObjectEntity;
import com.wuxian.janus.entity.OuterObjectTypeEntity;
import com.wuxian.janus.entity.PermissionEntity;

/**
 * @author Solomon
 */

@Data
public class PermissionDetail {
    PermissionEntity permission;
    OuterObjectEntity outerObject;
    OuterObjectTypeEntity outerObjectType;
}
