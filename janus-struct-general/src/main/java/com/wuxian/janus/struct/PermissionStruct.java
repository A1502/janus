package com.wuxian.janus.struct;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.struct.prototype.layer2.PermissionPrototype;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/11
 */
@TableName("permission")
public class PermissionStruct extends PermissionPrototype<Long, String, String> {
    //<ID, UID, TID>
}
