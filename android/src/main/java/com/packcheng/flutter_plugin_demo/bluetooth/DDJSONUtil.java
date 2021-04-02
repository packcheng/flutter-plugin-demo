package com.packcheng.flutter_plugin_demo.bluetooth;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rinfon on 15/7/21.
 */
public class DDJSONUtil {
    public static boolean has(JSONObject j, String key) {
        return !(j == null || key == null) && j.has(key);
    }

    public static String getString(JSONObject j, String key) {
        try {
            if (has(j, key)) {
                return j.getString(key);
            } else {
                return "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getInt(JSONObject j, String key) {
        try {
            if (has(j, key)) {
                return j.getInt(key);
            } else {
                return -1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static long getLong(JSONObject j, String key) {
        try {
            if (has(j, key)) {
                return j.getLong(key);
            } else {
                return -1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static double getDouble(JSONObject j, String key) {
        try {
            if (has(j, key)) {
                return j.getDouble(key);
            } else {
                return -1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static boolean getBoolean(JSONObject j, String key) {
        try {
            return has(j, key) && j.getBoolean(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JSONArray getJSONarray(JSONObject j, String key) {
        try {
            if (has(j, key)) {
                return j.getJSONArray(key);
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static JSONObject getJSONObject(JSONObject j, String key) {
        try {
            if (has(j, key)) {
                return j.getJSONObject(key);
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, Object> jsonToMap(JSONObject json) throws JSONException {
        HashMap<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static HashMap<String, Object> toMap(JSONObject object) throws JSONException {
        HashMap<String, Object> map = new HashMap<String, Object>();

        if (object != null) {
            Iterator<String> keysItr = object.keys();
            if (keysItr != null) {
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    Object value = object.get(key);

                    if (value instanceof JSONArray) {
                        value = toList((JSONArray) value);
                    } else if (value instanceof JSONObject) {
                        value = toMap((JSONObject) value);
                    }
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static JSONObject loadJSONFromAsset(Context context) {
        JSONObject jsonObject = null;
        String json = null;
        try {

            InputStream is = context.getAssets().open("LocList-2052-zh-cn.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            jsonObject = new JSONObject(json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    public static JSONArray concatArray(JSONArray... arrs)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (JSONArray arr : arrs) {
            for (int i = 0; i < arr.length(); i++) {
                result.put(arr.get(i));
            }
        }
        return result;
    }

    public static JSONArray remove(final int idx, final JSONArray from) {
        final List<JSONObject> objs = asList(from);
        objs.remove(idx);

        final JSONArray ja = new JSONArray();
        for (final JSONObject obj : objs) {
            ja.put(obj);
        }

        return ja;
    }

    public static JSONArray removeStringArray(final int idx, final JSONArray from) {
        final List<String> objs = asStringList(from);
        objs.remove(idx);

        final JSONArray ja = new JSONArray();
        for (final String obj : objs) {
            ja.put(obj);
        }

        return ja;
    }

    public static List<String> asStringList(final JSONArray ja) {
        final int len = ja.length();
        final ArrayList<String> result = new ArrayList<String>(len);
        for (int i = 0; i < len; i++) {
            String obj = null;
            try {
                obj = (String) ja.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }


    public static List<JSONObject> asList(final JSONArray ja) {
        final int len = ja.length();
        final ArrayList<JSONObject> result = new ArrayList<JSONObject>(len);
        for (int i = 0; i < len; i++) {
            final JSONObject obj = ja.optJSONObject(i);
            if (obj != null) {
                result.add(obj);
            } else {

            }
        }
        return result;
    }


}
