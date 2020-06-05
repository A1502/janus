package com.wuxian.janus.entity.prototype.layer1;

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
 * 外部对象类型实体
 *
 * @author wuxian
 * @email
 * @date 2019/07/11
 */
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
@TableName("outer_object_type")
public class OuterObjectTypePrototype<ID, UID> extends JanusPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    public void fill(ID id
            , String code, String name, String referenceIdRemark, Boolean usedByUserGroup, Boolean usedByPermission
            , UID createdBy, Date createdDate, UID lastModifiedBy, Date lastModifiedDate) {
        setId(id);

        setCode(code);
        setName(name);
        setReferenceIdRemark(referenceIdRemark);
        setUsedByUserGroup(usedByUserGroup);
        setUsedByPermission(usedByPermission);

        setCreationProposer(createdBy);
        setModificationProposer(lastModifiedBy);
        setCreatedBy(createdBy);
        setCreatedDate(createdDate);
        setLastModifiedBy(lastModifiedBy);
        setLastModifiedDate(lastModifiedDate);
    }

    @TableField("code")
    @ApiModelProperty(value = "编码", example = "BOSS_ORG")
    private String code;

    @TableField("name")
    @ApiModelProperty(value = "名称", example = "组织")
    private String name;

    @TableField("reference_id_remark")
    @ApiModelProperty(value = "reference_id的组成类型", example = "application_id;tenant_id")
    private String referenceIdRemark;

    @TableField("used_by_user_group")
    @ApiModelProperty(value = "是否作为用户组的关联对象", example = "false")
    private boolean usedByUserGroup;

    @TableField("used_by_permission")
    @ApiModelProperty(value = "是否作为数据级权限的关联对象", example = "false")
    private boolean usedByPermission;

    @TableField("version")
    @ApiModelProperty(value = "乐观锁", example = "998")
    private Integer version;

}
