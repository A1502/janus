package com.wuxian.janus.entity.prototype.second;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.JanusPrototype;
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
@TableName("role_permission_x")
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

    @TableField("role_id")
    @ApiModelProperty(value = "角色id", example = "1")
    private ID roleId;

    @TableField("permission_id")
    @ApiModelProperty(value = "权限id", example = "1")
    private ID permissionId;

    @TableField("version")
    @ApiModelProperty(value = "乐观锁", example = "998")
    private Integer version;
}
