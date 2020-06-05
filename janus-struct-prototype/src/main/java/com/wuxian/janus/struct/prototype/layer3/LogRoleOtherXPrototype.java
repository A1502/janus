package com.wuxian.janus.struct.prototype.layer3;

import com.wuxian.janus.struct.prototype.AccessPrototype;
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
public class LogRoleOtherXPrototype<ID, UID> extends AccessPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    @PropertyRemark(value = "角色id", example = "1")
    private ID roleId;

}
