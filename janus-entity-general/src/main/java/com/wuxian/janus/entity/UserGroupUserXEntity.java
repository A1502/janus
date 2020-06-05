package com.wuxian.janus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.layer1.UserGroupUserXPrototype;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/09
 */
@TableName("role_permission_x")
public class UserGroupUserXEntity extends UserGroupUserXPrototype<Long, String> {
    //<ID, UID>
}
