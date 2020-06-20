package com.wuxian.janus.index;

import java.util.function.Function;

public class NamedConverter {
    private String name;
    private Function<Object, String> action;

    public NamedConverter(String name, Function<Object, String> action) {
        this.name = name;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Function<Object, String> getAction() {
        return action;
    }

    public void setAction(Function<Object, String> action) {
        this.action = action;
    }
}
