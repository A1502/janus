package com.wuxian.janus.struct.prototype;

import com.wuxian.janus.struct.annotation.PropertyRemark;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Setter
@Getter
public class JanusPrototype<ID, UID> {

    @PropertyRemark(value = "编号", example = "1")
    private ID id;

    @PropertyRemark(value = "创建人", example = "1")
    private UID createdBy;

    @PropertyRemark(value = "创建时间", example = "2019-10-10")
    private Date createdDate;

    @PropertyRemark(value = "最后修改人", example = "1")
    private UID lastModifiedBy;

    @PropertyRemark(value = "最后修改时间", example = "2019-10-10")
    private Date lastModifiedDate;

    @PropertyRemark(value = "创建发起人", example = "1")
    private UID creationProposer;

    @PropertyRemark(value = "修改发起人", example = "1")
    private UID modificationProposer;
}