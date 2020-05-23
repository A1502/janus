package com.wuxian.janus.core.basis;

import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.TestTemplate;
import com.wuxian.janus.core.basis.data.MultipleIndexesMapData;
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
        map.createIndex(MultipleIndexesMapData.Student.class, (Object[] objects) ->
                        new String[]{(String) objects[0],
                                String.valueOf((int) objects[1]),
                                String.valueOf((boolean) objects[2])
                        },
                "address", "age", "multiple");
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

        assertEquals(result1.size(), 2);
        assertEquals(StrictUtils.get(result1, 0).getCode(), "std4_code");
        assertEquals(StrictUtils.get(result1, 1).getCode(), "std3_code");

        //test2
        MultipleIndexesMapData.Student sample2 = MultipleIndexesMapData.Student.builder().address("draft").age(22).multiple(true).build();
        List<MultipleIndexesMapData.Student> result2 = map.getByCondition(sample2,
                "address", "age", "multiple");
        assertEquals(result2.size(), 0);

        //test3
        MultipleIndexesMapData.Student sample3 = MultipleIndexesMapData.Student.builder().address("draft").multiple(true).build();
        List<MultipleIndexesMapData.Student> result3 = map.getByCondition(sample3,
                "address", "age", "multiple");
        assertEquals(result3.size(), 0);

        //test4
        MultipleIndexesMapData.Student sample4 = MultipleIndexesMapData.Student.builder().multiple(true).build();
        List<MultipleIndexesMapData.Student> result4 = map.getByCondition(sample4,
                "address", "age", "multiple");
        assertEquals(result4.size(), 0);
    }

    @DisplayName("测试清理源数据")
    @Test
    public void testClearSource() {
        MultipleIndexesMapData.Student sample = MultipleIndexesMapData.Student.builder().address("std3_\"address").age(22).multiple(true).build();
        List<MultipleIndexesMapData.Student> result1 = map.getByCondition(sample,
                "address", "age", "multiple");
        assertEquals(result1.size(), 2);

        map.clearSource();
        List<MultipleIndexesMapData.Student> result2;

        result2 = map.getByCondition(sample,
                "address", "age", "multiple");
        assertEquals(result2.size(), 0);
    }

    @DisplayName("测试by Keys过滤")
    @Test
    public void testGetByKeys() {

        //test1
        List<MultipleIndexesMapData.Student> students1 = map.getByKeys("std3_code", "std3_code", "std3_code");
        assertEquals(students1.size(), 3);
        assertEquals(StrictUtils.get(students1, 0).getCode(), "std3_code");
        assertEquals(StrictUtils.get(students1, 1).getCode(), "std3_code");
        assertEquals(StrictUtils.get(students1, 2).getCode(), "std3_code");

        //test2
        List<MultipleIndexesMapData.Student> students2 = map.getByKeys("std3_code", "std4_code", "std999_code");
        assertEquals(students2.size(), 3);
        assertEquals(StrictUtils.get(students2, 0).getCode(), "std3_code");
        assertEquals(StrictUtils.get(students2, 1).getCode(), "std4_code");
        assertNull(StrictUtils.get(students2, 2));
    }

    @DisplayName("测试by Key过滤")
    @Test
    public void testGetByKey() {
        MultipleIndexesMapData.Student student = map.getByKey("std3_code");
        assertEquals(student.getCode(), "std3_code");
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
