package com.wuxian.janus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.first.UserGroupPrototype;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/11
 */
@TableName("role_permission_x")
public class UserGroupEntity extends UserGroupPrototype<Long, String, String, String> {
    //<ID, UID, AID, TID>
}