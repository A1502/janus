package com.wuxian.janus.core;

import lombok.Data;
import com.wuxian.janus.struct.layer1.OuterObjectStruct;
import com.wuxian.janus.struct.layer1.OuterObjectTypeStruct;
import com.wuxian.janus.struct.layer2.PermissionStruct;

/**
 * @author wuxian
 */

@Data
public class PermissionDetail {
    PermissionStruct permission;
    OuterObjectStruct outerObject;
    OuterObjectTypeStruct outerObjectType;
}
