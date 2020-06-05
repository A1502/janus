package com.wuxian.janus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.NoArgsConstructor;
import com.wuxian.janus.entity.prototype.layer4.ExpRoleUserGroupXPrototype;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/09
 */

@NoArgsConstructor
@TableName("exp_role_user_group_x")
public class ExpRoleUserGroupXEntity extends ExpRoleUserGroupXPrototype<Long, String> {
    //<ID, UID>
}
