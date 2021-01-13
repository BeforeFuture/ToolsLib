package com.yy.toolslib.utils;

import android.util.Log;

import com.yy.library.BuildConfig;


/**
 * Log封装
 */
public class LogUtils {


    /**
     * TAG根，用于全局过滤Logcat信息，不同开发者可在后面追加自己的信息
     */
    static String ROOT_TAG = "YY_TOOLS_LIB_LOG_";

    static String className;//类名
    static String methodName;//方法名
    static int lineNumber;//行数

    /**
     * 判断是否可以调试
     *
     * @return
     */
    public static boolean isDebuggable() {
        return BuildConfig.DEBUG;
    }

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("================");
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")================:");
        buffer.append(log);
        return buffer.toString();
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

    public static void e(String TAG,String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.e(ROOT_TAG + className, createLog(message));
    }

    public static void i(String TAG,String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.i(ROOT_TAG + className, createLog(message));
    }

    public static void d(String TAG,String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.d(ROOT_TAG + className, createLog(message));
    }

    public static void v(String TAG,String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.v(ROOT_TAG + className, createLog(message));
    }

    public static void w(String TAG,String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.w(ROOT_TAG + className, createLog(message));
    }
    
    
}
