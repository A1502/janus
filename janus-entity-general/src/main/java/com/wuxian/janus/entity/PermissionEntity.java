package com.wuxian.janus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.PermissionPrototype;

/**
 * Prototype类
 *
 * @author Solomon
 * @email
 * @date 2019/07/11
 */
@TableName("permission")
public class PermissionEntity extends PermissionPrototype<Long, String, String> {
    //<ID, UID, TID>
}
