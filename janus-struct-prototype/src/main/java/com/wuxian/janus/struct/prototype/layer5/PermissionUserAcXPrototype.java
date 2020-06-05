package com.wuxian.janus.struct.prototype.layer5;

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
public class PermissionUserAcXPrototype<ID, UID, TID> extends ControlPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    @PropertyRemark(value = "用户id", example = "1")
    private UID userId;

    @PropertyRemark(value = "权限id", example = "1")
    private ID permissionId;

    @PropertyRemark(value = "租户id", example = "1")
    private TID tenantId;

    @PropertyRemark(value = "打包访问权:能否把该权限添加到角色中", example = "true")
    private boolean executeAccess;

    @PropertyRemark(value = "打包控制权:能否管理他人把该权限添加到角色中", example = "true")
    private boolean executeControl;

    @PropertyRemark(value = "乐观锁", example = "998")
    private Integer version;
}
