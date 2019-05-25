package me.devld.tour.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtil {

    private MapUtil() {
    }

    public static Map<String, Object> map(Object... params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        Map<String, Object> result = new HashMap<>(params.length / 2);
        for (int i = 0; i < params.length; i += 2) {
            String key = (String) params[i];
            Object val = params[i + 1];
            result.put(key, val);
        }
        return result;
    }

    public static String getString(Map<String, Object> map, String key) {
        Object obj = map.get(key);
        if (obj instanceof String) {
            return (String) obj;
        }
        return "";
    }

    public static <T> List<T> getList(Map<String, Object> map, String key, Class<T> t) {
        Object obj = map.get(key);
        if (obj instanceof List) {
            @SuppressWarnings("unchecked")
            List<T> res = (List<T>) obj;
            return res;
        }
        return Collections.emptyList();
    }

    public static List<Map<String, Object>> getListMap(Map<String, Object> map, String key) {
        Object obj = map.get(key);
        if (obj instanceof List) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> res = (List<Map<String, Object>>) obj;
            return res;
        }
        return Collections.emptyList();
    }

    public static Map<String, Object> getMap(Map<String, Object> map, String key) {
        Object obj = map.get(key);
        if (obj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> res = (Map<String, Object>) obj;
            return res;
        }
        return Collections.emptyMap();
    }

}
