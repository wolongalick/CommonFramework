package common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 几秒前，几分钟前，几小时前，几天前，几月前，几年前时间
 */
public class RelativeDateFormat {

    private static final long ONE_SECOND = 1000L;
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;  
    private static final long ONE_WEEK = 604800000L;//周
  
    private static final String JUST_NOW = "刚刚";
    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";  
    private static final String ONE_DAY_AGO = "天前";  
    private static final String ONE_WEEK_AGO = "周前";
  
    public static String transformTime(String time,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return format(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String format(Date date) {  
        long delta = new Date().getTime() - date.getTime();
        //1，小于1秒，刚刚
        if(delta < ONE_SECOND){
            return JUST_NOW;
        }
        //2，小于1分，几秒前
        if (delta < ONE_MINUTE) {
            long seconds = toSeconds(delta);  
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;  
        }
        //3，小于1小时，几分钟前
        if (delta < ONE_HOUR) {
            long minutes = toMinutes(delta);  
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;  
        }
        //4，小于1天，几小时前
        if (delta < ONE_DAY) {
            long hours = toHours(delta);  
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;  
        }
        //5，小于1周，几天前
        if (delta < ONE_WEEK) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        //6，小于4周，几周前
        if (delta < 4L * ONE_WEEK) {
            long weeks = toWeek(delta);
            return (weeks <= 0 ? 1 : weeks) + ONE_WEEK_AGO;
        }else{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }
    }  
  
    private static long toSeconds(long date) {  
        return date / 1000L;  
    }  
  
    private static long toMinutes(long date) {  
        return toSeconds(date) / 60L;  
    }  
  
    private static long toHours(long date) {  
        return toMinutes(date) / 60L;  
    }  
  
    private static long toDays(long date) {  
        return toHours(date) / 24L;  
    }  

    private static long toWeek(long date) {
        return toDays(date) / 7L;
    }
    private static long toMonths(long date) {
        return toDays(date) / 30L;  
    }  
  
    private static long toYears(long date) {  
        return toMonths(date) / 365L;  
    }  
  
}  