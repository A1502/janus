package com.wuxian.janus.core;

import com.wuxian.janus.core.cache.BaseOuterObjectTypeCachePool;
import com.wuxian.janus.core.critical.NativePermissionTemplateEnum;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.core.index.PermissionMap;
import com.wuxian.janus.core.index.PermissionTemplateMap;
import com.wuxian.janus.core.index.RolePermissionXMap;
import com.wuxian.janus.entity.*;
import com.wuxian.janus.entity.primary.IdType;

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
        rolePackage.getRoles().forEach(roleEntity -> {

            NativeRoleEnum nativeRole = NativeRoleEnum.getByCode(roleEntity.getCode());
            if (nativeRole != null) {
                if (nativeRole.getHasAllPermission()) {
                    result.setHasAllPermission(true);
                }
                merge(permissions, getNativePermissionInfo(nativeRole, permissionTemplateSource, singlePermissionSource, recorder));
            }

            if (!nativePermissionOnly) {
                merge(permissions, getPermissionInfo(roleEntity,
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
            PermissionTemplateEntity template = permissionTemplateSource.getByKey(permissionTemplateId);
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

    private static List<PermissionInfo> getPermissionInfo(RoleEntity role,
                                                          PermissionTemplateMap permissionTemplateSource,
                                                          RolePermissionXMap rolePermissionSource,
                                                          PermissionMap permissionSource,
                                                          boolean fillOuterObjectInfo,
                                                          BaseOuterObjectTypeCachePool outerObjectCache,
                                                          ErrorDataRecorder recorder) {

        List<PermissionInfo> result = new ArrayList<>();

        Map<IdType, List<PermissionEntity>> templateMap = new HashMap<>();

        List<RolePermissionXEntity> rolePermissionXEntities = rolePermissionSource.getByRoleId(new IdType(role.getId()));

        rolePermissionXEntities.forEach(rolePermissionXEntity ->
        {
            PermissionEntity permissionEntity = permissionSource.getByKey(new IdType(rolePermissionXEntity.getPermissionId()));
            if (permissionEntity == null) {
                recorder.add(ErrorDataRecorder.TableSchema.RolePermissionX.TABLE_NAME
                        , new IdType(rolePermissionXEntity.getId())
                        , ErrorDataRecorder.TableSchema.RolePermissionX.PERMISSION_ID
                        , String.valueOf(rolePermissionXEntity.getPermissionId())
                        , String.format("当前列内容代表的permission在表%s中并不存在", ErrorDataRecorder.TableSchema.Permission.TABLE_NAME)
                );
            } else {
                if (!StrictUtils.containsKey(templateMap, new IdType(permissionEntity.getPermissionTemplateId()))) {
                    templateMap.put(new IdType(permissionEntity.getPermissionTemplateId()), new ArrayList<>());
                }
                StrictUtils.get(templateMap, new IdType(permissionEntity.getPermissionTemplateId())).add(permissionEntity);
            }
        });

        templateMap.forEach((templateId, permissions) -> {
            PermissionTemplateEntity templateEntity = permissionTemplateSource.getByKey(templateId);
            if (templateEntity == null) {
                String ids = String.join(",", permissions.stream().map(p -> String.valueOf(p.getId())).collect(Collectors.toList()));
                recorder.add(ErrorDataRecorder.TableSchema.Permission.TABLE_NAME
                        , ids
                        , ErrorDataRecorder.TableSchema.RolePermissionX.PERMISSION_ID
                        , String.valueOf(templateId)
                        , String.format("当前列内容代表的PermissionTemplate在表%s中并不存在", ErrorDataRecorder.TableSchema.PermissionTemplate.TABLE_NAME)
                );
            } else {
                PermissionInfo permissionInfo = new PermissionInfo();
                permissionInfo.setTemplate(templateEntity);
                permissionInfo.setPermissionDetails(new ArrayList<>());

                for (PermissionEntity p : permissions) {
                    PermissionDetail detail = new PermissionDetail();
                    permissionInfo.getPermissionDetails().add(detail);
                    detail.setPermission(p);
                    if (fillOuterObjectInfo) {
                        OuterObjectEntity outerObjectEntity = outerObjectCache.getOuterObjectInstance(new IdType(p.getOuterObjectId()));
                        if (outerObjectEntity != null) {
                            detail.setOuterObject(outerObjectEntity);
                            OuterObjectTypeEntity outerObjectTypeEntity = outerObjectCache.getOuterObjectTypeMap()
                                    .getByKey(new IdType(outerObjectEntity.getOuterObjectTypeId()));
                            //outerObjectTypeEntity来自outerObjectCache必然存在，不会为null
                            detail.setOuterObjectType(outerObjectTypeEntity);
                        } else {
                            //do nothing,outerObjectEntity可以为null
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
            List<PermissionTemplateEntity> permissionTemplateEntities = permissionTemplateSource.getByCode(templatEnum.getCode());

            if (permissionTemplateEntities.size() != 1) {
                recorder.add(ErrorDataRecorder.TableSchema.PermissionTemplate.TABLE_NAME, new IdType(null), ErrorDataRecorder.TableSchema.PermissionTemplate.CODE, String.valueOf(templatEnum.getCode()),
                        String.format("当前列内容代表的permissionTemplate在表%s中应该存在一笔，但是实际是%s条;",
                                ErrorDataRecorder.TableSchema.PermissionTemplate.TABLE_NAME,
                                String.valueOf(permissionTemplateEntities.size())
                        ));
            } else {
                PermissionTemplateEntity templateEntity = StrictUtils.get(permissionTemplateEntities, 0);

                List<PermissionEntity> permissionEntities = singlePermissionSource.getByPermissionTemplateId(new IdType(templateEntity.getId()));

                PermissionDetail detail = new PermissionDetail();
                if (permissionEntities.size() != 1) {
                    recorder.add(ErrorDataRecorder.TableSchema.Permission.TABLE_NAME,
                            new IdType(null),
                            null,
                            null,
                            String.format("按permissionTemplateId = %s 条件应查到一笔记录，而实际是[%d]条",
                                    String.valueOf(templateEntity.getId()),
                                    permissionEntities.size())
                    );
                } else {
                    detail.setPermission(StrictUtils.get(permissionEntities, 0));
                }

                PermissionInfo item = new PermissionInfo();
                item.setPermissionDetails(new ArrayList<>());
                item.getPermissionDetails().add(detail);
                item.setTemplate(templateEntity);
                result.add(item);
            }
        });
        return result;
    }
}
