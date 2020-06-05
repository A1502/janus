package com.wuxian.janus.struct;

import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.IdBuilder;
import com.wuxian.janus.core.basis.StrictUtils;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IdTypeTest {

    @Test
    @DisplayName("测试两个IdType相等")
    void testEquals() {

        Long n1 = 1L;
        Long n2 = 1L;
        Assert.assertEquals(n1, n2);

        IdType idType1 = IdBuilder.id(1);
        IdType idType2 = IdBuilder.id(1);

        Assert.assertTrue(StrictUtils.equals(idType1, idType2));
    }

    @Test
    @DisplayName("测试IdType的value是null")
    void testWhenNull() {
        IdType idType = IdBuilder.id(null);
        Assert.assertNull(idType.getValue());
    }

    @Test
    @DisplayName("测试IdType排序")
    void testSort() {

        List<IdType> list = new ArrayList<>();
        list.add(IdBuilder.id(1));
        list.add(IdBuilder.id(8));
        list.add(IdBuilder.id(15));
        list.add(IdBuilder.id(3));

        List<IdType> sorted = list.stream().sorted().collect(Collectors.toList());

        List<IdType> idsSorted = IdBuilder.ids(1, 8, 15, 3);

        Assert.assertEquals(StrictUtils.get(sorted, 0), StrictUtils.get(idsSorted, 0));
        Assert.assertEquals(StrictUtils.get(sorted, 1), StrictUtils.get(idsSorted, 1));
        Assert.assertEquals(StrictUtils.get(sorted, 2), StrictUtils.get(idsSorted, 2));
        Assert.assertEquals(StrictUtils.get(sorted, 3), StrictUtils.get(idsSorted, 3));
    }

}
