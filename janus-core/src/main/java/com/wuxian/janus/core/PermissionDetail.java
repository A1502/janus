package com.wuxian.janus.core;

import lombok.Data;
import com.wuxian.janus.struct.OuterObjectStruct;
import com.wuxian.janus.struct.OuterObjectTypeStruct;
import com.wuxian.janus.struct.PermissionStruct;

/**
 * @author wuxian
 */

@Data
public class PermissionDetail {
    PermissionStruct permission;
    OuterObjectStruct outerObject;
    OuterObjectTypeStruct outerObjectType;
}
