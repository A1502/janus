package com.wuxian.janus.entity.prototype.layer1;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.ControlPrototype;
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
@TableName("role_user_x")
public class RoleUserXPrototype<ID, UID> extends ControlPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    public void fill(ID id, ID roleId, UID userId
            , Boolean viewAccess, Boolean executeAccess, Boolean editAccess, Boolean deleteAccess, Boolean enableAccess
            , Boolean viewControl, Boolean executeControl, Boolean editControl, Boolean deleteControl, Boolean enableControl
            , UID createdBy, Date createdDate, UID lastModifiedBy, Date lastModifiedDate, Integer version) {
        setId(id);
        setRoleId(roleId);
        setUserId(userId);

        setViewAccess(viewAccess);
        setExecuteAccess(executeAccess);
        setEditAccess(editAccess);
        setDeleteAccess(deleteAccess);
        setEnableAccess(enableAccess);

        setViewControl(viewControl);
        setExecuteControl(executeControl);
        setEditControl(editControl);
        setDeleteControl(deleteControl);
        setEnableControl(enableControl);

        setCreationProposer(createdBy);
        setModificationProposer(lastModifiedBy);
        setCreatedBy(createdBy);
        setCreatedDate(createdDate);
        setLastModifiedBy(lastModifiedBy);
        setLastModifiedDate(lastModifiedDate);
        setVersion(version);
    }

    @TableField("role_id")
    @ApiModelProperty(value = "角色id", example = "1")
    private ID roleId;

    @TableField("user_id")
    @ApiModelProperty(value = "用户id", example = "1")
    private UID userId;

    @TableField("execute_access")
    @ApiModelProperty(value = "加入访问权:能否加入", example = "true")
    private boolean executeAccess;

    @TableField("execute_control")
    @ApiModelProperty(value = "加入控制权:能否管理他人加入", example = "true")
    private boolean executeControl;

    @TableField("version")
    @ApiModelProperty(value = "乐观锁", example = "998")
    private Integer version;
}
