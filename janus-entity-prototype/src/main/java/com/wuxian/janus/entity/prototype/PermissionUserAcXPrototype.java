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
@TableName("permission_user_ac_x")
public class PermissionUserAcXPrototype<ID, UID, TID> extends ControlPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    @TableField("user_id")
    @ApiModelProperty(value = "用户id", example = "1")
    private UID userId;

    @TableField("permission_id")
    @ApiModelProperty(value = "权限id", example = "1")
    private ID permissionId;

    @TableField("tenant_id")
    @ApiModelProperty(value = "租户id", example = "1")
    private TID tenantId;

    @TableField("execute_access")
    @ApiModelProperty(value = "打包访问权:能否把该权限添加到角色中", example = "true")
    private boolean executeAccess;

    @TableField("execute_control")
    @ApiModelProperty(value = "打包控制权:能否管理他人把该权限添加到角色中", example = "true")
    private boolean executeControl;

    @TableField("version")
    @ApiModelProperty(value = "乐观锁", example = "998")
    private Integer version;
}
