package com.wuxian.janus.core.basis;

import com.wuxian.janus.TestTemplate;
import com.wuxian.janus.core.basis.data.MultipleIndexesMapData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * AutoFillMultipleIndexesMap Tester.
 *
 * @author Solomon
 */
public class AutoFillMultipleIndexesMapTest extends TestTemplate<MultipleIndexesMapData> {

    @Override
    protected MultipleIndexesMapData createData() {
        return new MultipleIndexesMapData();
    }

    MultipleIndexesMap<String, MultipleIndexesMapData.Student> map;

    @BeforeEach
    public void before() throws Exception {
        map = new AutoFillMultipleIndexesMap<>(
                () -> this.getData().loadSource()
        );

        map.createIndex(MultipleIndexesMapData.Student.class, (Object[] objects) ->
                        new String[]{(String) objects[0],
                                String.valueOf((int) objects[1]),
                                String.valueOf((boolean) objects[2])
                        },
                "address", "age", "multiple");
        map.setSource(this.getData().loadSource());
    }

    @AfterEach
    public void after() throws Exception {
    }

    @DisplayName("测试清理源数据")
    @Test
    public void testClearSource() throws Exception {
        MultipleIndexesMapData.Student sample = MultipleIndexesMapData.Student.builder().address("std3_\"address").age(22).multiple(true).build();
        List<MultipleIndexesMapData.Student> result1 = map.getByCondition(sample,
                "address", "age", "multiple");
        assertEquals(result1.size(), 2);

        map.clearSource();
        List<MultipleIndexesMapData.Student> result2;

        result2 = map.getByCondition(sample,
                "address", "age", "multiple");

        assertEquals(result2.size(), 2);
    }
}
