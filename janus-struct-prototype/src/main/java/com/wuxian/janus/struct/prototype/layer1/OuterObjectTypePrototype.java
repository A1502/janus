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

        PrototypeUtils.fill(this,createdBy,createdDate,lastModifiedBy,lastModifiedDate);
    }

    @PropertyRemark(value = "编码", example = "BOSS_ORG")
    private String code;

    @PropertyRemark(value = "名称", example = "组织")
    private String name;

    @PropertyRemark(value = "reference_id的组成类型", example = "application_id;tenant_id")
    private String referenceIdRemark;

    /**
     * 这个标识可以过滤大量数据提高外部用户组查询性能
     */
    @PropertyRemark(value = "是否作为用户组的关联对象", example = "false")
    private boolean usedByUserGroup;

    @PropertyRemark(value = "是否作为数据级权限的关联对象", example = "false")
    private boolean usedByPermission;

    @PropertyRemark(value = "描述", example = "这是用户组的描述...")
    private String description;

    @PropertyRemark(value = "乐观锁", example = "998")
    private Integer version;

}
