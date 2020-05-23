package com.wuxian.janus.entity.prototype;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.base.JanusPrototype;
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
 * @date 2019/07/11
 */
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
@TableName("permission")
public class PermissionPrototype<ID, UID, TID> extends JanusPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    public void fill(ID id
            , ID permissionTemplateId, TID tenantId, ID outerObjectId, String outerObjectRemark
            , UID createdBy, Date createdDate, UID lastModifiedBy, Date lastModifiedDate, Integer version) {
        setId(id);

        setPermissionTemplateId(permissionTemplateId);
        setTenantId(tenantId);
        setOuterObjectId(outerObjectId);
        setOuterObjectRemark(outerObjectRemark);

        setCreationProposer(createdBy);
        setLastModifiedBy(lastModifiedBy);
        setCreatedBy(createdBy);
        setCreatedDate(createdDate);
        setLastModifiedBy(lastModifiedBy);
        setLastModifiedDate(lastModifiedDate);
        setVersion(version);
    }

    @TableField("permission_template_id")
    @ApiModelProperty(value = "权限模板id", example = "1")
    private ID permissionTemplateId;

    @TableField("tenant_id")
    @ApiModelProperty(value = "租户id", example = "1")
    private TID tenantId;

    @TableField("outer_object_id")
    @ApiModelProperty(value = "外部对象id", example = "1")
    private ID outerObjectId;

    @TableField("outer_object_remark")
    @ApiModelProperty(value = "外部对象备注", example = "这是备注文本...")
    private String outerObjectRemark;

    @TableField("version")
    @ApiModelProperty(value = "乐观锁", example = "998")
    private Integer version;
}
