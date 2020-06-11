package com.wuxian.janus.struct.prototype.layer6;

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
public class UserOuterObjectXPrototype<ID, UID> extends JanusPrototype<ID, UID> {

    public void fill(ID id, ID outerObjectTypeId, String scope, UID userId, String outerObjectIdList
            , UID createdBy, Date createdDate, UID lastModifiedBy, Date lastModifiedDate) {
        setId(id);
        setOuterObjectTypeId(outerObjectTypeId);
        setScope(scope);
        setUserId(userId);
        setOuterObjectIdList(outerObjectIdList);

        setCreationProposer(createdBy);
        setModificationProposer(lastModifiedBy);
        setCreatedBy(createdBy);
        setCreatedDate(createdDate);
        setLastModifiedBy(lastModifiedBy);
        setLastModifiedDate(lastModifiedDate);
    }

    @PropertyRemark(value = "外部对象类型id", example = "1")
    private ID outerObjectTypeId;

    /**
    和角色以及用户组的scope概念相同
     */
    @PropertyRemark(value = "范围", example = "null")
    private String scope;

    @PropertyRemark(value = "用户id", example = "1")
    private UID userId;

    @PropertyRemark(value = "外部对象id的集合，逗号连接多个", example = "1,2,3,4")
    private String outerObjectIdList;

    //无需version
}
