package com.wuxian.janus.entity.prototype.fourth;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.ExpiryPrototype;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/09
 */
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
@TableName("exp_role_user_group_x")
public class ExpRoleUserGroupXPrototype<ID, UID> extends ExpiryPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    @TableField("role_id")
    @ApiModelProperty(value = "角色id", example = "1")
    private UID roleId;

    @TableField("user_group_id")
    @ApiModelProperty(value = "用户组id", example = "1")
    private ID userGroupId;


}
