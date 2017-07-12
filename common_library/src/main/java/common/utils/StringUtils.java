package common.utils;

import android.text.TextUtils;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2017/3/28
 * 备注:
 */
public class StringUtils {
    public static String parseEmptyStr(String str){
        if(TextUtils.isEmpty(str) || "null".equalsIgnoreCase(str)){
            return "";
        }
        return str;
    }
}
