package com.wuxian.janus.core.cache.provider;

import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.basis.ReflectUtils;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.entity.*;
import com.wuxian.janus.entity.primary.ApplicationIdType;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.TenantIdType;
import lombok.Getter;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("all")
public class DirectAccessControlSource {

    //<editor-fold desc="属性">

    @Getter //1
    private TenantMap<IdType, ScopeRoleUserXEntity> scopeSingleRoleUserX
            = new TenantMap<>();

    @Getter //2
    private TenantMap<IdType, ScopeRoleUserXEntity> scopeMultipleRoleUserX
            = new TenantMap<>();

    @Getter //3
    private TenantMap<IdType, ScopeUserGroupUserXEntity> scopeUserGroupUserX
            = new TenantMap<>();

    @Getter //4
    private TenantMap<IdType, UserGroupEntity> userGroup
            = new TenantMap<>();

    @Getter //5
    private TenantMap<IdType, UserGroupUserXEntity> userGroupUserX
            = new TenantMap<>();

    @Getter //6
    private TenantMap<IdType, PermissionEntity> singlePermission
            = new TenantMap<>();

    @Getter //7
    private TenantMap<IdType, RolePermissionXEntity> singleRolePermissionX
            = new TenantMap<>();

    @Getter //8
    private TenantMap<IdType, RoleEntity> singleRole
            = new TenantMap<>();

    @Getter //9
    private TenantMap<IdType, RoleOtherXEntity> singleRoleOtherX
            = new TenantMap<>();

    @Getter //10
    private TenantMap<IdType, RoleUserGroupXEntity> singleRoleUserGroupX
            = new TenantMap<>();

    @Getter //11
    private TenantMap<IdType, RoleUserXEntity> singleRoleUserX
            = new TenantMap<>();

    @Getter //12
    private TenantMap<IdType, PermissionEntity> multiplePermission
            = new TenantMap<>();

    @Getter //13
    private TenantMap<IdType, RolePermissionXEntity> multipleRolePermissionX
            = new TenantMap<>();

    @Getter //14
    private TenantMap<IdType, RoleEntity> multipleRole
            = new TenantMap<>();

    @Getter //15
    private TenantMap<IdType, RoleOtherXEntity> multipleRoleOtherX
            = new TenantMap<>();

    @Getter //16
    private TenantMap<IdType, RoleUserGroupXEntity> multipleRoleUserGroupX
            = new TenantMap<>();

    @Getter //17
    private TenantMap<IdType, RoleUserXEntity> multipleRoleUserX
            = new TenantMap<>();

    @Getter //18
    private ApplicationMap<IdType, PermissionTemplateEntity> permissionTemplate
            = new ApplicationMap<>();

    @Getter //19
    private Map<IdType, OuterObjectTypeEntity> outerObjectType
            = new HashMap<>();

    @Getter //20
    private JanusMap<IdType, OuterObjectEntity> outerObject
            = new JanusMap<>();

    @Getter //21
    private JanusMap<IdType, UserOuterObjectXEntity> userOuterObjectX
            = new JanusMap<>();

    //</editor-fold>

    //<editor-fold desc="打印功能">

    public void print(PrintStream stream) {
        this.printer.print(stream);
    }

    private Printer printer = new Printer();

    private class Printer {

        private void print(PrintStream stream) {
            //1
            fullPrintTenantMap("[ ScopeSingleRoleUserX ]", stream, BLANK, scopeSingleRoleUserX, o -> print(stream, ScopeRoleUserXEntity.class, o));
            //2
            fullPrintTenantMap("[ ScopeMultipleRoleUserX ]", stream, BLANK, scopeMultipleRoleUserX, o -> print(stream, ScopeRoleUserXEntity.class, o));
            //3
            fullPrintTenantMap("[ ScopeUserGroupUserX ]", stream, BLANK, scopeUserGroupUserX, o -> print(stream, ScopeUserGroupUserXEntity.class, o));
            //4
            fullPrintTenantMap("[ UserGroup ]", stream, BLANK, userGroup, o -> print(stream, UserGroupEntity.class, o));
            //5
            fullPrintTenantMap("[ UserGroupUserX ]", stream, BLANK, userGroupUserX, o -> print(stream, UserGroupUserXEntity.class, o));
            //6
            fullPrintTenantMap("[ SinglePermission ]", stream, BLANK, singlePermission, o -> print(stream, PermissionEntity.class, o));
            //7
            fullPrintTenantMap("[ SingleRolePermissionX ]", stream, BLANK, singleRolePermissionX, o -> print(stream, RolePermissionXEntity.class, o));
            //8
            fullPrintTenantMap("[ SingleRole ]", stream, BLANK, singleRole, o -> print(stream, RoleEntity.class, o));
            //9
            fullPrintTenantMap("[ SingleRoleOtherX ]", stream, BLANK, singleRoleOtherX, o -> print(stream, RoleOtherXEntity.class, o));
            //10
            fullPrintTenantMap("[ SingleRoleUserGroupX ]", stream, BLANK, singleRoleUserGroupX, o -> print(stream, RoleUserGroupXEntity.class, o));
            //11
            fullPrintTenantMap("[ SingleRoleUserX ]", stream, BLANK, singleRoleUserX, o -> print(stream, RoleUserXEntity.class, o));
            //12
            fullPrintTenantMap("[ MultiplePermission ]", stream, BLANK, multiplePermission, o -> print(stream, PermissionEntity.class, o));
            //13
            fullPrintTenantMap("[ MultipleRolePermissionX ]", stream, BLANK, multipleRolePermissionX, o -> print(stream, RolePermissionXEntity.class, o));
            //14
            fullPrintTenantMap("[ MultipleRole ]", stream, BLANK, multipleRole, o -> print(stream, RoleEntity.class, o));
            //15
            fullPrintTenantMap("[ MultipleRoleOtherX ]", stream, BLANK, multipleRoleOtherX, o -> print(stream, RoleOtherXEntity.class, o));
            //16
            fullPrintTenantMap("[ multipleRoleUserGroupX ]", stream, BLANK, multipleRoleUserGroupX, o -> print(stream, RoleUserGroupXEntity.class, o));
            //17
            fullPrintTenantMap("[ MultipleRoleUserX ]", stream, BLANK, multipleRoleUserX, o -> print(stream, RoleUserXEntity.class, o));
            //18
            stream.print("[ PermissionTemplate ]");
            stream.println();
            printApplicationMap(stream, BLANK, permissionTemplate, o -> print(stream, PermissionTemplateEntity.class, o));
            stream.println();
            //19
            stream.print("[ OuterObjectType ]");
            stream.println();
            printMap(stream, BLANK, outerObjectType, o -> print(stream, OuterObjectTypeEntity.class, o));
            stream.println();
            //20
            stream.print("[ OuterObject ]");
            stream.println();
            printJanusMap(stream, BLANK, "outerObjectTypeId", outerObject, o -> print(stream, OuterObjectEntity.class, o));
            stream.println();
            //21
            stream.print("[ UserOuterObjectX ]");
            stream.println();
            printJanusMap(stream, BLANK, "outerObjectTypeId", userOuterObjectX, o -> print(stream, UserOuterObjectXEntity.class, o));
            stream.println();
        }

        private String BLANK = "    ";

        private <E> String print(PrintStream stream, Class<E> clazz, E input) {

            //即使是null也要显示的字段
            List<String> showNullFields = Arrays.asList("scope");

            List<Field> fields = ReflectUtils.getAllFields(clazz);

            List<String> fieldStringList = new ArrayList<>();
            for (Field field : fields) {
                if ("id".equals(field.getName()) || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                try {
                    Object data = ReflectUtils.findGetterMethodByFieldName(clazz, field.getName()).invoke(input);
                    if (data != null) {
                        fieldStringList.add(field.getName() + " = \"" + data.toString() + "\"");
                    } else {
                        if (showNullFields.contains(field.getName())) {
                            fieldStringList.add(field.getName() + " = null");
                        } else {
                            //do nothing 其他情况下null字段不显示
                        }
                    }
                } catch (Exception e) {
                    fieldStringList.add(field.getName() + " has exception : " + e.toString() + " ; ");
                }
            }
            return String.join(" & ", fieldStringList.toArray(new String[fieldStringList.size()]));
        }


        private <E> void fullPrintTenantMap(String title, PrintStream stream, String
                indent, TenantMap<IdType, E> tenantMap, Function<E, String> printEntityHandler) {
            stream.print(title);
            stream.println();
            printTenantMap(stream, indent, tenantMap, printEntityHandler);
            stream.println();
        }

        private <E> void printTenantMap(PrintStream stream, String
                indent, TenantMap<IdType, E> tenantMap, Function<E, String> printEntityHandler) {
            Map<ApplicationIdType, Set<TenantIdType>> ids = tenantMap.getIds();
            for (ApplicationIdType aid : ids.keySet()) {
                Set<TenantIdType> tids = StrictUtils.get(ids, aid);
                for (TenantIdType tid : tids) {
                    Map<IdType, E> elements = tenantMap.get(aid, tid);

                    stream.print(indent + "[ applicationId = " + aid.toString() + ", tenantId = " + tid.toString() + ", count = " + elements.size() + " ]");
                    stream.println();

                    elements.entrySet().stream().forEach(
                            entry -> {
                                stream.print(indent + BLANK + "[ id = " + entry.getKey().toString() + " ] ");
                                stream.print(printEntityHandler.apply(entry.getValue()));
                                stream.println();
                            }
                    );
                }
            }
        }

        private <E> void printApplicationMap(PrintStream stream, String
                indent, ApplicationMap<IdType, E> applicationMap, Function<E, String> printEntityHandler) {
            Set<ApplicationIdType> ids = applicationMap.getIds();
            for (ApplicationIdType aid : ids) {
                Map<IdType, E> elements = applicationMap.get(aid);

                stream.print(indent + "[ applicationId = " + aid.toString() + ", count = " + elements.size() + " ]");
                stream.println();

                elements.entrySet().stream().forEach(
                        entry -> {
                            stream.print(indent + BLANK + "[ id = " + entry.getKey().toString() + " ] ");
                            stream.print(printEntityHandler.apply(entry.getValue()));
                            stream.println();
                        }
                );
            }
        }

        private <E> void printJanusMap(PrintStream stream, String indent, String
                idName, JanusMap<IdType, E> janusMap, Function<E, String> printEntityHandler) {

            Set<IdType> ids = janusMap.getIds();
            for (IdType id : ids) {
                Map<IdType, E> elements = janusMap.get(id);

                stream.print(indent + "[ " + idName + " = " + id.toString() + ", count = " + elements.size() + " ]");
                stream.println();

                elements.entrySet().stream().forEach(
                        entry -> {
                            stream.print(indent + BLANK + "[ id = " + entry.getKey().toString() + " ] ");
                            stream.print(printEntityHandler.apply(entry.getValue()));
                            stream.println();
                        }
                );
            }
        }

        private <E> void printMap(PrintStream stream, String
                indent, Map<IdType, E> map, Function<E, String> printEntityHandler) {

            stream.print(indent + "[ count = " + map.size() + " ]");
            stream.println();

            map.entrySet().stream().forEach(
                    entry -> {
                        stream.print(indent + BLANK + "[ id = " + entry.getKey().toString() + " ] ");
                        stream.print(printEntityHandler.apply(entry.getValue()));
                        stream.println();
                    }
            );
        }
    }

    //</editor-fold>

    //<editor-fold desc="从BaseAccessControlCacheProvider构建">

    public static DirectAccessControlSource dumpFrom(BaseAccessControlCacheProvider cacheProvider) {
        return CreateHelper.dumpFrom(cacheProvider);
    }

    private static class CreateHelper {

        /**
         * 从sourceLoaderBuilder中提取数据，填充到receiver
         *
         * @param applicationIdTenantIdRange 提取数据的applicationId,tenantId范围
         * @param sourceLoaderBuilder        数据源
         * @param receiver                   接收者
         * @param <K>                        对应SourceLoader的K
         * @param <V>                        对应SourceLoader的V
         */
        static <K, V> void fillTenantMap(Map<ApplicationIdType
                , Set<TenantIdType>> applicationIdTenantIdRange
                , BiFunction<ApplicationIdType, TenantIdType, SourceLoader<K, V>> sourceLoaderBuilder
                , TenantMap<K, V> receiver) {

            for (Map.Entry<ApplicationIdType, Set<TenantIdType>> entry : applicationIdTenantIdRange.entrySet()) {
                for (TenantIdType tenantId : entry.getValue()) {
                    SourceLoader<K, V> sourceLoader = sourceLoaderBuilder.apply(entry.getKey(), tenantId);
                    if (sourceLoader != null) {
                        Map<K, V> map = sourceLoader.load();
                        receiver.add(entry.getKey(), tenantId, map);
                    }
                }
            }

        }

        /**
         * 参考fillTenantMap的说明
         */
        static <K, V> void fillApplicationMap(Set<ApplicationIdType> applicationIdRange
                , Function<ApplicationIdType, SourceLoader<K, V>> sourceLoaderBuilder
                , ApplicationMap<K, V> receiver) {


            for (ApplicationIdType applicationId : applicationIdRange) {
                SourceLoader<K, V> sourceLoader = sourceLoaderBuilder.apply(applicationId);
                if (sourceLoader != null) {
                    Map<K, V> map = sourceLoader.load();
                    receiver.add(applicationId, map);
                }
            }
        }

        /**
         * 可替代fillApplicationMap的功能。所以fillApplicationMap这个方法去掉了
         * 逻辑与fillTenantMap类似
         */
        static <ID, K, V> void fillClusterMap(Set<ID> ids
                , Function<ID, SourceLoader<K, V>> sourceLoaderBuilder
                , ClusterMap<ID, K, V> receiver) {


            for (ID id : ids) {
                SourceLoader<K, V> sourceLoader = sourceLoaderBuilder.apply(id);
                if (sourceLoader != null) {
                    Map<K, V> map = sourceLoader.load();
                    receiver.add(id, map);
                }
            }
        }


        public static DirectAccessControlSource dumpFrom(BaseAccessControlCacheProvider cacheProvider) {

            DirectAccessControlSource result = new DirectAccessControlSource();

            Map<ApplicationIdType, Set<TenantIdType>> applicationIdTenantIdRange = cacheProvider.loadApplicationIdTenantIdRange();

            //1
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createScopeSingleRoleUserXLoader
                    , result.getScopeSingleRoleUserX());

            //2
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createScopeMultipleRoleUserXLoader
                    , result.getScopeMultipleRoleUserX());

            //3
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createScopeUserGroupUserXLoader
                    , result.getScopeUserGroupUserX());

            //4
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createUserGroupLoader
                    , result.getUserGroup());

            //5
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createUserGroupUserXLoader
                    , result.getUserGroupUserX());

            //6
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createSinglePermissionLoader
                    , result.getSinglePermission());

            //7
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createSingleRolePermissionXLoader
                    , result.getSingleRolePermissionX());

            //8
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createSingleRoleLoader
                    , result.getSingleRole());

            //9
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createSingleRoleOtherXLoader
                    , result.getSingleRoleOtherX());

            //10
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createSingleRoleUserGroupXLoader
                    , result.getSingleRoleUserGroupX());

            //11
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createSingleRoleUserXLoader
                    , result.getSingleRoleUserX());

            //12
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createMultiplePermissionLoader
                    , result.getMultiplePermission());

            //13
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createMultipleRolePermissionXLoader
                    , result.getMultipleRolePermissionX());

            //14
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createMultipleRoleLoader
                    , result.getMultipleRole());

            //15
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createMultipleRoleOtherXLoader
                    , result.getMultipleRoleOtherX());

            //16
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createMultipleRoleUserGroupXLoader
                    , result.getMultipleRoleUserGroupX());

            //17
            fillTenantMap(applicationIdTenantIdRange, cacheProvider::createMultipleRoleUserXLoader
                    , result.getMultipleRoleUserX());

            //18
            fillClusterMap(applicationIdTenantIdRange.keySet(), cacheProvider::createPermissionTemplateLoader
                    , result.getPermissionTemplate());

            //19
            result.getOuterObjectType().putAll(cacheProvider.createOuterObjectTypeLoader().load());

            //20
            fillClusterMap(result.getOuterObjectType().keySet(), cacheProvider::createOuterObjectLoader
                    , result.getOuterObject());

            //21
            fillClusterMap(result.getOuterObjectType().keySet(), cacheProvider::createUserOuterObjectXLoader
                    , result.getUserOuterObjectX());

            return result;
        }
    }

    //</editor-fold>
}
