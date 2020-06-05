package com.wuxian.janus.struct.prototype.layer1;

import com.wuxian.janus.struct.prototype.ControlPrototype;
import com.wuxian.janus.struct.annotation.PropertyRemark;
import com.wuxian.janus.struct.util.PrototypeUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

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
public class RoleUserGroupXPrototype<ID, UID> extends ControlPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    public void fill(ID id, ID roleId, ID userGroupId
            , Boolean viewAccess, Boolean executeAccess, Boolean editAccess, Boolean deleteAccess, Boolean enableAccess
            , Boolean viewControl, Boolean executeControl, Boolean editControl, Boolean deleteControl, Boolean enableControl
            , UID createdBy, Date createdDate, UID lastModifiedBy, Date lastModifiedDate, Integer version) {
        setId(id);
        setRoleId(roleId);
        setUserGroupId(userGroupId);

        PrototypeUtils.fillAccess(this, viewAccess, executeAccess, editAccess, deleteAccess, enableAccess);
        PrototypeUtils.fillControl(this, viewControl, executeControl, editControl, deleteControl, enableControl);


        PrototypeUtils.fill(this,createdBy,createdDate,lastModifiedBy,lastModifiedDate);
        setVersion(version);
    }

    @PropertyRemark(value = "角色id", example = "1")
    private ID roleId;

    @PropertyRemark(value = "用户组id", example = "1")
    private ID userGroupId;

    @PropertyRemark(value = "加入访问权:能否加入", example = "true")
    private boolean executeAccess;

    @PropertyRemark(value = "加入控制权:能否管理他人加入", example = "true")
    private boolean executeControl;

    @PropertyRemark(value = "乐观锁", example = "998")
    private Integer version;
}
