package com.wuxian.janus.core.calculate;

import com.wuxian.janus.core.critical.Access;
import com.wuxian.janus.core.critical.AccessControl;
import com.wuxian.janus.struct.prototype.AccessPrototype;
import com.wuxian.janus.struct.prototype.ControlPrototype;

public class AccessControlUtils {

    public static void fillByAccess(AccessPrototype accessPrototype, Access data) {
        accessPrototype.setViewAccess(data.isViewAccess());
        accessPrototype.setExecuteAccess(data.isExecuteAccess());
        accessPrototype.setEditAccess(data.isEditAccess());
        accessPrototype.setDeleteAccess(data.isDeleteAccess());
        accessPrototype.setEnableAccess(data.isEnableAccess());
    }

    public static boolean matchWithAccess(AccessPrototype prototype, Access access) {
        if (access.isViewAccess() != prototype.isViewAccess()) {
            return false;
        }
        if (access.isExecuteAccess() != prototype.isExecuteAccess()) {
            return false;
        }
        if (access.isEditAccess() != prototype.isEditAccess()) {
            return false;
        }
        if (access.isDeleteAccess() != prototype.isDeleteAccess()) {
            return false;
        }
        if (access.isEnableAccess() != prototype.isEnableAccess()) {
            return false;
        }
        return true;
    }

    public static void fillByAccessControl(ControlPrototype controlPrototype, AccessControl data) {
        fillByAccess(controlPrototype, data);

        controlPrototype.setViewControl(data.isViewControl());
        controlPrototype.setExecuteControl(data.isExecuteControl());
        controlPrototype.setEditControl(data.isEditControl());
        controlPrototype.setDeleteControl(data.isDeleteControl());
        controlPrototype.setEnableControl(data.isEnableControl());
    }

    public static boolean matchWithAccessControl(ControlPrototype prototype, AccessControl accessControl) {
        if (!matchWithAccess(prototype, accessControl)) {
            return false;
        }
        if (accessControl.isViewControl() != prototype.isViewControl()) {
            return false;
        }
        if (accessControl.isExecuteControl() != prototype.isExecuteControl()) {
            return false;
        }
        if (accessControl.isEditControl() != prototype.isEditControl()) {
            return false;
        }
        if (accessControl.isDeleteControl() != prototype.isDeleteControl()) {
            return false;
        }
        if (accessControl.isEnableControl() != prototype.isEnableControl()) {
            return false;
        }
        return true;
    }
}
