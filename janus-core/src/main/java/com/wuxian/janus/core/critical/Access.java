package com.wuxian.janus.core.critical;


import com.wuxian.janus.struct.prototype.AccessPrototype;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

    public void union(Access another) {
        this.viewAccess = this.viewAccess || another.viewAccess;
        this.executeAccess = this.executeAccess || another.executeAccess;
        this.editAccess = this.editAccess || another.editAccess;
        this.deleteAccess = this.deleteAccess || another.deleteAccess;
        this.enableAccess = this.enableAccess || another.enableAccess;
    }

    public void fill(AccessPrototype accessPrototype) {
        accessPrototype.setViewAccess(this.isViewAccess());
        accessPrototype.setExecuteAccess(this.isExecuteAccess());
        accessPrototype.setEditAccess(this.isEditAccess());
        accessPrototype.setDeleteAccess(this.isDeleteAccess());
        accessPrototype.setEnableAccess(this.isEnableAccess());
    }

    @Override
    public String toString() {
        List<String> list = new ArrayList();
        if (this.viewAccess) {
            list.add("viewAccess");
        }
        if (this.executeAccess) {
            list.add("executeAccess");
        }
        if (this.editAccess) {
            list.add("editAccess");
        }
        if (this.deleteAccess) {
            list.add("deleteAccess");
        }
        if (this.enableAccess) {
            list.add("enableAccess");
        }

        String[] array = new String[list.size()];
        list.toArray(array);

        return String.join(",", array);
    }
}
