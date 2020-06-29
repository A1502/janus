package com.wuxian.janus.core.critical;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class AccessControl extends Access {

    protected boolean viewControl;
    protected boolean executeControl;
    protected boolean editControl;
    protected boolean deleteControl;
    protected boolean enableControl;

    public AccessControl() {
    }

    public AccessControl(boolean viewAccess,
                         boolean executeAccess,
                         boolean editAccess,
                         boolean deleteAccess,
                         boolean enableAccess,
                         boolean viewControl,
                         boolean executeControl,
                         boolean editControl,
                         boolean deleteControl,
                         boolean enableControl) {
        super(viewAccess, executeAccess, editAccess, deleteAccess, enableAccess);
        this.viewControl = viewControl;
        this.executeControl = executeControl;
        this.editControl = editControl;
        this.deleteControl = deleteControl;
        this.enableControl = enableControl;
    }

    @SuppressWarnings("all")
    public AccessControl(boolean[] viewExecEditDelEnable) {
        super(viewExecEditDelEnable);
        if (viewExecEditDelEnable.length >= 6) {
            this.viewControl = viewExecEditDelEnable[5];
        }
        if (viewExecEditDelEnable.length >= 7) {
            this.executeControl = viewExecEditDelEnable[6];
        }
        if (viewExecEditDelEnable.length >= 8) {
            this.editControl = viewExecEditDelEnable[7];
        }
        if (viewExecEditDelEnable.length >= 9) {
            this.deleteControl = viewExecEditDelEnable[8];
        }
        if (viewExecEditDelEnable.length >= 10) {
            this.enableControl = viewExecEditDelEnable[9];
        }
    }

    public void union(AccessControl another) {
        super.union(another);
        this.viewControl = this.viewControl || another.viewControl;
        this.executeControl = this.executeControl || another.executeControl;
        this.editControl = this.editControl || another.editControl;
        this.deleteControl = this.deleteControl || another.deleteControl;
        this.enableControl = this.enableControl || another.enableControl;
    }

    @Override
    public String toString() {
        List<String> list = new ArrayList<>();

        String superString = super.toString();
        if (superString != null && superString.length() > 0) {
            list.add(superString);
        }

        if (this.viewControl) {
            list.add("viewControl");
        }
        if (this.executeControl) {
            list.add("executeControl");
        }
        if (this.editControl) {
            list.add("editControl");
        }
        if (this.deleteControl) {
            list.add("deleteControl");
        }
        if (this.enableControl) {
            list.add("enableControl");
        }

        String[] array = new String[list.size()];
        list.toArray(array);

        return String.join(",", array);
    }
}
