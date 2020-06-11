package com.wuxian.janus.struct.prototype.layer3;

import com.wuxian.janus.struct.prototype.ControlPrototype;
import com.wuxian.janus.struct.annotation.PropertyRemark;
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

    @PropertyRemark(value = "角色id", example = "1")
    private ID roleId;

    @PropertyRemark(value = "用户组id", example = "1")
    private ID userGroupId;

}
