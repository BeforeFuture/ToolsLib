package com.yy.toolslib.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.yy.toolslib.model.DeviceInfoModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class DeviceInfo {

    private static final String TAG = "DeviceInfo";

    /**
     * 读取手机状态权限的请求
     */
    public static final int MCH_READ_PHONE_STATE_REQUEST_CODE = 0x01;


    /**
     * 获取手机状态栏高度
     */
    public static int getStatusBarHeight(Context mContext) {
        // 获取手机上部菜单高度
        Resources resources = mContext.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen",
                "android");
        int height = resources.getDimensionPixelSize(resourceId);
//        CTLog.v( "fun#getStatusBarHeight height:" + height);
        return height;
    }

    /**
     * 手机屏幕高度
     */
    public static int getScreenHeight(Context mContext) {
        return getWindowManager(mContext).getDefaultDisplay().getHeight();
    }

    /**
     * 手机屏幕宽度
     */
    public static int getScreenWidth(Context mContext) {
        return getWindowManager(mContext).getDefaultDisplay().getWidth();
    }

    /**
     * 或得WindowManager
     */
    @SuppressLint("WrongConstant")
    public static WindowManager getWindowManager(Context mContext) {
        return (WindowManager) mContext.getSystemService("window");
    }

    /**
     * 判断当前手机屏幕是否全屏
     */
    public static boolean isFullScream(Activity mContext) {
        int v = mContext.getWindow().getAttributes().flags;
//        Logger.v( mContext.getClass().getSimpleName() + ":" + v
//                + "// 全屏 66816 - 非全屏 65792");
        if (66816 == v) {
            return true;
        } else if (65792 == v) {
            return false;
        } else {
            return false;
        }
    }

    /**
     * 判断当前手机屏幕是否全屏，可在service中使用
     */
    public static boolean isFullScream(Context mContext) {
        View mCheckFullScreenView = new View(mContext);
        mCheckFullScreenView.setBackgroundColor(Color.parseColor("#00000000"));
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(mContext.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 创建非模态、不可碰触
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        // 放在左上角
        params.gravity = Gravity.START | Gravity.TOP;
        params.height = 1;
        params.width = 1;
        // 设置弹出View类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        windowManager.addView(mCheckFullScreenView, params);
        return isFullscreen(mCheckFullScreenView);
    }

    /**
     * Check if fullscreen is activated by a position of a top left View
     *
     * @param topLeftView View which position will be compared with 0,0
     */
    public static boolean isFullscreen(View topLeftView) {
        int location[] = new int[2];
        topLeftView.getLocationOnScreen(location);
        return location[0] == 0 && location[1] == 0;
    }

    /**
     * 判断当前屏幕是否是横屏
     */
    public static boolean isVerticalScreen(Activity activity) {
        int flag = activity.getResources().getConfiguration().orientation;
        if (flag == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 设置全屏
     */
    public static void setFullScream(Activity mContext) {
        mContext.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 取消全屏
     */
    public static void cancelFullScream(Activity mContext) {
        mContext.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 判断微信是否可用
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }


    public static boolean checkApkExist(Context con, String packname) {
        if (TextUtils.isEmpty(packname)) {
            return false;
        }
        try {
            con.getPackageManager().getApplicationInfo(packname,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
//            Logger.w( "fun#checkApkExist app " + packname + " exist");
            return true;
        } catch (NameNotFoundException e) {
//            Logger.e( "fun#checkApkExist NameNotFoundException:" + e);
        }
        return false;
    }

    // public boolean checkApkExist(Context context, String packageName) {
    // if (packageName == null || “”.equals(packageName))
    // return false;
    // try {
    // ApplicationInfo info = context.getPackageManager()
    // .getApplicationInfo(packageName,
    // PackageManager.GET_UNINSTALLED_PACKAGES);
    // return true;
    // } catch (NameNotFoundException e) {
    // return false;
    // }
    // }


    /**
     * Android  6.0 之前（不包括6.0）
     * 必须的权限  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     */
    private static String getMacDefault(Context context) {
        String mac = "02:00:00:00:00:00";
        if (context == null) {
            return mac;
        }
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) {
            return mac;
        }
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
        }
        if (info == null) {
            return null;
        }
        mac = info.getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }
        return mac;
    }

    /**
     * Android 6.0（包括） - Android 7.0（不包括）
     */
    private static String getMacFromFile() {
        String WifiAddress = "02:00:00:00:00:00";
        try {
            WifiAddress = new BufferedReader(new FileReader(new File("/sys/class/net/wlan0/address"))).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return WifiAddress;
    }

    /**
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET" />
     */
    private static String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取MAC地址
     */
    public static String getMacAddress(Context context) {
        String mac = "00:00:00:00:00:00";
        CacheInfo cacheInfo = CacheUtils.getAcache(context);
        if (null == cacheInfo) {
            return mac;
        }

        DeviceInfoModel cacheModel = (DeviceInfoModel) cacheInfo.getAsObject(CacheUtils.DEVICE_INFO);
        if (null != cacheModel && null != cacheModel.getMAC() && !cacheModel.getMAC().equals("")) {
            return cacheModel.getMAC();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//android 小于6.0
            mac = getMacDefault(context);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {//Android大于6.0，且小于7.0
            mac = getMacFromFile();
        } else {//Android大于7.0
            mac = getMacFromHardware();
        }

        if (null == cacheModel) {
//            CTLog.i( "null == DeviceInfoModel");
            cacheModel = new DeviceInfoModel();
        }

        cacheModel.setMAC(mac);
        cacheInfo.put(CacheUtils.DEVICE_INFO, cacheModel);

        return mac;
    }


    private static boolean checkReadPhoneStatePermission(Context context) {
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE},
                        MCH_READ_PHONE_STATE_REQUEST_CODE);
                return false;
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getMEID(Context context) {
        CacheInfo cacheInfo = CacheUtils.getAcache(context);
        if (null == cacheInfo) {
            return deviceMEID(context);
        }
        DeviceInfoModel cacheModel = (DeviceInfoModel) cacheInfo.getAsObject(CacheUtils.DEVICE_INFO);
        if (null != cacheModel && null != cacheModel.getMEID() && !cacheModel.getMEID().equals("")) {
            return cacheModel.getMEID();
        }

        String meid = TextUtils.isEmpty(deviceMEID(context)) ? "" : deviceMEID(context);

        if (null == cacheModel) {
            cacheModel = new DeviceInfoModel();
            cacheModel.setMEID(meid);
            cacheInfo.put(CacheUtils.DEVICE_INFO, cacheModel);
        }
        return meid;
    }

    private static String deviceMEID(Context context) {
        String meid = "";
        if (Build.VERSION.SDK_INT < 29 && checkReadPhoneStatePermission(context)) {
            TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != mTelephonyMgr) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    meid = mTelephonyMgr.getMeid();
                } else {
                    meid = mTelephonyMgr.getDeviceId();
                }
            }
        }
        return meid;

    }

    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        CacheInfo cacheInfo = CacheUtils.getAcache(context);
        if (null == cacheInfo) {
            return deviceIMEI_1(context);
        }
        DeviceInfoModel cacheModel = (DeviceInfoModel) cacheInfo.getAsObject(CacheUtils.DEVICE_INFO);
        if (null != cacheModel && null != cacheModel.getIMEI_1() && !cacheModel.getIMEI_1().equals("")) {
            return cacheModel.getIMEI_1();
        }

        String imei_1 = TextUtils.isEmpty(deviceIMEI_1(context)) ? "" : deviceIMEI_1(context);

        if (null == cacheModel) {
            cacheModel = new DeviceInfoModel();
            cacheModel.setIMEI_1(imei_1);
            cacheInfo.put(CacheUtils.DEVICE_INFO, cacheModel);
        }

        return imei_1;
    }

    private static String deviceIMEI_1(Context context) {
        String imei1 = "";
        if (Build.VERSION.SDK_INT < 29 && checkReadPhoneStatePermission(context)) {
            TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != mTelephonyMgr) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imei1 = mTelephonyMgr.getImei(0);
                } else {
                    try {
                        return getDoubleImei(mTelephonyMgr, "getDeviceIdGemini", 0);
                    } catch (Exception e) {
                        try {
                            return getDoubleImei(mTelephonyMgr, "getDeviceId", 0);
                        } catch (Exception ex) {
                            return "";
                        }
                    }
                }
            }
        }
        return imei1;
    }


    @SuppressLint("MissingPermission")
    public static String getIMEI2(Context context) {
        CacheInfo cacheInfo = CacheUtils.getAcache(context);
        if (null == cacheInfo) {
            return deviceIMEI_2(context);
        }

        DeviceInfoModel cacheModel = (DeviceInfoModel) cacheInfo.getAsObject(CacheUtils.DEVICE_INFO);

        if (null != cacheModel && null != cacheModel.getIMEI_2() && !cacheModel.getIMEI_2().equals("")) {
            return cacheModel.getIMEI_2();
        }

        String imei_2 = TextUtils.isEmpty(deviceIMEI_2(context)) ? "" : deviceIMEI_2(context);

        if (null == cacheModel) {
            cacheModel = new DeviceInfoModel();
            cacheModel.setIMEI_2(imei_2);
            cacheInfo.put(CacheUtils.DEVICE_INFO, cacheModel);
        }
        return imei_2;
    }


    private static String deviceIMEI_2(Context context) {
        String imei2 = "";
        if (Build.VERSION.SDK_INT < 29 && checkReadPhoneStatePermission(context)) {
            TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != mTelephonyMgr) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return mTelephonyMgr.getImei(1);
                } else {
                    try {
                        return getDoubleImei(mTelephonyMgr, "getDeviceIdGemini", 1);
                    } catch (Exception e) {
                        try {
                            return getDoubleImei(mTelephonyMgr, "getDeviceId", 1);
                        } catch (Exception ex) {
                            return "";
                        }
                    }
                }
            }
        }
        return imei2;
    }


    /**
     * 获取双卡手机的imei
     */
    private static String getDoubleImei(TelephonyManager telephony, String predictedMethodName, int slotID) throws Exception {
        String inumeric = null;

        Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
        Class<?>[] parameter = new Class[1];
        parameter[0] = int.class;
        Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);
        Object[] obParameter = new Object[1];
        obParameter[0] = slotID;
        Object ob_phone = getSimID.invoke(telephony, obParameter);
        if (ob_phone != null) {
            inumeric = ob_phone.toString();
        }
        return inumeric;
    }

    /**
     * 获取设备唯一标识符
     * 用作内部唯一设备标识
     * 使用MAC + （IMEI + IMEI2）/  oaid + ANDROID_ID然后md5
     * 不使用MEID是因为获取到的MEID可能会和IMEI以及IMEI2中的某个相同
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        CacheInfo cacheInfo = CacheUtils.getAcache(context);
        //用于生成最终的唯一标识符
        if (null == cacheInfo) {
            return deviceId(context);
        }

        DeviceInfoModel cacheModel = (DeviceInfoModel) cacheInfo.getAsObject(CacheUtils.DEVICE_INFO);

        if (null != cacheModel && null != cacheModel.getMD5_DEVICE_ID() && !cacheModel.getMD5_DEVICE_ID().equals("")) {
            return cacheModel.getMD5_DEVICE_ID();
        }

        if (null == cacheModel) {
            cacheModel = new DeviceInfoModel();
            cacheModel.setMD5_DEVICE_ID(deviceId(context));
            //持久化操作, 进行保存到SD卡中
            cacheInfo.put(CacheUtils.DEVICE_INFO, cacheModel);
        }

        return deviceId(context);
    }

    /**
     * 创建设备ID
     *
     * @return
     */
    private static String deviceId(Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        String uuid = "";

        //读取保存的在sd卡中的唯一标识符
        String macAddress = "";
        String imei = "";
        String imei2 = "";
        String androidId = "";

        //获取设备的MACAddress地址 去掉中间相隔的冒号
        macAddress = getMacAddress(context).replace(":", "");
        if (null != macAddress && !"".equals(macAddress)) {
            stringBuffer.append(macAddress);
        }

        if (Build.VERSION.SDK_INT >= 29) {
//            stringBuffer.append(MCApiFactory.getMCApi().getDeviceOaid());
        } else {
            //获取IMEI
            imei = getIMEI(context);
            if (null != imei && !"".equals(imei)) {
                stringBuffer.append(imei);
            }
            //获取IMEI2
            imei2 = getIMEI2(context);
            if (null != imei2 && !"".equals(imei2)) {
                stringBuffer.append(imei2);
            }
        }

        //获取AndroidId
        androidId = getAndroidId(context);
        if (null != androidId && !"".equals(androidId)) {
            stringBuffer.append(androidId);
        }

        //如果以上都没有获取到，则自己生成相应的UUID作为相应设备唯一标识符
        if (stringBuffer.length() <= 0) {
            UUID randomUUID = UUID.randomUUID();
            uuid = randomUUID.toString().replace("-", "");
            stringBuffer.append(uuid);
        }

        //为了统一格式对设备的唯一标识进行md5加密 最终生成32位字符串
        return getMD5(stringBuffer.toString(), false);
    }

    /**
     * 对挺特定的 内容进行 md5 加密
     *
     * @param message   加密明文
     * @param upperCase 加密以后的字符串是是大写还是小写  true 大写  false 小写
     * @return
     */
    private static String getMD5(String message, boolean upperCase) {
        String md5str = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] input = message.getBytes();

            byte[] buff = md.digest(input);

            md5str = bytesToHex(buff, upperCase);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }


    public static String bytesToHex(byte[] bytes, boolean upperCase) {
        StringBuffer md5str = new StringBuffer();
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        if (upperCase) {
            return md5str.toString().toUpperCase();
        }
        return md5str.toString().toLowerCase();
    }

    public static String getAndroidId(Context context) {
        CacheInfo cacheInfo = CacheUtils.getAcache(context);
        if (null == cacheInfo) {
            return createAndroidID(context);
        }

        DeviceInfoModel cacheModel = (DeviceInfoModel) cacheInfo.getAsObject(CacheUtils.DEVICE_INFO);

        if (null != cacheModel && !cacheModel.getANDROID_ID().equals("")) {
            return cacheModel.getANDROID_ID();
        }

        if (null == cacheModel) {
            cacheModel = new DeviceInfoModel();
            cacheModel.setANDROID_ID(createAndroidID(context));
            cacheInfo.put(CacheUtils.DEVICE_INFO, cacheModel);
        }

        return createAndroidID(context);
    }

    private static String createAndroidID(Context context) {
        String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        if (null != ANDROID_ID && !"".equals(ANDROID_ID)) {
            return ANDROID_ID;
        } else {
            return UUID.randomUUID().toString().replace("-", "");
        }
    }

}
