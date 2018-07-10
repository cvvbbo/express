package com.emiaoqian.express.utils;

import android.util.Log;



/**
 * Created by xiong on 2017/8/17.
 */

public class LogUtil {

        private static final String TAG = "LogUtil";

        //表示当时是否是debug模式(开发调试模式)，在项目开发完毕之后需要将这个变量置为false
        public static boolean isDebug = true;

        /**
         * 打印d级别的log
         * @param tag
         * @param msg
         */
        public static void d(String tag, String msg){
            if(isDebug){
                Log.d(tag, msg);
            }
        }

        //突破log的字数限制
    public static void logE( String content) {
        int p = 2048;
        long length = content.length();
        if (length < p || length == p)
            Log.e(TAG,content);
        else {
            while (content.length() > p) {
                String logContent = content.substring(0, p);
                content = content.replace(logContent, "");
                Log.e(TAG, logContent);
            }
            Log.e(TAG, content);
        }
    }


    public static void d(String msg){
            if(isDebug){
                Log.d(TAG, msg);
            }
        }

        public static void w(String msg){
        if(isDebug){
            Log.w(TAG, msg);
           }
       }

        public static void e(String tag, String msg){
            if(isDebug){
                Log.e(tag, msg);
            }
        }

        public static void e(String msg){
            if(isDebug){
                Log.e(TAG, msg);
            }
        }
    }

