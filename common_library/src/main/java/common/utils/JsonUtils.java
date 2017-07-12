/**
 *
 */
package common.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import common.bean.MsMessage;

/**
 * @author 照东 将一个jsonArray类型的字符串转换成list—hashmap
 */

@SuppressWarnings({"rawtypes", "unchecked"})
public class JsonUtils {
    private static Gson gson = new Gson();

    public static Object parseMapIterabletoJSON(Object object) throws JSONException {
        if (object instanceof Map) {
            JSONObject json = new JSONObject();
            Map map = (Map) object;
            for (Object key : map.keySet()) {
                json.put(key.toString(), parseMapIterabletoJSON(map.get(key)));
            }
            return json;
        } else if (object instanceof Iterable) {
            JSONArray json = new JSONArray();
            for (Object value : ((Iterable) object)) {
                json.put(value);
            }
            return json;
        } else {
            return object;
        }
    }

    public static String parseBean2json(Object obj) {
        return gson.toJson(obj);
    }

    public static boolean isEmptyObject(JSONObject object) {
        return object.names() == null;
    }

    public static Map<String, Object> getMap(JSONObject object, String key)
            throws JSONException {
        return toMap(object.getJSONObject(key));
    }

    public static Map<String, Object> toMap(JSONObject object)
            throws JSONException {

        Map<String, Object> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(object.get(key)));
        }
        return map;
    }

    public static Map<String, String> parseMap(JSONObject object)
            throws JSONException {
        Map<String, String> map = new HashMap();
        try {
            Iterator keys = object.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                map.put(key, fromJson(object.get(key)).toString());
            }
            return map;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static List toList(JSONArray array) throws JSONException {
        List list = new ArrayList();
        for (int i = 0; i < array.length(); i++) {
            list.add(fromJson(array.get(i)));
        }
        return list;
    }

    private static Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return toMap((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        } else {
            return json;
        }
    }

    public static <T> List<T> parseRootJson2List(String json, Class<T> clazz, String listKey) throws JSONException {
        return parseJson2List(new JSONObject(json).getJSONObject("data").getJSONArray(listKey).toString(), clazz);
    }

    public static <T> List<T> parseRootJson2List(String json, Class<T> clazz) throws JSONException {
        return parseJson2List(new JSONObject(json).getJSONArray("data").toString(), clazz);
    }

    public static <T> List<T> parseJson2List(String json, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if (TextUtils.isEmpty(json)) {
            return list;
        }

        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (JsonElement element : array) {
            try {
                list.add(gson.fromJson(element, clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static <T> List<T> parseMsMessage2List(String json, Class<T> clazz) {
        return parseJson2List(gson.toJson(gson.fromJson(json, MsMessage.class).getData()), clazz);
    }

    public static <T> T parseJson2Bean(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> T parseRootJson2Bean(String json, Class<T> clazz) throws JSONException {
        return gson.fromJson(new JSONObject(json).getJSONObject("data").toString(), clazz);
    }

    public static <T> T parseRootJson2Bean(String json, Class<T> clazz, String obj_key) throws JSONException {
        return gson.fromJson(new JSONObject(json).getJSONObject("data").getJSONObject(obj_key).toString(), clazz);
    }

    public static <T> T parseJson2Bean(String json, Type type) {
        return gson.fromJson(json, type);
    }

    public static String getHint(String rootJson) {
        try {
            return new JSONObject(rootJson).getString("hint");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static <W> String parseMap2String(Map<String, W> map) {
        String jsonStr = gson.toJson(map);
        return jsonStr;
    }

    /**
     * 用来兼容Android4.4以下版本的remove方法
     *
     * @param jsonArray
     * @param index
     * @return
     */
    public static JSONArray removeCompatibilityKITKAT(JSONArray jsonArray, int index) {
        JSONArray mJsonArray = new JSONArray();
        if (index < 0)
            return mJsonArray;
        if (index > jsonArray.length())
            return mJsonArray;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                if (i != index) {
                    mJsonArray.put(jsonArray.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mJsonArray;
    }
}
