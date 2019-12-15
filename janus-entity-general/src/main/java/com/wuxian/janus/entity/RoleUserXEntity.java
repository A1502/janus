package com.wuxian.janus.entity;import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.RoleUserXPrototype;

/**
 * Prototype类
 *
 * @author Solomon
 * @email
 * @date 2019/07/09
 */
@TableName("role_permission_x")
public class RoleUserXEntity extends RoleUserXPrototype<Long, String> {
    //<ID, UID>
}