package com.wuxian.janus.struct.prototype.layer1;

import com.wuxian.janus.struct.prototype.AccessPrototype;
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
public class RoleOtherXPrototype<ID, UID> extends AccessPrototype<ID, UID> {

    public void fill(ID id, ID roleId
            , Boolean viewAccess, Boolean executeAccess, Boolean editAccess, Boolean deleteAccess, Boolean enableAccess
            , UID createdBy, Date createdDate, UID lastModifiedBy, Date lastModifiedDate, Integer version) {
        setId(id);
        setRoleId(roleId);

        PrototypeUtils.fillAccess(this, viewAccess, executeAccess, editAccess, deleteAccess, enableAccess);

        PrototypeUtils.fill(this,createdBy,createdDate,lastModifiedBy,lastModifiedDate);
        setVersion(version);
    }

    private static final long serialVersionUID = 1L;

    @PropertyRemark(value = "角色id", example = "1")
    private ID roleId;

    @PropertyRemark(value = "加入访问权:能否加入", example = "true")
    private boolean executeAccess;

    @PropertyRemark(value = "乐观锁", example = "998")
    private Integer version;
}
