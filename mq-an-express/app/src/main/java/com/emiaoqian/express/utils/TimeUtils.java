package com.emiaoqian.express.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiong on 2018/3/26.
 */

public class TimeUtils {
    public TimeUtils() {
    }

    public static String long2String(long time) {
        int sec = (int)time / 1000;
        int min = sec / 60;
        sec %= 60;
        return min < 10?(sec < 10?"0" + min + ":0" + sec:"0" + min + ":" + sec):(sec < 10?min + ":0" + sec:min + ":" + sec);
    }

    public static String getCurrentTime() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        return sdf.format(Long.valueOf(System.currentTimeMillis()/1000L));

        long currentTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(currentTime);
        String format = formatter.format(date);
        return format;
    }

    public static String date2TimeStamp(String date_str,String format){
                try {
                         SimpleDateFormat sdf = new SimpleDateFormat(format);
                         return String.valueOf(sdf.parse(date_str).getTime()/1000);
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 return "";
             }
}
