package com.wuxian.janus.struct.prototype.layer1;

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
 * @date 2019/07/09
 */
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
public class RolePrototype<ID, UID, AID, TID> extends JanusPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    public RolePrototype<ID, UID, AID, TID> fill(
            ID id, AID applicationId, TID tenantId, String code, Boolean multiple, String name, Boolean enable
            , ID permissionTemplateId, ID outerObjectId, String outerObjectRemark, String description
            , UID createdBy, Date createdDate, UID lastModifiedBy, Date lastModifiedDate, Integer version) {
        setId(id);
        setApplicationId(applicationId);
        setTenantId(tenantId);
        setCode(code);
        setMultiple(multiple);
        setName(name);
        setEnable(enable);
        setPermissionTemplateId(permissionTemplateId);
        setOuterObjectId(outerObjectId);
        setOuterObjectRemark(outerObjectRemark);
        setDescription(description);

        setCreationProposer(createdBy);
        setModificationProposer(lastModifiedBy);
        setCreatedBy(createdBy);
        setCreatedDate(createdDate);
        setLastModifiedBy(lastModifiedBy);
        setLastModifiedDate(lastModifiedDate);
        setVersion(version);
        return this;
    }

    @ApiModelProperty(value = "应用id", example = "1")
    private AID applicationId;

    @ApiModelProperty(value = "租户", example = "1")
    private TID tenantId;

    @ApiModelProperty(value = "编码", example = "boss")
    private String code;

    @ApiModelProperty(value = "操作级权限角色 = false，数据级权限角色 = true", example = "false")
    private Boolean multiple;

    @ApiModelProperty(value = "名称", example = "基础配置平台")
    private String name;

    @ApiModelProperty(value = "描述", example = "这是角色的描述...")
    private String description;

    @ApiModelProperty(value = "状态", example = "true")
    private Boolean enable;

    @ApiModelProperty(value = "绑定的权限模板id", example = "1")
    private ID permissionTemplateId;

    @ApiModelProperty(value = "外部对象类型id", example = "1")
    private ID outerObjectTypeId;

    @ApiModelProperty(value = "绑定的外部对象id", example = "1")
    private ID outerObjectId;

    @ApiModelProperty(value = "绑定的外部对象的备注", example = "会议室对象:CQ811")
    private String outerObjectRemark;

    @ApiModelProperty(value = "乐观锁", example = "998")
    private Integer version;
}
