package com.wuxian.janus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.RolePermissionXPrototype;

/**
 * Prototype类
 *
 * @author Solomon
 * @email
 * @date 2019/07/09
 */

@TableName("role_permission_x")
public class RolePermissionXEntity extends RolePermissionXPrototype<Long, String> {
    //<ID, UID>
}
