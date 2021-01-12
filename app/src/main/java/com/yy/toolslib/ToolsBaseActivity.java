package com.yy.toolslib;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.yy.toolslib.notchlib.NotchScreenManager;
import com.yy.toolslib.utils.CTInflaterUtils;
import com.yy.toolslib.utils.ConvertToTranslucent;

public class ToolsBaseActivity extends FragmentActivity {// Activity

    private final String TAG = "ToolsBaseActivity";
    private NotchScreenManager notchScreenManager = NotchScreenManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 支持显示到刘海区域
        notchScreenManager.setDisplayInNotch(this);

        //设置Translucent为true
        ConvertToTranslucent.convertActivityToTranslucent(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    protected int getLayout(String layoutName) {
        return CTInflaterUtils.getLayout(this, layoutName);
    }

    protected int getId(String idName) {
        return CTInflaterUtils.getControl(this, idName);
    }

    protected int getDrawable(String drawbleName) {
        return CTInflaterUtils.getDrawable(this, drawbleName);
    }

    /**
     * 获取string
     *
     * @param stringName 获取string名称
     * @return
     */
    protected int getString(String stringName) {
        return CTInflaterUtils.getString(this, stringName);
    }

    /**
     * 获取string
     *
     * @param stringName 获取string名称
     * @return
     */
    protected int getStringArrays(String stringName) {
        return CTInflaterUtils.getIdByName(this, "array", stringName);
    }

}
