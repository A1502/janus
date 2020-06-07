package com.wuxian.janus.core.calculate;

import com.wuxian.janus.core.basis.StrictUtils;
import com.wuxian.janus.core.calculate.error.ErrorDataRecorder;
import com.wuxian.janus.core.calculate.error.ErrorInfoFactory;
import com.wuxian.janus.struct.layer1.OuterObjectStruct;
import com.wuxian.janus.struct.layer1.OuterObjectTypeStruct;
import com.wuxian.janus.struct.layer1.UserGroupStruct;
import com.wuxian.janus.struct.layer6.UserOuterObjectXStruct;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.UserIdType;
import com.wuxian.janus.core.cache.BaseOuterObjectTypeCache;
import com.wuxian.janus.core.cache.BaseOuterObjectTypeCachePool;
import com.wuxian.janus.core.critical.NativeUserGroupEnum;
import com.wuxian.janus.core.index.UserGroupMap;

import java.util.*;

public class UserGroupUtils {
    static List<UserGroupStruct> getExecuteAccessOuterUserGroups(UserIdType userId,
                                                                 List<String> scopes,
                                                                 BaseOuterObjectTypeCachePool outerObjectCache,
                                                                 UserGroupMap userGroupSource,
                                                                 ErrorDataRecorder recorder) {

        Map<IdType, UserGroupStruct> resultMap = new HashMap<>();


        List<OuterObjectTypeStruct> outerUserGroupTypes = outerObjectCache
                .getOuterObjectTypeMap()
                .getByUsedByUserGroup(true);

        for (OuterObjectTypeStruct type : outerUserGroupTypes) {

            BaseOuterObjectTypeCache outerObjectTypeCache = outerObjectCache.getOuterObjectTypeCache(new IdType(type.getId()));

            List<UserGroupStruct> userGroupList = getExecuteAccessOuterUserGroups(userId,
                    scopes,
                    outerObjectTypeCache,
                    userGroupSource,
                    recorder);

            //在resultMap中收集所有id不重复的UserGroupStruct
            for (UserGroupStruct userGroupStruct : userGroupList) {
                if (!StrictUtils.containsKey(resultMap, new IdType(userGroupStruct.getId()))) {
                    resultMap.put(new IdType(userGroupStruct.getId()), userGroupStruct);
                }
            }
        }
        //转为list返回
        return new ArrayList<>(resultMap.values());
    }

    static List<UserGroupStruct> getExecuteAccessOuterUserGroups(UserIdType userId,
                                                                 List<String> scopes,
                                                                 BaseOuterObjectTypeCache outerObjectTypeCache,
                                                                 UserGroupMap userGroupSource,
                                                                 ErrorDataRecorder recorder) {
        Map<IdType, UserGroupStruct> resultMap = new HashMap<>();

        //按userId查找数据
        List<UserOuterObjectXStruct> list = outerObjectTypeCache.getUserOuterObjectX().getByUserId(userId);

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
                ErrorInfoFactory.OuterObjectType.outerObjectTypeIdScopeUserIdNotUnique(recorder
                        , outerObjectTypeCache, scope, userId, list);
            }
        }

        //提取scope匹配的数据
        Set<IdType> groupIdList = new HashSet<>();

        for (UserOuterObjectXStruct userOuterObjectX : list) {

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
                    ErrorInfoFactory.UserOuterObjectX.outerGroupIdListInvalid(recorder
                            , userOuterObjectX, groupIdStringList, groupIdString);
                    continue;
                }

                //通过groupId找OuterObject
                OuterObjectStruct outerObjectStruct = outerObjectTypeCache.getOuterObject().getByKey(groupId);
                if (outerObjectStruct == null) {
                    ErrorInfoFactory.UserOuterObjectX.outerGroupIdListNotMatch(recorder
                            , userOuterObjectX, groupIdStringList, groupId);
                    continue;
                }
                //校验下找到的OuterObjectTypeId和输入的type.getId是否匹配
                if (!StrictUtils.equals(outerObjectStruct.getOuterObjectTypeId(), userOuterObjectX.getOuterObjectTypeId())) {

                    ErrorInfoFactory.UserOuterObjectX.outerObjectTypeIdNotMatch(recorder
                            , userOuterObjectX, outerObjectStruct, groupIdStringList, groupId);
                    continue;
                }

                groupIdList.add(groupId);
            }
        }

        for (IdType groupId : groupIdList) {
            //在userGroup表中查找已经被作为外部用户组的数据
            List<UserGroupStruct> userGroupList = userGroupSource.getByOuterGroupId(groupId);

            //在resultMap中收集所有id不重复的UserGroupStruct
            for (UserGroupStruct userGroupStruct : userGroupList) {
                if (!StrictUtils.containsKey(resultMap, new IdType(userGroupStruct.getId()))) {
                    resultMap.put(new IdType(userGroupStruct.getId()), userGroupStruct);
                }
            }
        }
        //转为list返回
        return new ArrayList<>(resultMap.values());
    }

    public static Set<NativeUserGroupEnum> getNativeUserGroupEnums(List<UserGroupStruct> userGroups) {
        Set<NativeUserGroupEnum> result = new HashSet<>();
        for (UserGroupStruct userGroupStruct : userGroups) {
            NativeUserGroupEnum nativeUserGroupEnum = NativeUserGroupEnum.getByCode(userGroupStruct.getCode());
            if (nativeUserGroupEnum != null) {
                result.add(nativeUserGroupEnum);
            }
        }
        return result;
    }

    public static Set<UserGroupStruct> getNativeUserGroups(List<UserGroupStruct> userGroups) {
        Map<NativeUserGroupEnum, UserGroupStruct> result = new HashMap<>();
        for (UserGroupStruct userGroupStruct : userGroups) {
            NativeUserGroupEnum nativeUserGroupEnum = NativeUserGroupEnum.getByCode(userGroupStruct.getCode());
            if (nativeUserGroupEnum != null && !StrictUtils.containsKey(result, nativeUserGroupEnum)) {
                result.put(nativeUserGroupEnum, userGroupStruct);
            }
        }
        return new HashSet<>(result.values());
    }
}
