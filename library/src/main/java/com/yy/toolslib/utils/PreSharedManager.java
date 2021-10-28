package com.yy.toolslib.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;


public class PreSharedManager {

    private static final String PRE_NAME = "userInfo";

    /**
     * 得到string值
     *
     * @param key
     * @param context
     * @return
     */
    public static String getString(String key, Context context) {
        return context.getSharedPreferences(PRE_NAME,
                Context.MODE_PRIVATE).getString(key, "");
    }

    /**
     * 得到long值
     *
     * @param key
     * @param context
     * @return
     */
    public static long getLong(String key, Context context) {
        return context.getSharedPreferences(PRE_NAME,
                Context.MODE_PRIVATE).getLong(key, 0);
    }

    /**
     * 得到boolean值
     *
     * @param key
     * @param context
     * @param defValue
     * @return
     */
    public static boolean getBooleaen(String key, Context context, boolean defValue) {
        return context.getSharedPreferences(PRE_NAME,
                Context.MODE_PRIVATE).getBoolean(key, true);
    }

    /**
     * 得到int值
     *
     * @param key
     * @param context
     * @param defValue
     * @return
     */
    public static int getInt(String key, Context context, int defValue) {
        return context.getSharedPreferences(PRE_NAME,
                Context.MODE_PRIVATE).getInt(key, defValue);
    }

    /**
     * 设置string值
     *
     * @param key
     * @param value
     * @param context
     */
    public static void setString(String key, String value, Context context) {
        Editor editor = context.getSharedPreferences(PRE_NAME,
                Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }


    /**
     * 设置long值
     *
     * @param key
     * @param value
     * @param context
     */
    public static void setLong(String key, Long value, Context context) {
        Editor editor = context.getSharedPreferences(PRE_NAME,
                Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
//		editor.putString(key, value);
        editor.apply();
    }

    /**
     * 设置int值
     *
     * @param key
     * @param value
     * @param context
     */
    public static void setInt(String key, int value, Context context) {
        Editor editor = context.getSharedPreferences(PRE_NAME,
                Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 设置boolean值
     *
     * @param key
     * @param value
     * @param context
     */
    public static void setBoolean(String key, boolean value, Context context) {
        Editor editor = context.getSharedPreferences(PRE_NAME,
                Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 2020.06.30 新增
     *
     * @param context
     */
    public static void clearSharedPerferences(Context context) {
        Editor editor = context.getSharedPreferences(PRE_NAME,
                Context.MODE_PRIVATE).edit();
        //清空数据
        editor.clear();
        editor.apply();
    }
}
