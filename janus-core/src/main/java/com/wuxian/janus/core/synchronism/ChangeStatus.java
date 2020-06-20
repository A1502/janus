package com.wuxian.janus.core.synchronism;

import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import com.wuxian.janus.util.StrictUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ChangeStatus {

    //<editor-fold desc="属性存取">

    private boolean outerObjectTypeStatus = false;

    private boolean applicationIdTenantIdRangeStatus = false;

    public void setOuterObjectTypeStatus(boolean status) {
        outerObjectTypeStatus = status;
    }

    public boolean getOuterObjectTypeStatus() {
        return outerObjectTypeStatus;
    }

    public void setApplicationIdTenantIdRangeStatus(boolean status) {
        applicationIdTenantIdRangeStatus = status;
    }

    public boolean getApplicationIdTenantIdRangeStatus() {
        return applicationIdTenantIdRangeStatus;
    }

    //</editor-fold>

    //<editor-fold desc="OuterObjectTypeCacheStatus存取">

    private Map<IdType, Map<OuterObjectTypeCacheChangePart, Boolean>> outerObjectTypeCacheStatus = new HashMap<>();

    public void changeOuterObjectTypeCacheStatus(IdType outerObjectTypeId, OuterObjectTypeCacheChangePart part, boolean status) {
        if (!StrictUtils.containsKey(outerObjectTypeCacheStatus, outerObjectTypeId)) {
            outerObjectTypeCacheStatus.put(outerObjectTypeId, new HashMap<>());
        }
        StrictUtils.get(outerObjectTypeCacheStatus, outerObjectTypeId).put(part, status);
    }

    public void changeOuterObjectTypeCacheStatus(Map<IdType, List<OuterObjectTypeCacheChangePart>> outerObjectTypeCacheStatusMap
            , boolean status) {
        for (IdType outerObjectTypeId : outerObjectTypeCacheStatusMap.keySet()) {
            List<OuterObjectTypeCacheChangePart> changeParts = StrictUtils.get(outerObjectTypeCacheStatusMap, outerObjectTypeId);
            if (changeParts.size() > 0) {
                if (!StrictUtils.containsKey(outerObjectTypeCacheStatus, outerObjectTypeId)) {
                    outerObjectTypeCacheStatus.put(outerObjectTypeId, new HashMap<>());
                }
                for (OuterObjectTypeCacheChangePart part : changeParts) {
                    StrictUtils.get(outerObjectTypeCacheStatus, outerObjectTypeId).put(part, status);
                }
            }
        }
    }

    public Map<IdType, List<OuterObjectTypeCacheChangePart>> fetchChangedOuterObjectTypeCache() {

        Map<IdType, List<OuterObjectTypeCacheChangePart>> result = new HashMap<>();

        for (IdType outerObjectTypeId : outerObjectTypeCacheStatus.keySet()) {
            Map<OuterObjectTypeCacheChangePart, Boolean> map = StrictUtils.get(outerObjectTypeCacheStatus, outerObjectTypeId);
            List<OuterObjectTypeCacheChangePart> truePartList = map.keySet().stream().filter(map::get).collect(Collectors.toList());
            if (truePartList.size() > 0) {
                result.put(outerObjectTypeId, truePartList);
            }
        }

        return result;
    }

    //</editor-fold>

    //<editor-fold desc="ApplicationStatus存取">

    private Map<ApplicationIdType, Boolean> permissionTemplateStatus = new HashMap<>();

    public void changeApplicationStatus(ApplicationIdType applicationId, boolean permissionTemplateStatus) {
        this.permissionTemplateStatus.put(applicationId, permissionTemplateStatus);
    }

    public List<ApplicationIdType> fetchChangedApplication() {
        return permissionTemplateStatus.keySet().stream().filter(permissionTemplateStatus::get).collect(Collectors.toList());
    }
    //</editor-fold>

    //<editor-fold desc="TenantStatus存取">

    private class TenantChangePartMap {
        private ApplicationIdType applicationId;
        private TenantIdType tenantId;
        private Map<TenantChangePart, Boolean> value = new HashMap<>();

        TenantChangePartMap(ApplicationIdType applicationId, TenantIdType tenantId) {
            this.applicationId = applicationId;
            this.tenantId = tenantId;
        }
    }

    public class TenantChangePartStatus {
        public final ApplicationIdType applicationId;
        public final TenantIdType tenantId;
        public final List<TenantChangePart> value = new ArrayList<>();

        private TenantChangePartStatus(ApplicationIdType applicationId, TenantIdType tenantId) {
            this.applicationId = applicationId;
            this.tenantId = tenantId;
        }
    }

    private Map<String, TenantChangePartMap> tenantStatus = new HashMap<>();

    private String joinId(ApplicationIdType applicationId, TenantIdType tenantId) {
        return applicationId + "," + tenantId;
    }

    public void changeTenantStatus(ApplicationIdType applicationId, TenantIdType tenantId, TenantChangePart part, boolean partStatus) {
        String key = joinId(applicationId, tenantId);
        if (!StrictUtils.containsKey(tenantStatus, key)) {
            tenantStatus.put(key, new TenantChangePartMap(applicationId, tenantId));
        }
        StrictUtils.get(tenantStatus, key).value.put(part, partStatus);
    }

    public void changeTenantStatus(ApplicationIdType applicationId, TenantIdType tenantId, List<TenantChangePart> parts, boolean partStatus) {
        String key = joinId(applicationId, tenantId);
        if (!StrictUtils.containsKey(tenantStatus, key)) {
            tenantStatus.put(key, new TenantChangePartMap(applicationId, tenantId));
        }
        for (TenantChangePart part : parts) {
            StrictUtils.get(tenantStatus, key).value.put(part, partStatus);
        }
    }

    public List<TenantChangePartStatus> fetchChangedTenant() {
        List<TenantChangePartStatus> result = new ArrayList<>();

        for (TenantChangePartMap item : tenantStatus.values()) {
            List<TenantChangePart> changedParts = item.value
                    .keySet()
                    .stream()
                    .filter(item.value::get)
                    .collect(Collectors.toList());
            if (changedParts.size() > 0) {
                TenantChangePartStatus status = new TenantChangePartStatus(item.applicationId, item.tenantId);
                status.value.addAll(changedParts);
                result.add(status);
            }
        }
        return result;
    }

    //</editor-fold>

    public void accept(ChangeStatus other) {

        //applicationIdTenantIdRangeStatus
        this.applicationIdTenantIdRangeStatus = other.applicationIdTenantIdRangeStatus;

        //outerObjectTypeStatus
        this.outerObjectTypeStatus = other.outerObjectTypeStatus;

        //outerObjectTypeCacheStatus
        for (IdType outerObjectTypeId : other.outerObjectTypeCacheStatus.keySet()) {
            Map<OuterObjectTypeCacheChangePart, Boolean> reference =
                    StrictUtils.get(other.outerObjectTypeCacheStatus, outerObjectTypeId);
            if (!StrictUtils.containsKey(this.outerObjectTypeCacheStatus, outerObjectTypeId)) {
                this.outerObjectTypeCacheStatus.put(outerObjectTypeId, new HashMap<>());
            }
            Map<OuterObjectTypeCacheChangePart, Boolean> current = StrictUtils.get(this.outerObjectTypeCacheStatus, outerObjectTypeId);
            for (OuterObjectTypeCacheChangePart part : reference.keySet()) {
                current.put(part, StrictUtils.get(reference, part));
            }
        }

        //permissionTemplateStatus
        for (ApplicationIdType applicationId : other.permissionTemplateStatus.keySet()) {
            this.permissionTemplateStatus.put(applicationId, StrictUtils.get(other.permissionTemplateStatus, applicationId));
        }

        //tenantStatus
        for (String key : other.tenantStatus.keySet()) {
            TenantChangePartMap reference = StrictUtils.get(other.tenantStatus, key);
            if (!StrictUtils.containsKey(this.tenantStatus, key)) {
                tenantStatus.put(key
                        , new TenantChangePartMap(reference.applicationId, reference.tenantId));
            }
            TenantChangePartMap current = StrictUtils.get(tenantStatus, key);
            for (TenantChangePart part : reference.value.keySet()) {
                current.value.put(part, StrictUtils.get(reference.value, part));
            }
        }
    }
}
