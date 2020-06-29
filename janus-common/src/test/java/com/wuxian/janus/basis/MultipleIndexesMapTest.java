package com.wuxian.janus.basis;

import com.wuxian.janus.basis.data.MultipleIndexesMapData;
import com.wuxian.janus.index.MultipleIndexesMap;
import com.wuxian.janus.index.NamedConverter;
import com.wuxian.janus.util.StrictUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * AutoFillMultipleIndexesMap Tester.
 *
 * @author wuxian
 */
public class MultipleIndexesMapTest extends TestTemplate<MultipleIndexesMapData> {

    @Override
    protected MultipleIndexesMapData createData() {
        return new MultipleIndexesMapData();
    }

    MultipleIndexesMap<String, MultipleIndexesMapData.Student> map;

    @BeforeEach
    public void before() {
        map = new MultipleIndexesMap<>();

        map.createIndex(MultipleIndexesMapData.Student.class,
                new NamedConverter("address", o -> (String) o)
                , new NamedConverter("age", o -> String.valueOf((int) o))
                , new NamedConverter("multiple", o -> String.valueOf((boolean) o)));

        map.setSource(this.getData().loadSource());
    }

    @AfterEach
    public void after() {
    }

    @DisplayName("测试by条件过滤数据")
    @Test
    public void testGetByCondition() {
        //test1
        MultipleIndexesMapData.Student sample1 = MultipleIndexesMapData.Student.builder().address("std3_\"address").age(22).multiple(true).build();
        List<MultipleIndexesMapData.Student> result1 = map.getByCondition(sample1,
                "address", "age", "multiple");

        assertEquals(2, result1.size());
        assertEquals("std4_code", StrictUtils.get(result1, 0).getCode());
        assertEquals("std3_code", StrictUtils.get(result1, 1).getCode());

        //test2
        MultipleIndexesMapData.Student sample2 = MultipleIndexesMapData.Student.builder().address("draft").age(22).multiple(true).build();
        List<MultipleIndexesMapData.Student> result2 = map.getByCondition(sample2,
                "address", "age", "multiple");
        assertEquals(0, result2.size());

        //test3
        MultipleIndexesMapData.Student sample3 = MultipleIndexesMapData.Student.builder().address("draft").multiple(true).build();
        List<MultipleIndexesMapData.Student> result3 = map.getByCondition(sample3,
                "address", "age", "multiple");
        assertEquals(0, result3.size());

        //test4
        MultipleIndexesMapData.Student sample4 = MultipleIndexesMapData.Student.builder().multiple(true).build();
        List<MultipleIndexesMapData.Student> result4 = map.getByCondition(sample4,
                "address", "age", "multiple");
        assertEquals(0, result4.size());
    }

    @DisplayName("测试清理源数据")
    @Test
    public void testClearSource() {
        MultipleIndexesMapData.Student sample = MultipleIndexesMapData.Student.builder().address("std3_\"address").age(22).multiple(true).build();
        List<MultipleIndexesMapData.Student> result1 = map.getByCondition(sample,
                "address", "age", "multiple");
        assertEquals(2, result1.size());

        map.clearSource();
        List<MultipleIndexesMapData.Student> result2;

        result2 = map.getByCondition(sample,
                "address", "age", "multiple");
        assertEquals(0, result2.size());
    }

    @DisplayName("测试by Keys过滤")
    @Test
    public void testGetByKeys() {

        //test1
        List<MultipleIndexesMapData.Student> students1 = map.getByKeys("std3_code", "std3_code", "std3_code");
        assertEquals(3, students1.size());
        assertEquals("std3_code", StrictUtils.get(students1, 0).getCode());
        assertEquals("std3_code", StrictUtils.get(students1, 1).getCode());
        assertEquals("std3_code", StrictUtils.get(students1, 2).getCode());

        //test2
        List<MultipleIndexesMapData.Student> students2 = map.getByKeys("std3_code", "std4_code", "std999_code");
        assertEquals(3, students2.size());
        assertEquals("std3_code", StrictUtils.get(students2, 0).getCode());
        assertEquals("std4_code", StrictUtils.get(students2, 1).getCode());
        assertNull(StrictUtils.get(students2, 2));
    }

    @DisplayName("测试by Key过滤")
    @Test
    public void testGetByKey() {
        MultipleIndexesMapData.Student student = map.getByKey("std3_code");
        assertEquals("std3_code", student.getCode());
    }

    @DisplayName("测试将集合转化成Map")
    @Test
    public void testCollectorsToMap() {

        //测试age作为key重复，建立map会不会异常

        MultipleIndexesMapData.Student s0 = MultipleIndexesMapData.Student.builder()
                .age(1)
                .address("si chuan")
                .build();

        MultipleIndexesMapData.Student s1 = MultipleIndexesMapData.Student.builder()
                //如果age 1重复，测试会崩溃
                .age(3)
                .address("chong qing")
                .build();

        MultipleIndexesMapData.Student s2 = MultipleIndexesMapData.Student.builder()
                .age(2)
                .address("bei jing")
                .build();

        List<MultipleIndexesMapData.Student> list = new ArrayList<>();
        list.add(s0);
        list.add(s1);
        list.add(s2);

        Map<Integer, MultipleIndexesMapData.Student> x = list.stream().collect(
                Collectors.toMap(MultipleIndexesMapData.Student::getAge, Function.identity())
        );

        //随便写个判断，主要是看x的生成会不会崩溃
        assertNotNull(x);
    }
}
