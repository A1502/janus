package com.wuxian.janus.entity;import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.PermissionDetailPrototype;

/**
 * Prototype类
 *
 * @author Solomon
 * @email
 * @date 2019/07/09
 */

@TableName("permission_detail")
public class PermissionDetailEntity extends PermissionDetailPrototype<Long, String> {
    //<ID, UID>
}
