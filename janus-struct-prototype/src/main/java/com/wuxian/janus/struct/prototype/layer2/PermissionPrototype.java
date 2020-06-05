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
 * @date 2019/07/11
 */
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
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

    @PropertyRemark(value = "权限模板id", example = "1")
    private ID permissionTemplateId;

    @PropertyRemark(value = "租户id", example = "1")
    private TID tenantId;

    @PropertyRemark(value = "外部对象id", example = "1")
    private ID outerObjectId;

    @PropertyRemark(value = "外部对象备注", example = "这是备注文本...")
    private String outerObjectRemark;

    @PropertyRemark(value = "乐观锁", example = "998")
    private Integer version;
}
