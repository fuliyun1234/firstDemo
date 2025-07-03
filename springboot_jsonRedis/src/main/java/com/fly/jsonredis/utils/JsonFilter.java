package com.fly.jsonredis.utils;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class JsonFilter {


    public static String getFilterJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject filteredJsonObject = filterJson(jsonObject);
        return filteredJsonObject.toString();
    }

    public static JSONObject filterJson(JSONObject jsonObject) {
        JSONObject filteredObject = new JSONObject();
        for (String key : jsonObject.keySet()) {
            if (isDesiredField(key)) {
                filteredObject.put(key, jsonObject.get(key));
            } else {
                Object value = jsonObject.get(key);
                if (value instanceof JSONObject) {
                    JSONObject filteredChildObject = filterJson((JSONObject) value);
                    if (filteredChildObject.length() > 0) {
                        filteredObject.put(key, filteredChildObject);
                    }
                } else if (value instanceof JSONArray) {
                    JSONArray filteredArray = filterJsonArray((JSONArray) value);
                    if (filteredArray.length() > 0) {
                        filteredObject.put(key, filteredArray);
                    }
                }
            }
        }
        return filteredObject;
    }

    public static JSONArray filterJsonArray(JSONArray jsonArray) {
        JSONArray filteredArray = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONObject) {
                JSONObject filteredObject = filterJson((JSONObject) value);
                if (filteredObject.length() > 0) {
                    filteredArray.put(filteredObject);
                }
            } else if (value instanceof JSONArray) {
                // 递归处理嵌套的JSONArray（如果需要）
                JSONArray nestedFilteredArray = filterJsonArray((JSONArray) value);
                if (nestedFilteredArray.length() > 0) {
                    filteredArray.put(nestedFilteredArray);
                }
            }
        }
        return filteredArray;
    }

    private static boolean isDesiredField(String fieldName) {
        return "@_name".equals(fieldName) ||
                "@_dsType".equals(fieldName) ||
                "@_dsLength".equals(fieldName) ||
                "@_nodeNum".equals(fieldName) ||
                "@_nullable".equals(fieldName);
    }

    private static boolean isDesiredField1(String fieldName) {
        List<String> predefinedFieldNames = Arrays.asList("Field1", "Field2", "Field3");
        for (String predefinedFieldName : predefinedFieldNames) {
            if (predefinedFieldName.equals(fieldName)) {
                return true;
            }
        }
        return false;
    }





}