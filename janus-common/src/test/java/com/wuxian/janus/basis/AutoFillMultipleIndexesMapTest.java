package com.wuxian.janus.basis;

import com.wuxian.janus.basis.data.MultipleIndexesMapData;
import com.wuxian.janus.index.AutoFillMultipleIndexesMap;
import com.wuxian.janus.index.MultipleIndexesMap;
import com.wuxian.janus.index.NamedConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * AutoFillMultipleIndexesMap Tester.
 *
 * @author wuxian
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

        map.createIndex(MultipleIndexesMapData.Student.class,
                new NamedConverter("address", o -> (String) o)
                , new NamedConverter("age", o -> String.valueOf((int) o))
                , new NamedConverter("multiple", o -> String.valueOf((boolean) o)));
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
        assertEquals(2, result1.size());

        map.clearSource();
        List<MultipleIndexesMapData.Student> result2;

        result2 = map.getByCondition(sample,
                "address", "age", "multiple");

        assertEquals(2, result2.size());
    }
}
