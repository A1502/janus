package com.wuxian.janus;

public abstract class TestTemplate<T> {

    private T data;

    abstract protected T createData();

    protected T getData() {
        if (data == null) {
            data = createData();
        }
        return data;
    }
}

