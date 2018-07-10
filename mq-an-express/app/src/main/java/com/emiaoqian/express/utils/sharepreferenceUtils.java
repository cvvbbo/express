package com.emiaoqian.express.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

/**
 * Created by xiong on 2017/8/15.
 */

public class sharepreferenceUtils {

    private static SharedPreferences preferences;

    public static void saveintdata(Context context, String key, int value) {
        if (preferences == null) {
            preferences = context.getSharedPreferences("int", Context.MODE_PRIVATE);
        }
        preferences.edit().putInt(key, value).commit();
    }
    public static void saveStringdata(Context context, String key, String value) {
        if (preferences == null) {
            preferences = context.getSharedPreferences("int", Context.MODE_PRIVATE);
        }
        preferences.edit().putString(key, value).commit();
    }

    public static void saveBooleandata(Context context, String key, boolean value) {
        if (preferences == null) {
            preferences = context.getSharedPreferences("int", Context.MODE_PRIVATE);
        }
        preferences.edit().putBoolean(key, value).commit();
    }
    public static int getintdata(Context context,String key,int value){

        if (preferences==null){
            preferences=context.getSharedPreferences("int",Context.MODE_APPEND);
        }
        return  preferences.getInt(key,value);
    }

    public static boolean getbooleandata(Context context,String key,boolean value){

        if (preferences==null){
            preferences=context.getSharedPreferences("int",Context.MODE_APPEND);
        }
        return  preferences.getBoolean(key,value);
    }

    public static String getStringdata(Context context,String key,String value){

        if (preferences==null){

            preferences=context.getSharedPreferences("int",Context.MODE_APPEND);
        }
        return  preferences.getString(key,value);
    }

    public static void saveHashsetdata(Context context, String key, HashSet<String> value) {
        if (preferences == null) {
            preferences = context.getSharedPreferences("int", Context.MODE_PRIVATE);
        }
        preferences.edit().putStringSet(key, value).commit();
    }

    public static HashSet<String> getHashsetdata(Context context,String key,HashSet<String> value){

        if (preferences==null){

            preferences=context.getSharedPreferences("int",Context.MODE_APPEND);
        }
        return (HashSet<String>) preferences.getStringSet(key,value);
    }

}
