package com.wuxian.janus.cache.model;

import com.wuxian.janus.cache.model.extract.id.LongIdGenerator;
import com.wuxian.janus.struct.primary.IdType;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class DefaultIdGeneratorTest {

    @Test
    @DisplayName("基本功能测试IdGenerator")
    void testCase1() {
        LongIdGenerator generator = new LongIdGenerator();

        IdType usedOne = new IdType(null);
        usedOne.setStringValue(((Integer) Integer.MAX_VALUE).toString());

        generator.addUsed(Collections.singletonList(usedOne));

        IdType getOne = generator.generate();
        System.out.println(getOne.getValue().getClass().getName());
        Assert.assertEquals(getOne.toString(), "2147483648");
    }
}
