package com.wuxian.janus.cache.model.source;

import com.wuxian.janus.cache.model.ErrorFactory;
import com.wuxian.janus.cache.model.source.item.RoleItem;
import com.wuxian.janus.cache.model.source.item.UserGroupItem;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User implements RoleItem, UserGroupItem {

    @Getter
    private String id;

    @Getter
    private List<String> scopes = new ArrayList<>();

    //---------------------------------------------------------------------------------------------------------------------------------

    protected User() {
    }

    /**
     * 这个类中的byId重载确实是短参数依赖长参数，和其他model类是相反的;
     * 主要是因为scope的默认值为null的处理逻辑，如这个构造函数所示
     */
    public static User byId(String id) {
        return byId(id, (String) null);
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    public static User byId(String id, String... scope) {
        if (id == null) {
            throw ErrorFactory.createIdCannotBeNullError();
        }
        User result = new User();
        result.id = id;
        result.scopes.addAll(Arrays.asList(scope));
        return result;
    }
}
