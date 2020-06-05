package com.wuxian.janus.struct.prototype.layer3;

import com.wuxian.janus.struct.prototype.ControlPrototype;
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
public class LogRoleUserGroupXPrototype<ID, UID> extends ControlPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id", example = "1")
    private ID roleId;

    @ApiModelProperty(value = "用户组id", example = "1")
    private ID userGroupId;

}
