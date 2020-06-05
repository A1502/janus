package com.wuxian.janus.struct.layer2;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.struct.prototype.layer2.RolePermissionXPrototype;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/09
 */

@TableName("role_permission_x")
public class RolePermissionXStruct extends RolePermissionXPrototype<Long, String> {
    //<ID, UID>
}
