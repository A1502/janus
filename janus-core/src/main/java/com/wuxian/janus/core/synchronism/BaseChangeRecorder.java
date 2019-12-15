package com.wuxian.janus.core.synchronism;

import java.util.function.Function;

public abstract class BaseChangeRecorder {

    protected ChangeStatus baseLine = new ChangeStatus();

    public abstract void accept(ChangeStatus input);

    public abstract void accept(Function<ChangeStatus, ChangeStatus> handler);
}
