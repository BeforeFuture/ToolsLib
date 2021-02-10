package com.yy.toolslib.utils;


import android.content.Context;
import android.content.res.Resources;

public class YyInflaterUtils {

    private static final String TAG = "YyInflaterUtils";

    /**
     * 获取布局文件
     *
     * @param con        上下文
     * @param layoutName 布局文件名称
     * @return
     */
    public static int getLayout(Context con, String layoutName) {
        return getIdByName(con, "layout", layoutName);
    }

    /**
     * 获取控件
     *
     * @param con         上下文
     * @param controlName 控件名称
     * @return
     */
    public static int getControl(Context con, String controlName) {
        return getIdByName(con, "id", controlName);
    }

    /**
     * 获取资源文件
     *
     * @param con          上下文
     * @param drawableName 资源名称
     * @return
     */
    public static int getDrawable(Context con, String drawableName) {
        return getIdByName(con, "drawable", drawableName);
    }

    /**
     * 获取string
     *
     * @param con        上下文
     * @param stringName 获取string名称
     * @return
     */
    public static int getString(Context con, String stringName) {
        return getIdByName(con, "string", stringName);
    }

    /**
     * Refer to external project resources
     *
     * @param context
     * @param className
     * @param name
     * @return
     */
    private static int getIdByName1(Context context, String className,
                                    String name) {
        String packageName = null;
        Class<?> r = null;
        int id = 0;
        try {
            packageName = context.getPackageName();
            //LogUtils.w(TAG, "packageName:"+packageName);
            r = Class.forName(packageName + ".R");
            Class<?>[] classes = r.getClasses();
            Class<?> desireClass = null;
            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }
            if (desireClass != null) {
                id = desireClass.getField(name).getInt(desireClass);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("getIdByName1 ClassNotFoundException---className: " + className + " ; name: " + name);
        } catch (IllegalArgumentException e) {
            System.out.println("getIdByName1 IllegalArgumentException---className: " + className + " ; name: " + name);
        } catch (SecurityException e) {
            System.out.println("getIdByName1 SecurityException---className: " + className + " ; name: " + name);
        } catch (IllegalAccessException e) {
            System.out.println("getIdByName1 IllegalAccessException---className: " + className + " ; name: " + name);
        } catch (NoSuchFieldException e) {
            System.out.println("getIdByName1 NoSuchFieldException---className: " + className + " ; name: " + name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    private static int getIdByName2(Context context, String className,
                                    String name) {
        Resources res = null;
        int id = 0;
        try {
            res = context.getResources();
            id = res.getIdentifier(name, className, context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * @param context   上下文
     * @param className 类名
     * @param name      属性名
     * @return
     */
    public static int getIdByName(Context context, String className, String name) {
        int id = -1;
        int id1 = getIdByName1(context, className, name);
        int id2 = getIdByName2(context, className, name);
        if (id2 != 1 && id2 != 0) {
            id = id2;
        } else if (id1 != 0) {
            id = id1;
        } else {
            LogUtils.e(TAG, "存在SDK找不到的资源文件:" + "className:" + className + ";   name:" + name);
        }
        return id;
    }
}
