package com.wuxian.janus.entity;import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.RolePrototype;

/**
 * Prototype类
 *
 * @author Solomon
 * @email
 * @date 2019/07/09
 */
@TableName("role_permission_x")
public class RoleEntity extends RolePrototype<Long, Long, Long, Long> {
    //<ID, UID, AID, TID>
}