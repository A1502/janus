package com.wuxian.janus.struct.prototype.layer1;

import com.wuxian.janus.struct.prototype.JanusPrototype;
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
public class RolePrototype<ID, UID, AID, TID> extends JanusPrototype<ID, UID> {

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

        PrototypeUtils.fill(this,createdBy,createdDate,lastModifiedBy,lastModifiedDate);
        setVersion(version);
        return this;
    }

    @PropertyRemark(value = "应用id", example = "1")
    private AID applicationId;

    @PropertyRemark(value = "租户", example = "1")
    private TID tenantId;

    @PropertyRemark(value = "编码", example = "boss")
    private String code;

    @PropertyRemark(value = "操作级权限角色 = false，数据级权限角色 = true", example = "false")
    private Boolean multiple;

    @PropertyRemark(value = "名称", example = "基础配置平台")
    private String name;

    @PropertyRemark(value = "描述", example = "这是角色的描述...")
    private String description;

    @PropertyRemark(value = "状态", example = "true")
    private Boolean enable;

    @PropertyRemark(value = "绑定的权限模板id", example = "1")
    private ID permissionTemplateId;

    @PropertyRemark(value = "外部对象类型id", example = "1")
    private ID outerObjectTypeId;

    @PropertyRemark(value = "绑定的外部对象id", example = "1")
    private ID outerObjectId;

    @PropertyRemark(value = "绑定的外部对象的备注", example = "会议室对象:CQ811")
    private String outerObjectRemark;

    @PropertyRemark(value = "乐观锁", example = "998")
    private Integer version;
}
