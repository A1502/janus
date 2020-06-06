package com.wuxian.janus.struct.prototype.layer4;

import com.wuxian.janus.struct.annotation.PropertyRemark;
import com.wuxian.janus.struct.prototype.ExpiryPrototype;
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
public class ExpUserGroupUserXPrototype<ID, UID> extends ExpiryPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    @PropertyRemark(value = "用户组id", example = "1")
    private Integer userGroupId;

    @PropertyRemark(value = "用户id", example = "1")
    private Integer userId;

}
