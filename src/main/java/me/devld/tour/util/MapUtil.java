package me.devld.tour.util;

import java.util.HashMap;
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

}
