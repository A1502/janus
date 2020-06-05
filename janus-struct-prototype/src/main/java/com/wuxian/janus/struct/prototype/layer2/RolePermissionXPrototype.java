package com.wuxian.janus.struct.prototype.layer2;

import com.wuxian.janus.struct.prototype.JanusPrototype;
import com.wuxian.janus.struct.annotation.PropertyRemark;
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
public class RolePermissionXPrototype<ID, UID> extends JanusPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    public void fill(ID id, ID roleId, ID permissionId
            , UID createdBy, Date createdDate, UID lastModifiedBy
            , Date lastModifiedDate, Integer version) {
        setId(id);
        setRoleId(roleId);
        setPermissionId(permissionId);
        setVersion(version);

        setCreationProposer(createdBy);
        setModificationProposer(lastModifiedBy);
        setCreatedBy(createdBy);
        setCreatedDate(createdDate);
        setLastModifiedBy(lastModifiedBy);
        setLastModifiedDate(lastModifiedDate);
    }

    @PropertyRemark(value = "角色id", example = "1")
    private ID roleId;

    @PropertyRemark(value = "权限id", example = "1")
    private ID permissionId;

    @PropertyRemark(value = "乐观锁", example = "998")
    private Integer version;
}
