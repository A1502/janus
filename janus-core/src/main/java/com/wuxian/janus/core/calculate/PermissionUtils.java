package com.wuxian.janus.core.calculate;

import com.wuxian.janus.core.cache.BaseOuterObjectTypeCachePool;
import com.wuxian.janus.core.calculate.error.ErrorDataRecorder;
import com.wuxian.janus.core.calculate.error.ErrorInfoFactory;
import com.wuxian.janus.core.critical.NativePermissionTemplateEnum;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.core.index.PermissionMap;
import com.wuxian.janus.core.index.PermissionTemplateMap;
import com.wuxian.janus.core.index.RolePermissionXMap;
import com.wuxian.janus.struct.layer1.OuterObjectStruct;
import com.wuxian.janus.struct.layer1.OuterObjectTypeStruct;
import com.wuxian.janus.struct.layer1.RoleStruct;
import com.wuxian.janus.struct.layer2.PermissionStruct;
import com.wuxian.janus.struct.layer2.PermissionTemplateStruct;
import com.wuxian.janus.struct.layer2.RolePermissionXStruct;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.util.StrictUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class PermissionUtils {

    /**
     * 判定是否对具体outerObject有permissionTemplateIds的权限。
     *
     * @param permissionTemplateIds    多个权限模板
     * @param outerObjectId            【重要】可以为null，表示不限定outerObject。不为null表示具体一个outerObject
     * @param permissionPackage        权限搜索的范围
     * @param permissionTemplateSource 权限模板的范围（permissionTemplate的Id要在这个范围）
     * @return
     */
    static PermissionResult isPermitted(List<IdType> permissionTemplateIds, IdType outerObjectId,
                                        PermissionPackage permissionPackage, PermissionTemplateMap permissionTemplateSource) {

        PermissionResult result = new PermissionResult();

        Map<IdType, Boolean> nativeStatus = getNativeStatus(permissionTemplateIds, permissionTemplateSource);

        nativeStatus.forEach((id, status) -> {
            if (status == null) {
                result.getDetail().put(id, null);
            } else {
                if (permissionPackage.getHasAllPermission()) {
                    result.getDetail().put(id, true);
                } else {
                    if (permissionPackage.getHasAllCustomPermission() && !status) {
                        result.getDetail().put(id, true);
                    } else {
                        isPermitted(result.getDetail(), id, outerObjectId, permissionPackage.getPermissions());
                    }
                }
            }
        });
        return result;
    }

    /**
     * 判定是否对具体outerObject有permissionTemplateId的权限。
     *
     * @param result               存放判断结果
     * @param permissionTemplateId 具体的权限模板
     * @param outerObjectId        【重要】可以为null，表示不限定outerObject。不为null表示具体一个outerObject
     * @param permissionInfos      权限搜索的范围
     */
    static void isPermitted(Map<IdType, Boolean> result, IdType permissionTemplateId, IdType outerObjectId, List<PermissionInfo> permissionInfos) {
        for (PermissionInfo permissionInfo : permissionInfos) {
            if (permissionTemplateId.valueEquals(permissionInfo.getTemplate().getId())) {
                if (outerObjectId == null) {
                    result.put(permissionTemplateId, true);
                } else {
                    //在detail中要找到OuterObjectId才生效
                    boolean foundOuterObjectId = false;
                    for (PermissionDetail detail : permissionInfo.getPermissionDetails()) {
                        if (outerObjectId.valueEquals(detail.getOuterObject().getId())) {
                            foundOuterObjectId = true;
                            break;
                        }
                    }
                    if (foundOuterObjectId) {
                        result.put(permissionTemplateId, true);
                        break;
                    }
                }
            }
        }
        if (!StrictUtils.containsKey(result, permissionTemplateId)) {
            result.put(permissionTemplateId, false);
        }
    }

    static PermissionPackage getPermissionPackage(ExecuteAccessRolePackage rolePackage,
                                                  PermissionTemplateMap permissionTemplateSource,
                                                  RolePermissionXMap rolePermissionSource,
                                                  PermissionMap singlePermissionSource,
                                                  PermissionMap multiplePermissionSource,
                                                  BaseOuterObjectTypeCachePool outerObjectCache,
                                                  boolean useSinglePermissions,
                                                  boolean nativePermissionOnly,
                                                  ErrorDataRecorder recorder) {

        PermissionPackage result = new PermissionPackage();

        //由于getHasAllTenantCustomExecuteAccessRoles目前2019-9-18不会true，所以结果也不会true
        result.setHasAllCustomPermission(rolePackage.getExecuteAccessOfTenantCustomRoles());

        List<PermissionInfo> permissions = new ArrayList<>();
        rolePackage.getRoles().forEach(roleStruct -> {

            NativeRoleEnum nativeRole = NativeRoleEnum.getByCode(roleStruct.getCode());
            if (nativeRole != null) {
                if (nativeRole.getHasAllPermission()) {
                    result.setHasAllPermission(true);
                }
                merge(permissions, getNativePermissionInfo(nativeRole, permissionTemplateSource, singlePermissionSource, recorder));
            }

            if (!nativePermissionOnly) {
                merge(permissions, getPermissionInfo(roleStruct,
                        permissionTemplateSource,
                        rolePermissionSource,
                        useSinglePermissions ? singlePermissionSource : multiplePermissionSource,
                        !useSinglePermissions,
                        outerObjectCache,
                        recorder));
            }

        });

        result.getPermissions().addAll(permissions);
        return result;
    }

    /**
     * 返回true表示native,false表示非native（即custom),null表示不存在
     */
    private static Map<IdType, Boolean> getNativeStatus(List<IdType> permissionTemplateIds, PermissionTemplateMap permissionTemplateSource) {

        Map<IdType, Boolean> result = new HashMap<>();

        for (IdType permissionTemplateId : permissionTemplateIds) {
            PermissionTemplateStruct template = permissionTemplateSource.getByKey(permissionTemplateId);
            if (template != null) {
                result.put(permissionTemplateId, NativePermissionTemplateEnum.getByCode(template.getCode()) != null);
            } else {
                result.put(permissionTemplateId, null);
            }
        }
        return result;
    }

    private static void merge(List<PermissionInfo> container, List<PermissionInfo> input) {
        //container和input的template的Id相等的，把input的朝container合并
        for (PermissionInfo inputItem : input) {
            PermissionInfo containerItem = container.stream().filter(c ->
                    StrictUtils.equals(c.getTemplate().getId(), inputItem.getTemplate().getId()))
                    .findFirst().orElse(null);

            //merge的container(来自customer）权限理论上不会和input(来自native)的template一样
            //所以containerItem会一直为null
            if (containerItem != null) {
                //找到，就把内容直接合并
                containerItem.getPermissionDetails().addAll(inputItem.getPermissionDetails());

                //但是合并后，要对PermissionDetail按id去重复处理
                List<PermissionDetail> distinctDetails = containerItem.getPermissionDetails().stream().filter(
                        distinctBy(d -> d.getPermission().getId())
                ).collect(Collectors.toList());

                //更新为不重复的集合
                containerItem.setPermissionDetails(distinctDetails);
            } else {
                //找不到就整体加入到container
                container.add(inputItem);
            }
        }
    }

    private static <T, R> Predicate<T> distinctBy(Function<T, R> keyBuilder) {
        Set<Object> set = ConcurrentHashMap.newKeySet();
        return input -> set.add(keyBuilder.apply(input));
    }

    private static List<PermissionInfo> getPermissionInfo(RoleStruct role,
                                                          PermissionTemplateMap permissionTemplateSource,
                                                          RolePermissionXMap rolePermissionSource,
                                                          PermissionMap permissionSource,
                                                          boolean fillOuterObjectInfo,
                                                          BaseOuterObjectTypeCachePool outerObjectCache,
                                                          ErrorDataRecorder recorder) {

        List<PermissionInfo> result = new ArrayList<>();

        Map<IdType, List<PermissionStruct>> templateMap = new HashMap<>();

        List<RolePermissionXStruct> rolePermissionXStructs = rolePermissionSource.getByRoleId(new IdType(role.getId()));

        rolePermissionXStructs.forEach(rolePermissionXStruct ->
        {
            PermissionStruct permissionStruct = permissionSource.getByKey(new IdType(rolePermissionXStruct.getPermissionId()));
            if (permissionStruct == null) {
                ErrorInfoFactory.RolePermissionX.permissionIdNotMatch(recorder, rolePermissionXStruct);
            } else {
                if (!StrictUtils.containsKey(templateMap, new IdType(permissionStruct.getPermissionTemplateId()))) {
                    templateMap.put(new IdType(permissionStruct.getPermissionTemplateId()), new ArrayList<>());
                }
                StrictUtils.get(templateMap, new IdType(permissionStruct.getPermissionTemplateId())).add(permissionStruct);
            }
        });

        templateMap.forEach((templateId, permissions) -> {
            PermissionTemplateStruct templateStruct = permissionTemplateSource.getByKey(templateId);
            if (templateStruct == null) {
                ErrorInfoFactory.Permission.permissionIdNotMatch(recorder, templateId, permissions);
            } else {
                PermissionInfo permissionInfo = new PermissionInfo();
                permissionInfo.setTemplate(templateStruct);
                permissionInfo.setPermissionDetails(new ArrayList<>());

                for (PermissionStruct p : permissions) {
                    PermissionDetail detail = new PermissionDetail();
                    permissionInfo.getPermissionDetails().add(detail);
                    detail.setPermission(p);
                    if (fillOuterObjectInfo) {
                        OuterObjectStruct outerObjectStruct = outerObjectCache.getOuterObjectInstance(new IdType(p.getOuterObjectId()));
                        if (outerObjectStruct != null) {
                            detail.setOuterObject(outerObjectStruct);
                            OuterObjectTypeStruct outerObjectTypeStruct = outerObjectCache.getOuterObjectTypeMap()
                                    .getByKey(new IdType(outerObjectStruct.getOuterObjectTypeId()));
                            //outerObjectTypeStruct来自outerObjectCache必然存在，不会为null
                            detail.setOuterObjectType(outerObjectTypeStruct);
                        } else {
                            //do nothing,outerObjectStruct可以为null
                        }
                    }
                }
                result.add(permissionInfo);
            }
        });
        return result;
    }

    private static List<PermissionInfo> getNativePermissionInfo(NativeRoleEnum nativeRole,
                                                                PermissionTemplateMap permissionTemplateSource,
                                                                PermissionMap singlePermissionSource,
                                                                ErrorDataRecorder recorder) {
        List<PermissionInfo> result = new ArrayList<>();

        List<NativePermissionTemplateEnum> permissionTemplates = nativeRole.getPermissionTemplates();

        permissionTemplates.forEach(templatEnum -> {
            List<PermissionTemplateStruct> permissionTemplateStructs = permissionTemplateSource.getByCode(templatEnum.getCode());

            if (permissionTemplateStructs.size() != 1) {
                ErrorInfoFactory.PermissionTemplate.nativePermissionTemplateNotUnique(recorder
                        , templatEnum, permissionTemplateStructs);
            } else {
                PermissionTemplateStruct templateStruct = StrictUtils.get(permissionTemplateStructs, 0);

                List<PermissionStruct> permissionStructs = singlePermissionSource.getByPermissionTemplateId(new IdType(templateStruct.getId()));

                PermissionDetail detail = new PermissionDetail();
                if (permissionStructs.size() != 1) {
                    ErrorInfoFactory.Permission.permissionTemplateIdNotUnique(recorder, templateStruct, permissionStructs);
                } else {
                    detail.setPermission(StrictUtils.get(permissionStructs, 0));
                }

                PermissionInfo item = new PermissionInfo();
                item.setPermissionDetails(new ArrayList<>());
                item.getPermissionDetails().add(detail);
                item.setTemplate(templateStruct);
                result.add(item);
            }
        });
        return result;
    }
}
