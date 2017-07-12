package common.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by cxw on 2017/3/13.
 */

public class MapUtils {
    //将javabean实体类转为map类型，然后返回一个map类型的值
    public static Map<String, Object> beanToMap(Object obj) {
        String json = JsonUtils.parseBean2json(obj);
        try {
            return JsonUtils.toMap(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
