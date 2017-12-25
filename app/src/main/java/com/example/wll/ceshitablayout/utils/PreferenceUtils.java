package com.example.wll.ceshitablayout.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by Administrator on 2017/7/29.
 */

public class PreferenceUtils {
    private static SharedPreferences preferences;
    private static String NAME = "wanglili";

    private static SharedPreferences getPreferences(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(NAME,
                    context.MODE_PRIVATE);
        }
        return preferences;
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);

    }

    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);

    }

    // 设置值,设置中心的值
    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = getPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();

    }

    public static String getString(Context context, String key) {
        return getString(context, key, null);

    }

    public static String getString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);

    }

    // 设置值,设置中心的值
    public static void setString(Context context, String key, String value) {
        SharedPreferences preferences = getPreferences(context);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    // 设置值,设置中心的值
    public static void setInt(Context context, String key, int value) {
        SharedPreferences preferences = getPreferences(context);
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();

    }

    public static int getInt(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);

    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);

    }

    /**
     * 用于保存集合
     *
     * @param key key
     * @param map map数据
     * @return 保存结果
     */
    public static <K, V> boolean putHashMapData(Context context, String key, Map<K, V> map) {
        boolean result;
        Editor editor = getPreferences(context).edit();
        try {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            editor.putString(key, json);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 用于保存集合
     *
     * @param key key
     * @return HashMap
     */
    public static <V> HashMap<String, V> getHashMapData(Context context, String key, Class<V> clsV) {
        String json = getString(context, key, "");
        HashMap<String, V> map = new HashMap<>();
        Gson gson = new Gson();
        if (!TextUtils.isEmpty(json)) {
            JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
            for (Map.Entry<String, JsonElement> entry : entrySet) {
                String entryKey = entry.getKey();
                JsonObject value = (JsonObject) entry.getValue();
                map.put(entryKey, gson.fromJson(value, clsV));
            }
            return map;
        }
        return null;
    }


    public static void clear(Context context) {
        SharedPreferences preferences = getPreferences(context);
        Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }


}
