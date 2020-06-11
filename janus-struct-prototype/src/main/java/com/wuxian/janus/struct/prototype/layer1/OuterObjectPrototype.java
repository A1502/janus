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
 * 外部对象Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/11
 */
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
public class OuterObjectPrototype<ID, UID> extends JanusPrototype<ID, UID> {

    public void fill(ID id
            , ID outerObjectTypeId, String referenceId, String referenceCode, String referenceName
            , String referenceDescription, UID createdBy, Date createdDate, UID lastModifiedBy, Date lastModifiedDate) {

        setId(id);
        setOuterObjectTypeId(outerObjectTypeId);
        setReferenceId(referenceId);
        setReferenceCode(referenceCode);
        setReferenceName(referenceName);
        setReferenceDescription(referenceDescription);

        PrototypeUtils.fill(this,createdBy,createdDate,lastModifiedBy,lastModifiedDate);
    }

    @PropertyRemark(value = "外部对象类型id", example = "1")
    private ID outerObjectTypeId;

    @PropertyRemark(value = "外部引用id", example = "CN88800")
    private String referenceId;

    @PropertyRemark(value = "外部引用code", example = "China_Shanghai")
    private String referenceCode;

    @PropertyRemark(value = "外部引用名称", example = "中国上海市")
    private String referenceName;

    @PropertyRemark(value = "外部引用描述", example = "这是唯一识别外部数据的描述...")
    private String referenceDescription;

    //无需version
}
