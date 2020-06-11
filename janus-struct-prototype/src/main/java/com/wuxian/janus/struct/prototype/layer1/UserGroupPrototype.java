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
 * @date 2019/07/11
 */
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
public class UserGroupPrototype<ID, UID, AID, TID> extends JanusPrototype<ID, UID> {

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

        PrototypeUtils.fill(this,createdBy,createdDate,lastModifiedBy,lastModifiedDate);
        setVersion(version);
    }

    @PropertyRemark(value = "应用id", example = "1")
    private AID applicationId;

    @PropertyRemark(value = "租户", example = "1")
    private TID tenantId;

    @PropertyRemark(value = "编码", example = "boss")
    private String code;

    @PropertyRemark(value = "名称", example = "基础配置平台")
    private String name;

    @PropertyRemark(value = "状态", example = "true")
    private Boolean enable;

    @PropertyRemark(value = "描述", example = "这是用户组的描述...")
    private String description;

    @PropertyRemark(value = "绑定的外部对象id", example = "1")
    private ID outerObjectId;

    @PropertyRemark(value = "绑定的外部对象的备注", example = "会议室对象:CQ811")
    private String outerObjectRemark;

    @PropertyRemark(value = "乐观锁", example = "998")
    private Integer version;
}
