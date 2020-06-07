package com.wuxian.janus.cache.model.source;


import lombok.Data;

@Data
public class Access {

    private boolean viewAccess;
    private boolean executeAccess;
    private boolean editAccess;
    private boolean deleteAccess;
    private boolean enableAccess;

    public Access() {
    }

    public Access(boolean viewAccess
            , boolean executeAccess
            , boolean editAccess
            , boolean deleteAccess
            , boolean enableAccess) {
        this.viewAccess = viewAccess;
        this.executeAccess = executeAccess;
        this.editAccess = editAccess;
        this.deleteAccess = deleteAccess;
        this.enableAccess = enableAccess;
    }
}
