package com.wuxian.janus.core.critical;

import com.wuxian.janus.core.basis.StrictUtils;
import lombok.Getter;

@Getter
@SuppressWarnings("all")
public enum LevelEnum {

    /**
     * 完全
     */
    FULL(true, true, true, true, true,//v,exec,e,del,en
            //ctrl
            true, true, true, true, true),

    /**
     * 除了自己不execute，其他访问控制能力都具备
     */
    HYPO_FULL(true, false, true, true, true,//v,exec,e,del,en
            //ctrl
            true, true, true, true, true),

    /**
     * 除了删除其他都能
     */
    ZERO(true, true, true, false, true,//v,exec,e,del,en
            //ctrl
            true, true, true, false, true),

    /**
     * 不能删不能禁用
     */
    ONE(true, true, true, false, false,//v,exec,e,del,en
            //ctrl
            true, true, true, false, false),

    /**
     * 自己用，也决定别人能否用
     */
    TWO(true, true, false, false, false,//v,exec,e,del,en
            //ctrl
            true, true, false, false, false),

    /**
     * 自己不用，只决定别人能否用
     */
    TWO_POINT_FIVE(true, false, false, false, false,//v,exec,e,del,en
            //ctrl
            true, true, false, false, false),

    /**
     * 仅自己用
     */
    THREE(true, true, false, false, false,//v,exec,e,del,en
            //ctrl
            false, false, false, false, false),

    /**
     * 看看而已
     */
    FOUR(true, false, false, false, false,//v,exec,e,del,en
            //ctrl
            false, false, false, false, false),

    /**
     * 没有权限
     */
    NONE(false, false, false, false, false,//v,exec,e,del,en
            //ctrl
            false, false, false, false, false);

    private boolean viewAccess;
    private boolean executeAccess;
    private boolean editAccess;
    private boolean deleteAccess;
    private boolean enableAccess;
    private boolean viewControl;
    private boolean executeControl;
    private boolean editControl;
    private boolean deleteControl;
    private boolean enableControl;

    LevelEnum(boolean viewAccess,
              boolean executeAccess,
              boolean editAccess,
              boolean deleteAccess,
              boolean enableAccess,
              boolean viewControl,
              boolean executeControl,
              boolean editControl,
              boolean deleteControl,
              boolean enableControl) {
        this.viewAccess = viewAccess;
        this.executeAccess = executeAccess;
        this.editAccess = editAccess;
        this.deleteAccess = deleteAccess;
        this.enableAccess = enableAccess;
        this.viewControl = viewControl;
        this.executeControl = executeControl;
        this.editControl = editControl;
        this.deleteControl = deleteControl;
        this.enableControl = enableControl;
    }

    public AccessControl toAccessControl() {
        AccessControl result = new AccessControl(this.viewAccess
                , this.executeAccess
                , this.editAccess
                , this.deleteAccess
                , this.enableAccess
                , this.viewControl
                , this.executeControl
                , this.editControl
                , this.deleteControl
                , this.enableControl);
        return result;
    }

    /**
     * 判定是否兼容：
     * levle为null的属性忽略比较,即level任何一个不为null属性不相等视作没有compatible
     */
    public boolean compatibleWith(AccessControlMode mode) {
        if (mode.getViewAccess() != null && !StrictUtils.equals(mode.getViewAccess(), this.isViewAccess())) {
            return false;
        }
        if (mode.getExecuteAccess() != null && !StrictUtils.equals(mode.getExecuteAccess(), this.isExecuteAccess())) {
            return false;
        }
        if (mode.getEditAccess() != null && !StrictUtils.equals(mode.getEditAccess(), this.isEditAccess())) {
            return false;
        }
        if (mode.getDeleteAccess() != null && !StrictUtils.equals(mode.getDeleteAccess(), this.isDeleteAccess())) {
            return false;
        }
        if (mode.getEnableAccess() != null && !StrictUtils.equals(mode.getEnableAccess(), this.isEnableAccess())) {
            return false;
        }


        if (mode.getViewControl() != null && !StrictUtils.equals(mode.getViewControl(), this.isViewControl())) {
            return false;
        }
        if (mode.getExecuteControl() != null && !StrictUtils.equals(mode.getExecuteControl(), this.isExecuteControl())) {
            return false;
        }
        if (mode.getEditControl() != null && !StrictUtils.equals(mode.getEditControl(), this.isEditControl())) {
            return false;
        }
        if (mode.getDeleteControl() != null && !StrictUtils.equals(mode.getDeleteControl(), this.isDeleteControl())) {
            return false;
        }
        if (mode.getEnableControl() != null && !StrictUtils.equals(mode.getEnableControl(), this.isEnableControl())) {
            return false;
        }

        return true;
    }
}
