package com.yy.toolslib.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonUtils {
    private static String TAG = "CommonUtils";

    public static final float SAMLL_DIALOG_WIDTH = 0.88f;//登录弹框一类的宽度,原设计图是0.67733333333333‬f
    public static final float SAMLL_DIALOG_WIDTH_LANDSCAPE = 0.88f;//登录弹框横屏一类的宽度,原设计图是0.67733333333333‬f
    public static final float BIG_DIALOG_WIDTH = 0.928f;//实名认证弹框的宽度,实名认证弹框

    public final static String ACCOUNT_LIST_KEY = "ACCOUNT_LIST_KEY";//保存用户列表的KEY
    public final static String DEVICE_INFO = "DEVICE_INFO";//设备信息

//    public final static String GAME_ACCOUNT_LIST_KEY = "GAME_ACCOUNT_LIST_KEY" + MCApiFactory.getMCApi().getContext().getPackageName();//保存用户列表的KEY,2020.09.02
    public final static String GAME_ACCOUNT_INFO = "GAME_ACCOUNT_INFO";//账户信息
//    public final static String IS_HIDE_FLOATING_DIALOG = "IS_HIDE_FLOATING_DIALOG" + MCApiFactory.getMCApi().getContext().getPackageName();//是否隐藏悬浮窗，1表示隐藏 ，其他值表示显示

    public static long UP_TIME = 0;//上线时间，即登录成功时间点

    private static boolean checkStoragePermission(Context context) {
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public static int dp2px(Context context, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    /**
     * 是否安装了某个APP
     *
     * @param context
     * @param pkgName
     * @return
     */
    public static boolean checkAppInstalled(Context context, String pkgName) {
        if (TextUtils.isEmpty(pkgName)) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
            return true;//true为安装了，false为未安装
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 打开某个APP
     *
     * @param mContext
     * @param packageName 包名
     */
    public static void openApp(Context mContext, String packageName) {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    // 跳转到应用宝的网页版地址
    private final static String WEB_YINGYONGBAO_MARKET_URL = "https://a.app.qq.com/o/simple.jsp?pkgname=";

    public static void downLoadGameApp(Context mContext, String pagekageName) {
        if (checkAppInstalled(mContext, "com.tencent.android.qqdownloader")) {
            try {
                if (TextUtils.isEmpty(pagekageName))
                    return;
                Uri uri = Uri.parse("market://details?id=" + pagekageName);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.tencent.android.qqdownloader");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                goToYingYongBaoWeb(mContext, pagekageName);
            }
        } else {
            goToYingYongBaoWeb(mContext, pagekageName);
        }
    }

    /**
     * 跳转到应用宝网页版
     */
    private static void goToYingYongBaoWeb(Context context, String name) {
        try {
            Uri uri = Uri.parse(WEB_YINGYONGBAO_MARKET_URL + name);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void main(String[] args) throws ParseException {
        String format = "HH:mm:ss";
        Date nowTime = new SimpleDateFormat(format).parse("09:27:00");
        Date startTime = new SimpleDateFormat(format).parse("09:27:00");
        Date endTime = new SimpleDateFormat(format).parse("09:27:59");
        System.out.println(isEffectiveDate(nowTime, startTime, endTime));
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 判断当前系统时间是否在指定时间的范围内
     * <p>
     * //     * @param beginHour 开始小时，例如22
     * //     * @param beginMin  开始小时的分钟数，例如30
     * //     * @param endHour   结束小时，例如 8
     * //     * @param endMin    结束小时的分钟数，例如0
     *
     * @return true表示在范围内，否则false
     */
    public static boolean isCurrentInTimeScope() {//int beginHour, int beginMin, int endHour, int endMin
        boolean result = false;
        final long aDayInMillis = 1000 * 60 * 60 * 24;
        final long currentTimeMillis = System.currentTimeMillis();

        Time now = new Time();
        now.set(currentTimeMillis);

        Time startTime = new Time();
        startTime.set(currentTimeMillis);
//        startTime.hour = beginHour;
//        startTime.minute = beginMin;
        startTime.hour = 22;
        startTime.minute = 0;

        Time endTime = new Time();
        endTime.set(currentTimeMillis);
//        endTime.hour = endHour;
//        endTime.minute = endMin;
        endTime.hour = 8;
        endTime.minute = 0;

        if (!startTime.before(endTime)) {
            // 跨天的特殊情况（比如22:00-8:00）
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
            // 普通情况(比如 8:00 - 14:00)
//            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime

            result = false;
        }
        return result;
    }


    /**
     * 根据身份证号计算年龄
     *
     * @param pensonnelIdCard
     * @return
     */
    public static Integer getPersonAgeFromIdCard(String pensonnelIdCard) {
        if (pensonnelIdCard.length() != 15 && pensonnelIdCard.length() != 18) {
            return 0;
        }

        //截取身份证中出行人出生日期中的年、月、日
        Integer personYear;
        Integer personMonth = Integer.parseInt(pensonnelIdCard.substring(10, 12));
        Integer personDay = Integer.parseInt(pensonnelIdCard.substring(12, 14));

        if (pensonnelIdCard.length() == 15) {
            personYear = 1900 + Integer.parseInt(pensonnelIdCard.substring(6, 8));
        } else {
            personYear = Integer.parseInt(pensonnelIdCard.substring(6, 10));
        }

        Calendar cal = Calendar.getInstance();
        // 得到当前时间的年、月、日
        Integer yearNow = cal.get(Calendar.YEAR);
        Integer monthNow = cal.get(Calendar.MONTH) + 1;
        Integer dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去生日年月日
        Integer yearMinus = yearNow - personYear;
        Integer monthMinus = monthNow - personMonth;
        Integer dayMinus = dayNow - personDay;

        Integer age = yearMinus; //先大致赋值

        if (yearMinus == 0) { //出生年份为当前年份
            age = 0;
        } else { //出生年份大于当前年份
            if (monthMinus < 0) {//出生月份小于当前月份时，还没满周岁
                age = age - 1;
            }
            if (monthMinus == 0) {//当前月份为出生月份时，判断日期
                if (dayMinus < 0) {//出生日期小于当前月份时，没满周岁
                    age = age - 1;
                }
            }
        }
        return age;
    }


    /****************
     *
     * 发起添加群流程。群号：老瘪犊子特战队(618468579) 的 key 为： -iDSSU204FAvYFYYOdRLd3yaRtdSOSaZ
     * 调用 joinQQGroup(-iDSSU204FAvYFYYOdRLd3yaRtdSOSaZ) 即可发起手Q客户端申请加群 老瘪犊子特战队(618468579)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    public static boolean joinQQGroup(Activity activity, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Activity context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        context.getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        int widthPixel = outMetrics.widthPixels;
        int heightPixel = outMetrics.heightPixels;

//        LogUtils.i(TAG, "getScreenHeight-height:" + heightPixel);
        return heightPixel;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen.xml");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;

    }


}
