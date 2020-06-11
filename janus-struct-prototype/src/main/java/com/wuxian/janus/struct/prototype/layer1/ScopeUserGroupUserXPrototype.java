package com.wuxian.janus.struct.prototype.layer1;

import com.wuxian.janus.struct.prototype.JanusPrototype;
import com.wuxian.janus.struct.annotation.PropertyRemark;
import com.wuxian.janus.struct.util.PrototypeUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
public class ScopeUserGroupUserXPrototype<ID, UID> extends JanusPrototype<ID, UID> {

    public void fill(ID id, String scope, ID userGroupId, UID userId
            , UID createdBy, Date createdDate, UID lastModifiedBy, Date lastModifiedDate) {
        setId(id);
        setScope(scope);
        setUserGroupId(userGroupId);
        setUserId(userId);

        PrototypeUtils.fill(this,createdBy,createdDate,lastModifiedBy,lastModifiedDate);
    }

    /**
    用户可以在不同的scope下加入角色和用户组。达到在不同scope下的权限不同效果。
     */
    @PropertyRemark(value = "范围", example = "null")
    private String scope;

    @PropertyRemark(value = "用户组id", example = "1")
    private ID userGroupId;

    @PropertyRemark(value = "用户id", example = "1")
    private UID userId;

    //无需version

}
