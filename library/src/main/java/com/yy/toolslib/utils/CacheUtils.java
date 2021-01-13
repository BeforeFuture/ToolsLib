package com.yy.toolslib.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import java.io.File;

public class CacheUtils {

    private static String TAG = "CacheUtils";

    public final static String DEVICE_INFO = "DEVICE_INFO";//设备信息


    /**
     * 获取保存设备信息的aCache
     * 2020.06.23 之后使用
     * 移动了保存device id的目录，保证公司所有项目访问的同一个设备id
     */
    public static CacheInfo getAcache(Context context) {
        //android 6.0以上，无权限，直接返回null
        if (Build.VERSION.SDK_INT >= 23
                && (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            return null;
        }

        String cacheStr = "";
        if (Build.VERSION.SDK_INT >= 29) { //如果Android 版本高于（或等于）Android 10，则需要更换路径
                File cache = new File((context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/device/"));//+ DEVICE_INFO.hashCode()
                return CacheInfo.get(context, cache, 1000 * 1000 * 50, Integer.MAX_VALUE);
        } else {
            cacheStr = "/data/data/" + context.getPackageName() + "/device/";
            File formFile = new File(cacheStr);
            return CacheInfo.get(context, formFile, 1000 * 1000 * 50, Integer.MAX_VALUE);
        }
    }

}
