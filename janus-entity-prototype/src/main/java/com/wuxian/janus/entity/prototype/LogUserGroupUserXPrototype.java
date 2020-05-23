package com.wuxian.janus.entity.prototype;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.base.ControlPrototype;
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
@TableName("log_user_group_user_x")
public class LogUserGroupUserXPrototype<ID, UID> extends ControlPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    @TableField("user_group_id")
    @ApiModelProperty(value = "用户组id", example = "1")
    private ID userGroupId;

    @TableField("user_id")
    @ApiModelProperty(value = "用户id", example = "1")
    private UID userId;
}
