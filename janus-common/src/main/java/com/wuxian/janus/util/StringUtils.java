package com.wuxian.janus.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    private StringUtils() {
    }

    public static String safeJoinStrings(String... data) {
        List<String> safe = new ArrayList<>();
        for (String d : data) {
            if (d == null) {
                safe.add("null");
            } else {
                String tmp = d.replace("\\", "\\\\").replace("\"", "\\\"");
                safe.add("\"" + tmp + "\"");
            }
        }
        return String.join(",", safe);
    }


}
