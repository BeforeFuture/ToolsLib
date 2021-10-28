package com.yy.toolslib.utils;

import android.util.Log;

import com.yy.library.BuildConfig;


/**
 * Log封装
 */
public class Logger {


    /**
     * TAG根，用于全局过滤Logcat信息，不同开发者可在后面追加自己的信息
     */
    static String ROOT_TAG = "YY_TOOLS_LIB_LOG_";


    static String className;//类名
    static String methodName;//方法名
    static int lineNumber;//行数

    public static boolean isDebug;


    /**
     * 判断是否可以调试
     *
     * @return
     */
    public static boolean isDebuggable() {
        return isDebug;
    }

    private static String createLog(String log) {
        if (null == log) {
            log = "";
        }

        log = unicodeToUTF_8(log);

        StringBuffer buffer = new StringBuffer();
        buffer.append("");
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append("):");
        buffer.append(log);

        String result = buffer.toString();
        return result;
    }

    /**
     * 获取文件名、方法名、所在行数
     *
     * @param sElements
     */
    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.e(ROOT_TAG, createLog(message));
    }

    public static void i(String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.i(ROOT_TAG, createLog(message));
    }

    public static void d(String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.d(ROOT_TAG, createLog(message));
    }

    public static void v(String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.v(ROOT_TAG, createLog(message));
    }

    public static void w(String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.w(ROOT_TAG, createLog(message));
    }

    public static String unicodeToUTF_8(String src) {
        if (null == src) {
            return null;
        }
        System.out.println("src: " + src);
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < src.length(); ) {
            char c = src.charAt(i);
            if (i + 6 < src.length() && c == '\\' && src.charAt(i + 1) == 'u') {
                String hex = src.substring(i + 2, i + 6);
                try {
                    out.append((char) Integer.parseInt(hex, 16));
                } catch (NumberFormatException nfe) {
                    nfe.fillInStackTrace();
                }
                i = i + 6;
            } else {
                out.append(src.charAt(i));
                ++i;
            }
        }
        return out.toString();

    }
    
}
