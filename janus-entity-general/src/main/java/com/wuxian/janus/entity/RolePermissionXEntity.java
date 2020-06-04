package com.wuxian.janus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.second.RolePermissionXPrototype;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/09
 */

@TableName("role_permission_x")
public class RolePermissionXEntity extends RolePermissionXPrototype<Long, String> {
    //<ID, UID>
}
