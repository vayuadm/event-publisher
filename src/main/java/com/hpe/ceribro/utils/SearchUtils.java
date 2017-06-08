package com.hpe.ceribro.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class SearchUtils {

    public static LinkedHashMap<String, String> buildRange(String from, String to) {

        LinkedHashMap<String, String> ret = new LinkedHashMap<>(1);
        ret.put("last-modified", String.format("{\"gte\":\"%s\",\"lte\":\"%s\"}", from, to));

        return ret;
    }

    public static Map<String, String[]> buildQueryParams(String type, int start, int size) {

        Map<String, String[]> ret = new LinkedHashMap<>(5);
        String[] strings = new String[]{Integer.toString(start)};
        ret.put("start", strings);
        strings = new String[]{""};
        ret.put("q", strings);
        strings = new String[]{Integer.toString(size)};
        ret.put("size", strings);
        strings = new String[]{type};
        ret.put("type", strings);
        strings = new String[]{"sa"};
        ret.put("user", strings);

        return ret;
    }
}
