package com.wuxian.janus.struct.prototype.layer1;

import com.wuxian.janus.struct.annotation.PropertyRemark;
import com.wuxian.janus.struct.prototype.AccessPrototype;
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
public class UserGroupOtherXPrototype<ID, UID> extends AccessPrototype<ID, UID> {

    public void fill(ID id, ID userGroupId
            , Boolean viewAccess, Boolean executeAccess, Boolean editAccess, Boolean deleteAccess, Boolean enableAccess
            , UID createdBy, Date createdDate, UID lastModifiedBy, Date lastModifiedDate, Integer version) {
        setId(id);
        setUserGroupId(userGroupId);

        PrototypeUtils.fillAccess(this, viewAccess, executeAccess, editAccess, deleteAccess, enableAccess);

        PrototypeUtils.fill(this,createdBy,createdDate,lastModifiedBy,lastModifiedDate);
        setVersion(version);
    }

    private static final long serialVersionUID = 1L;

    @PropertyRemark(value = "用户组id", example = "1")
    private ID userGroupId;

    @PropertyRemark(value = "加入访问权:能否加入", example = "true")
    private boolean executeAccess;

    @PropertyRemark(value = "乐观锁", example = "998")
    private Integer version;
}
