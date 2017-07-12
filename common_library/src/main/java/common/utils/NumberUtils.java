package common.utils;

import java.text.DecimalFormat;

/**
 * Created by cxw on 2015/11/18.
 */
public class NumberUtils {
    public static DecimalFormat format1=new DecimalFormat("#.0");
    public static DecimalFormat format2=new DecimalFormat("#.00");

    public static String convert(Object obj,DecimalFormat format) {
        String str = format.format(obj);
        if(str.startsWith(".")){
            return "0"+str;
        }
        return str;
    }

}
