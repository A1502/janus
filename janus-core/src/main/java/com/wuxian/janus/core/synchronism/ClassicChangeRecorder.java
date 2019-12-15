package com.wuxian.janus.core.synchronism;

import java.util.function.Function;

public class ClassicChangeRecorder extends BaseChangeRecorder {

    @Override
    public void accept(ChangeStatus input) {
        synchronized (this) {
            this.baseLine.accept(input);
        }
    }

    @Override
    public void accept(Function<ChangeStatus, ChangeStatus> handler) {
        synchronized (this) {
            ChangeStatus status = handler.apply(baseLine);
            this.baseLine.accept(status);
        }
    }
}
