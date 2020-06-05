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
public class PermissionTemplatePrototype<ID, UID, AID> extends JanusPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    public void fill(ID id, AID applicationId, String code, Boolean multiple, String name
            , String description, String permissionLevel, String permissionType, ID outerObjectTypeId
            , UID createdBy, Date createdDate, UID lastModifiedBy, Date lastModifiedDate, Integer version) {
        setApplicationId(applicationId);
        setCode(code);
        setMultiple(multiple);
        setName(name);
        setDescription(description);
        setPermissionLevel(permissionLevel);
        setPermissionType(permissionType);
        setOuterObjectTypeId(outerObjectTypeId);
        setVersion(version);
        setId(id);
        setCreatedBy(createdBy);
        setCreatedDate(createdDate);
        setLastModifiedBy(lastModifiedBy);
        setLastModifiedDate(lastModifiedDate);
    }

    @PropertyRemark(value = "应用id", example = "1")
    private AID applicationId;

    @PropertyRemark(value = "编码", example = "Student.Edit")
    private String code;

    /**
    操作级权限 = false，数据级权限 = true，数据级权限可以通过permission表的outerObjectId来指定一条数据
     */
    @PropertyRemark(value = "操作级权限 = false，数据级权限 = true", example = "false")
    private Boolean multiple;

    @PropertyRemark(value = "名称", example = "编辑学生")
    private String name;

    @PropertyRemark(value = "描述", example = "这是权限模板的描述...")
    private String description;

    @PropertyRemark(value = "权限敏感等级枚举,", example = "read|edit|delete|add|read_write|advanced_read|advanced_edit|advanced_delete|advanced_add|advanced_read_write")
    private String permissionLevel;

    @PropertyRemark(value = "自定义的权限分类", example = "navigation")
    private String permissionType;

    @PropertyRemark(value = "绑定的外部对象类型id", example = "1")
    private ID outerObjectTypeId;

    @PropertyRemark(value = "乐观锁", example = "998")
    private Integer version;
}
