package com.wuxian.janus.core.critical;


import lombok.Data;

@Data
public class Access {

    protected boolean viewAccess;
    protected boolean executeAccess;
    protected boolean editAccess;
    protected boolean deleteAccess;
    protected boolean enableAccess;

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

    @SuppressWarnings("all")
    public Access(boolean[] viewExecEditDelEnable) {
        if (viewExecEditDelEnable.length >= 1) {
            this.viewAccess = viewExecEditDelEnable[0];
        }
        if (viewExecEditDelEnable.length >= 2) {
            this.executeAccess = viewExecEditDelEnable[1];
        }
        if (viewExecEditDelEnable.length >= 3) {
            this.editAccess = viewExecEditDelEnable[2];
        }
        if (viewExecEditDelEnable.length >= 4) {
            this.deleteAccess = viewExecEditDelEnable[3];
        }
        if (viewExecEditDelEnable.length >= 5) {
            this.enableAccess = viewExecEditDelEnable[4];
        }
    }

    @Override
    public String toString() {
        String empty = "";
        return (this.viewAccess ? ("viewAccess") : empty)
                + (this.executeAccess ? ("," + "executeAccess") : empty)
                + (this.editAccess ? ("," + "editAccess") : empty)
                + (this.deleteAccess ? ("," + "deleteAccess") : empty)
                + (this.enableAccess ? ("," + "enableAccess") : empty);
    }
}
