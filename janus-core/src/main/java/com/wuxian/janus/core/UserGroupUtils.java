package com.wuxian.janus.core;

import com.wuxian.janus.entity.OuterObjectEntity;
import com.wuxian.janus.entity.OuterObjectTypeEntity;
import com.wuxian.janus.entity.UserGroupEntity;
import com.wuxian.janus.entity.UserOuterObjectXEntity;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.UserIdType;
import com.wuxian.janus.core.cache.BaseOuterObjectTypeCache;
import com.wuxian.janus.core.cache.BaseOuterObjectTypeCachePool;
import com.wuxian.janus.core.critical.NativeUserGroupEnum;
import com.wuxian.janus.core.index.UserGroupMap;

import java.util.*;

public class UserGroupUtils {
    static List<UserGroupEntity> getExecuteAccessOuterUserGroups(UserIdType userId,
                                                                 List<String> scopes,
                                                                 BaseOuterObjectTypeCachePool outerObjectCache,
                                                                 UserGroupMap userGroupSource,
                                                                 ErrorDataRecorder recorder) {

        Map<IdType, UserGroupEntity> resultMap = new HashMap<>();


        List<OuterObjectTypeEntity> outerUserGroupTypes = outerObjectCache
                .getOuterObjectTypeMap()
                .getByUsedByUserGroup(true);

        for (OuterObjectTypeEntity type : outerUserGroupTypes) {

            BaseOuterObjectTypeCache outerObjectTypeCache = outerObjectCache.getOuterObjectTypeCache(new IdType(type.getId()));

            List<UserGroupEntity> userGroupList = getExecuteAccessOuterUserGroups(userId,
                    scopes,
                    outerObjectTypeCache,
                    userGroupSource,
                    recorder);

            //在resultMap中收集所有id不重复的UserGroupEntity
            for (UserGroupEntity userGroupEntity : userGroupList) {
                if (!StrictUtils.containsKey(resultMap, new IdType(userGroupEntity.getId()))) {
                    resultMap.put(new IdType(userGroupEntity.getId()), userGroupEntity);
                }
            }
        }
        //转为list返回
        return new ArrayList<>(resultMap.values());
    }

    static List<UserGroupEntity> getExecuteAccessOuterUserGroups(UserIdType userId,
                                                                 List<String> scopes,
                                                                 BaseOuterObjectTypeCache outerObjectTypeCache,
                                                                 UserGroupMap userGroupSource,
                                                                 ErrorDataRecorder recorder) {
        Map<IdType, UserGroupEntity> resultMap = new HashMap<>();

        //按userId查找数据
        List<UserOuterObjectXEntity> list = outerObjectTypeCache.getUserOuterObjectX().getByUserId(userId);

        //group by scope后检查数量是否唯一
        Map<String, Integer> countMap = new HashMap<>();
        //Collectors.groupingBy当key为null，即scope为null会产生崩溃，所以下面手动计算countMap
        list.forEach(o -> {
            if (!StrictUtils.containsKey(countMap, o.getScope())) {
                countMap.put(o.getScope(), 1);
            } else {
                int count = StrictUtils.get(countMap, o.getScope());
                countMap.put(o.getScope(), count + 1);
            }
        });

        for (String scope : countMap.keySet()) {
            if (StrictUtils.get(countMap, scope) > 1) {
                recorder.add(ErrorDataRecorder.TableSchema.OuterObjectType.TABLE_NAME,
                        new IdType(null),
                        null,
                        null,
                        String.format("按 outerObjectTypeId = [%s] AND scope = [%s] AND userId = [%s] " +
                                        "条件应最多查到一笔记录，而实际是[%d]条",
                                outerObjectTypeCache.getOuterObjectTypeId(),
                                scope,
                                userId,
                                list.size()
                        )
                );
            }
        }


        //提取scope匹配的数据
        Set<IdType> groupIdList = new HashSet<>();

        for (UserOuterObjectXEntity userOuterObjectX : list) {

            //scopes == null时，表示不过滤，全部数据都要处理
            if (scopes != null && !scopes.contains(userOuterObjectX.getScope())) {
                continue;
            }

            //提取OuterGroupIdList,它的内容是一个逗号连接起来的多个groupId字符串
            String groupIdStringList = userOuterObjectX.getOuterObjectIdList();
            String[] groupIds = groupIdStringList.split(",");
            for (String groupIdString : groupIds) {
                IdType groupId;
                try {
                    //字符串转IdType
                    groupId = new IdType(null);
                    groupId.setStringValue(groupIdString);
                } catch (Exception e) {
                    recorder.add(ErrorDataRecorder.TableSchema.UserOuterObjectX.TABLE_NAME,
                            new IdType(userOuterObjectX.getId()),
                            ErrorDataRecorder.TableSchema.UserOuterObjectX.OUTER_GROUP_ID_LIST,
                            groupIdStringList,
                            String.format("当前列内容按逗号split之后的结果中[%s]无法转换为整数", groupIdString)
                    );
                    continue;
                }

                //通过groupId找OuterObject
                OuterObjectEntity outerObjectEntity = outerObjectTypeCache.getOuterObject().getByKey(groupId);
                if (outerObjectEntity == null) {
                    recorder.add(ErrorDataRecorder.TableSchema.UserOuterObjectX.TABLE_NAME,
                            new IdType(userOuterObjectX.getId()),
                            ErrorDataRecorder.TableSchema.UserOuterObjectX.OUTER_GROUP_ID_LIST,
                            groupIdStringList,
                            String.format("当前列内容按逗号split之后的结果中[%s]" +
                                            "在表" + ErrorDataRecorder.TableSchema.OuterObject.TABLE_NAME + "中找不到对应一行数据",
                                    groupId
                            )
                    );
                    continue;
                }
                //校验下找到的OuterObjectTypeId和输入的type.getId是否匹配
                if (!StrictUtils.equals(outerObjectEntity.getOuterObjectTypeId(), userOuterObjectX.getOuterObjectTypeId())) {
                    recorder.add(ErrorDataRecorder.TableSchema.UserOuterObjectX.TABLE_NAME,
                            new IdType(userOuterObjectX.getId()),
                            ErrorDataRecorder.TableSchema.UserOuterObjectX.OUTER_GROUP_ID_LIST,
                            groupIdStringList,
                            String.format("当前列内容按逗号split之后的结果中[%s]" +
                                            "在表" + ErrorDataRecorder.TableSchema.OuterObject.TABLE_NAME +
                                            "中找到对应一行数据的列" + ErrorDataRecorder.TableSchema.OuterObject.OUTER_OBJECT_TYPE_ID + "的值[%s]" +
                                            "与当前数据的同名列的值[%s]不一致",
                                    groupId,
                                    outerObjectEntity.getOuterObjectTypeId(),
                                    userOuterObjectX.getOuterObjectTypeId()
                            )
                    );
                    continue;
                }

                groupIdList.add(groupId);
            }
        }

        for (IdType groupId : groupIdList) {
            //在userGroup表中查找已经被作为外部用户组的数据
            List<UserGroupEntity> userGroupList = userGroupSource.getByOuterGroupId(groupId);

            //在resultMap中收集所有id不重复的UserGroupEntity
            for (UserGroupEntity userGroupEntity : userGroupList) {
                if (!StrictUtils.containsKey(resultMap, new IdType(userGroupEntity.getId()))) {
                    resultMap.put(new IdType(userGroupEntity.getId()), userGroupEntity);
                }
            }
        }
        //转为list返回
        return new ArrayList<>(resultMap.values());
    }

    public static Set<NativeUserGroupEnum> getNativeUserGroupEnums(List<UserGroupEntity> userGroups) {
        Set<NativeUserGroupEnum> result = new HashSet<>();
        for (UserGroupEntity userGroupEntity : userGroups) {
            NativeUserGroupEnum nativeUserGroupEnum = NativeUserGroupEnum.getByCode(userGroupEntity.getCode());
            if (nativeUserGroupEnum != null) {
                result.add(nativeUserGroupEnum);
            }
        }
        return result;
    }

    public static Set<UserGroupEntity> getNativeUserGroups(List<UserGroupEntity> userGroups) {
        Map<NativeUserGroupEnum, UserGroupEntity> result = new HashMap<>();
        for (UserGroupEntity userGroupEntity : userGroups) {
            NativeUserGroupEnum nativeUserGroupEnum = NativeUserGroupEnum.getByCode(userGroupEntity.getCode());
            if (nativeUserGroupEnum != null && !StrictUtils.containsKey(result, nativeUserGroupEnum)) {
                result.put(nativeUserGroupEnum, userGroupEntity);
            }
        }
        return new HashSet<>(result.values());
    }
}
