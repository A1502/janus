package com.wuxian.janus.cache.model;

import com.wuxian.janus.ErrorCodeException;
import com.wuxian.janus.cache.model.extract.SourceExtractor;
import com.wuxian.janus.cache.model.extract.id.LongIdGeneratorFactory;
import com.wuxian.janus.cache.model.source.ApplicationGroup;
import com.wuxian.janus.cache.model.source.OuterObject;
import com.wuxian.janus.cache.model.source.OuterObjectType;
import com.wuxian.janus.cache.model.source.User;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.cache.provider.JanusMap;
import com.wuxian.janus.struct.layer1.OuterObjectStruct;
import com.wuxian.janus.struct.layer1.OuterObjectTypeStruct;
import com.wuxian.janus.struct.layer6.UserOuterObjectXStruct;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.UserIdType;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

public class OuterObjectTest {

    //<editor-fold desc="私有方法">

    private IdType createIdType(String value) {
        IdType result = new IdType(null);
        result.setStringValue(value);
        return result;
    }

    private UserIdType createUserIdType(String value) {
        UserIdType result = new UserIdType(null);
        result.setStringValue(value);
        return result;
    }

    //</editor-fold>

    @Test
    @DisplayName("测试outerObject、outerObjectType和UserGroupUserX的建立")
    void testOuterObjectAndOuterObjectType() {
        ApplicationGroup applicationGroup = new ApplicationGroup().addItem(
                OuterObjectType.byId("19", "oot19"),
                OuterObjectType.byId("1", "oot1"))
                .addItem(
                        OuterObject.byId("12", "oo12", "oot21")
                                .addItem(User.byId("1", "A", "B")
                                        , User.byId("2", "X", null)
                                        , User.byId("2", "C")),
                        OuterObject.byId("10", "oo10", "oot1")
                                .addItem(User.byId("2", (String) null)),
                        OuterObject.byId("11", "oo11", "oot21")
                                .addItem(User.byId("1", "C")
                                        , User.byId("2", (String) null)),
                        new OuterObject("oo11", "oot21")
                                .addItem(User.byId("1", "A", "B")),
                        OuterObject.byId("13", "oo13", "oot20")
                );

        DirectAccessControlSource source = TestUtils.extractAndPrint("testOuterObjectAndOuterObjectType"
                , applicationGroup);

        //验证OuterObjectType
        Set<IdType> ids = source.getOuterObjectType().keySet();
        Assert.assertEquals(4, ids.size());
        for (String id : new String[]{"1", "19", "20", "21"}) {
            Assert.assertTrue(ids.contains(createIdType(id)));
        }

        //验证OuterObject
        //oot1 应包含outerObjectId: 10
        checkOuterObjectTypeContainsId(source, "oot1", new String[]{"10"});

        //oot9 应包含outerObjectId: 无
        checkOuterObjectTypeContainsId(source, "oot19", new String[]{});

        //oot2 应包含outerObjectId: 11,12
        checkOuterObjectTypeContainsId(source, "oot21", new String[]{"11", "12"});

        //oot10 应包含outerObjectId: 13
        checkOuterObjectTypeContainsId(source, "oot20", new String[]{"13"});

        //验证UserOuterObjectX
        checkUserOuterObjectXContains(source, "1", "A", "11,12");
        checkUserOuterObjectXContains(source, "1", "B", "11,12");
        checkUserOuterObjectXContains(source, "2", "X", "12");
        checkUserOuterObjectXContains(source, "2", null, "11,12");
        checkUserOuterObjectXContains(source, "2", "C", "12");
        checkUserOuterObjectXContains(source, "2", null, "10");
        checkUserOuterObjectXContains(source, "1", "C", "11");

        JanusMap<IdType, UserOuterObjectXStruct> janusMap = source.getUserOuterObjectX();
        Assert.assertEquals(2, janusMap.getIds().size());
        long xStructCount = janusMap.getIds().stream()
                .map(id -> janusMap.get(id).values())
                .mapToLong(Collection::size).sum();
        Assert.assertEquals(7, xStructCount);

    }

    private void checkUserOuterObjectXContains(DirectAccessControlSource source, String userId, String scope, String outerObjectIdList) {
        JanusMap<IdType, UserOuterObjectXStruct> map = source.getUserOuterObjectX();

        long count = 0L;
        for (IdType id : map.getIds()) {
            count += map.get(id).values().stream().filter(o ->
                    userId.equals(o.getUserId().toString())
                            && ((scope == null && o.getScope() == null) || (scope != null && scope.equals(o.getScope())))
                            && outerObjectIdList.equals(o.getOuterObjectIdList())).count();
        }
        Assert.assertEquals(1L, count);
    }

    private void checkOuterObjectTypeContainsId(DirectAccessControlSource source, String outerObjectTypeCode, String[] outerObjectIds) {

        Collection<OuterObjectTypeStruct> outerObjectTypes = source.getOuterObjectType().values();
        OuterObjectTypeStruct typeStruct = TestUtils.findFirst(outerObjectTypes, o -> o.getCode().equals(outerObjectTypeCode));
        Map<IdType, OuterObjectStruct> objMap = source.getOuterObject().get(new IdType(typeStruct.getId()));

        if (outerObjectIds.length == 0) {
            Assert.assertNull(objMap);
            return;
        }

        Collection<IdType> objIds = new ArrayList<>();
        for (String id : outerObjectIds) {
            objIds.add(createIdType(id));
        }
        Assert.assertTrue(objMap.keySet().containsAll(objIds));
        Assert.assertEquals(objMap.keySet().size(), objIds.size());
    }

    @Test
    @DisplayName("引发Id冲突异常")
    void testIdConflict() {
        ApplicationGroup applicationGroup = new ApplicationGroup().addItem(
                OuterObjectType.byId("9", "oot19"),
                OuterObjectType.byId("1", "oot1"),
                OuterObjectType.byId("17", "oot1"));

        SourceExtractor sourceExtractor = new SourceExtractor(new LongIdGeneratorFactory());
        try {
            sourceExtractor.extract(applicationGroup);
            Assert.fail("未触发期望异常");
        } catch (ErrorCodeException ece) {
            Assert.assertEquals(ece.getErrorCode(), ErrorFactory.ID_CONFLICT);
        }
    }

    @Test
    @DisplayName("引发Code冲突异常")
    void testCodeConflict() {
        ApplicationGroup applicationGroup = new ApplicationGroup().addItem(
                OuterObjectType.byId("9", "oot19"),
                OuterObjectType.byId("1", "oot1"),
                OuterObjectType.byId("1", "oot17"));

        SourceExtractor sourceExtractor = new SourceExtractor(new LongIdGeneratorFactory());
        try {
            sourceExtractor.extract(applicationGroup);
            Assert.fail("未触发期望异常");
        } catch (ErrorCodeException ece) {
            Assert.assertEquals(ece.getErrorCode(), ErrorFactory.KEY_FIELDS_CONFLICT);
        }
    }

    @Test
    @DisplayName("测试模型的合并实体功能")
    void testMergeStruct() {
        OuterObject copyFromModel = new OuterObject("oo1", "subject_outer");
        OuterObjectStruct struct1 = new OuterObjectStruct();
        struct1.setId(createIdType("888").getValue());
        struct1.setCreatedBy(createUserIdType("200").getValue());
        struct1.setLastModifiedBy(createUserIdType("201").getValue());
        struct1.setCreatedDate(new Date());
        struct1.setLastModifiedDate(new Date());
        struct1.setOuterObjectTypeId(createIdType("888").getValue());

        //struct1.setReferenceId("refId-999");   //故意在struct1屏蔽掉这行留给struct2
        struct1.setReferenceCode("refCode-999");
        struct1.setReferenceName("refName-999");
        struct1.setCreationProposer(createUserIdType("200").getValue());
        struct1.setModificationProposer(createUserIdType("201").getValue());
        copyFromModel.setStruct(struct1);

        OuterObject subject = new OuterObject("oo1");
        OuterObjectStruct struct2 = new OuterObjectStruct();
        struct1.setReferenceId("refId-888");
        subject.setStruct(struct2);

        //执行合并
        subject.mergeFrom(copyFromModel);

        Assert.assertEquals(subject.getStruct().getId(), struct1.getId());
        Assert.assertEquals(subject.getStruct().getCreatedBy(), struct1.getCreatedBy());
        Assert.assertEquals(subject.getStruct().getLastModifiedBy(), struct1.getLastModifiedBy());
        Assert.assertEquals(subject.getStruct().getCreatedDate(), struct1.getCreatedDate());
        Assert.assertEquals(subject.getStruct().getLastModifiedDate(), struct1.getLastModifiedDate());
        Assert.assertEquals(subject.getStruct().getOuterObjectTypeId(), struct1.getOuterObjectTypeId());
        Assert.assertEquals(subject.getStruct().getReferenceCode(), struct1.getReferenceCode());
        Assert.assertEquals(subject.getStruct().getReferenceName(), struct1.getReferenceName());
        Assert.assertEquals(subject.getStruct().getCreationProposer(), struct1.getCreationProposer());
        Assert.assertEquals(subject.getStruct().getModificationProposer(), struct1.getModificationProposer());

        //注意下面测试是关键
        Assert.assertEquals(subject.getStruct().getReferenceId(), struct2.getReferenceId());
        Assert.assertEquals(subject.getOuterObjectTypeCode(), copyFromModel.getOuterObjectTypeCode());
    }

    @Test
    @DisplayName("提取OuterObject数据")
    void testOuterObject() {
        ApplicationGroup applicationGroup = new ApplicationGroup()
                .addItem(
                        OuterObject.byId("1", "oo99", "oot88"),
                        new OuterObject("oo99", "oot88"),
                        new OuterObject("oo99", "oot85")
                );

        SourceExtractor sourceExtractor = new SourceExtractor(new LongIdGeneratorFactory());
        DirectAccessControlSource source = sourceExtractor.extract(applicationGroup);

        checkOuterObjectTypeContainsId(source, "oot88", new String[]{"1"});

        Assert.assertEquals(2, source.getOuterObject().getIds().size());
        Assert.assertEquals(2, source.getOuterObjectType().keySet().size());
    }
}





















