package com.wuxian.janus.struct.prototype.layer1;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.struct.prototype.JanusPrototype;
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
@TableName("user_group")
public class UserGroupPrototype<ID, UID, AID, TID> extends JanusPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    public void fill(ID id, AID applicationId, TID tenantId
            , String code, String name, Boolean enable, String description, ID outerObjectId
            , UID createdBy, Date createdDate, UID lastModifiedBy, Date lastModifiedDate, Integer version) {
        setId(id);
        setApplicationId(applicationId);
        setTenantId(tenantId);
        setCode(code);
        setName(name);
        setEnable(enable);
        setDescription(description);
        setOuterObjectId(outerObjectId);

        setCreationProposer(createdBy);
        setModificationProposer(lastModifiedBy);
        setCreatedBy(createdBy);
        setCreatedDate(createdDate);
        setLastModifiedBy(lastModifiedBy);
        setLastModifiedDate(lastModifiedDate);
        setVersion(version);
    }

    @TableField("application_id")
    @ApiModelProperty(value = "应用id", example = "1")
    private AID applicationId;

    @TableField("tenant_id")
    @ApiModelProperty(value = "租户", example = "1")
    private TID tenantId;

    @TableField("code")
    @ApiModelProperty(value = "编码", example = "boss")
    private String code;

    @TableField("name")
    @ApiModelProperty(value = "名称", example = "基础配置平台")
    private String name;

    @TableField("enable")
    @ApiModelProperty(value = "状态", example = "true")
    private Boolean enable;

    @TableField("description")
    @ApiModelProperty(value = "描述", example = "这是用户组的描述...")
    private String description;

    @TableField("outer_object_id")
    @ApiModelProperty(value = "外部对象id", example = "1")
    private ID outerObjectId;

    @TableField("version")
    @ApiModelProperty(value = "乐观锁", example = "998")
    private Integer version;
}
