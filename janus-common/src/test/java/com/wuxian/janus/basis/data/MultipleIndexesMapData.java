package com.wuxian.janus.basis.data;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

public class MultipleIndexesMapData {

    @Data
    @Builder
    public static class Student {
        private String name;
        private String code;
        private int age;
        private String address;
        private Boolean multiple;
    }

    public Map<String, Student> loadSource() {
        Map<String, Student> result = new HashMap<>();

        result.put("std1_code", Student.builder().age(20).address("std1_address").name("std1").code("std1_code").multiple(true).build());
        result.put("std2_code", Student.builder().age(20).address("std2_address").name("std2").code("std2_code").multiple(true).build());
        result.put("std3_code", Student.builder().age(22).address("std3_\"address").name("std3").code("std3_code").multiple(true).build());
        result.put("std4_code", Student.builder().age(22).address("std3_\"address").name("std4").code("std4_code").multiple(true).build());
        result.put("std5_code", Student.builder().age(20).address("std5_address").name("std5").code("std5_code").multiple(true).build());
        result.put("std6_code", Student.builder().age(22).address("std6_address").name("std6").code("std6_code").multiple(true).build());
        result.put("std7_code", Student.builder().age(20).address("std7_address").name("std7").code("std7_code").multiple(true).build());
        result.put("std8_code", Student.builder().age(20).address("std8_address").name("std8").code("std8_code").multiple(true).build());

        return result;
    }
}