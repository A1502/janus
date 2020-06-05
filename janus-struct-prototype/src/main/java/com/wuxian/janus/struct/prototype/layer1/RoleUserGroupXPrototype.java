package com.wuxian.janus.struct.prototype.layer1;

import com.wuxian.janus.struct.prototype.ControlPrototype;
import io.swagger.annotations.ApiModelProperty;
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

        setViewAccess(viewAccess);
        setExecuteAccess(executeAccess);
        setEditAccess(editAccess);
        setDeleteAccess(deleteAccess);

        setViewControl(viewControl);
        setExecuteControl(executeControl);
        setEditControl(editControl);
        setDeleteControl(deleteControl);
        setEnableControl(enableControl);
        setEnableAccess(enableAccess);

        setCreationProposer(createdBy);
        setModificationProposer(lastModifiedBy);
        setCreatedBy(createdBy);
        setCreatedDate(createdDate);
        setLastModifiedBy(lastModifiedBy);
        setLastModifiedDate(lastModifiedDate);
        setVersion(version);
    }

    @ApiModelProperty(value = "角色id", example = "1")
    private ID roleId;

    @ApiModelProperty(value = "用户组id", example = "1")
    private ID userGroupId;

    @ApiModelProperty(value = "加入访问权:能否加入", example = "true")
    private boolean executeAccess;

    @ApiModelProperty(value = "加入控制权:能否管理他人加入", example = "true")
    private boolean executeControl;

    @ApiModelProperty(value = "乐观锁", example = "998")
    private Integer version;
}
