package com.by.util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QueryMapUtil {

    public static Map<String, Object> queryMap(HttpServletRequest request) {

        // 1、创建map封装查询的参数（分页的参数，条件查询的参数）
        Map<String, Object> map = new HashMap<>();

        // 2、获取用户传递的参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        Set<Map.Entry<String, String[]>> entries = parameterMap.entrySet();

        // 3、遍历
        for (Map.Entry<String, String[]> entry : entries) {
            String key = entry.getKey();
            String value = entry.getValue()[0];

            // 把属性名称和属性值放入map
            map.put(key, value);
        }

        return map;
    }

}
