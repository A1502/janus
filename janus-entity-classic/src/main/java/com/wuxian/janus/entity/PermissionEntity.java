package com.wuxian.janus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.layer2.PermissionPrototype;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/11
 */
@TableName("permission")
public class PermissionEntity extends PermissionPrototype<Long, Long, Long> {
    //<ID, UID, TID>
}
