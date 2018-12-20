package com.mobven.moviedb.helper.mjson;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mjson.Json;

public class DataExtractor {

    public static String getString(String key, Json data) {
        String stringValue = null;
        if(checkKeyExists(key,data) && data.at(key).isString()) {
            stringValue = data.at(key).asString();
        }
        return stringValue;
    }

    public static int getInteger(String key, Json data) {
        int intValue = Integer.MIN_VALUE;
        String stringValue;
        if((stringValue = getString(key,data)) != null) {
            intValue = Integer.parseInt(stringValue);
        }
        return intValue;
    }

    public static boolean getBoolean(String key, Json data) {
        boolean booleanValue = false;
        if(checkKeyExists(key,data) && data.at(key).isBoolean()) {
            booleanValue = data.at(key).asBoolean();
        }
        return booleanValue;
    }

    public static List<String> getList(String key, Json data) {
        List<String> listValue = null;
        if(checkKeyExists(key,data) && data.at(key).isArray()) {
            listValue = new ArrayList<>();
            for (Object entry : data.at(key).asList()) {
                listValue.add(entry.toString());
            }
        }
        return listValue;
    }

    public static Bundle getBundle(@Nullable String key, @NonNull Json data) {
        Bundle bundle = null;
        Map<String,String> mapToConvert = getMap(key,data);
        if(mapToConvert != null) {
            bundle = new Bundle();
            for (Map.Entry<String,String> entry : mapToConvert.entrySet()) {
                bundle.putString(entry.getKey(),entry.getValue());
            }
        }
        return bundle;
    }

    public static TreeMap<String,String> getTreeMap(@Nullable String key, @NonNull Json data) {
        TreeMap<String,String> mapValue = null;
        Map<String,String> mapToConvert = getMap(key,data);
        if(mapToConvert != null) {
            mapValue = new TreeMap<>();
            for (Map.Entry<String,String> entry : mapToConvert.entrySet()) {
                mapValue.put(entry.getKey(),entry.getValue());
            }
        }
        return mapValue;
    }

    public static Map<String,String> getMap(@Nullable String key, @NonNull Json data) {
        Map<String,String> mapValue = null;
        Map<String,Object> mapToConvert = null;
        if(key != null) {
            if(checkKeyExists(key,data) && data.at(key).isObject()) {
                mapToConvert = data.at(key).asMap();
            }
        } else {
            if(data.isObject()) {
                mapToConvert = data.asMap();
            }
        }
        if(mapToConvert != null) {
            mapValue = new TreeMap<>();
            for (Map.Entry<String,Object> entry : mapToConvert.entrySet()) {
                mapValue.put(entry.getKey(),entry.getValue().toString());
            }
        }
        return mapValue;
    }

    private static boolean checkKeyExists(String key, Json data) {
        return data != null && data.has(key);
    }
}
