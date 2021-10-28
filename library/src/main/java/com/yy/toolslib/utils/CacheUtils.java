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
        File cache = new File((context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/info/"));//+ DEVICE_INFO.hashCode()
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return CacheInfo.get(context, cache, 1000 * 1000 * 50, Integer.MAX_VALUE);
    }

}
