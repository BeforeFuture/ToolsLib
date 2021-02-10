package com.yy.toolslib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yy.toolslib.matisse.Matisse;
import com.yy.toolslib.matisse.MimeType;
import com.yy.toolslib.matisse.internal.entity.CaptureStrategy;
import com.yy.toolslib.oaid.DeviceOAID;
import com.yy.toolslib.oaid.IGetter;
import com.yy.toolslib.utils.YyInflaterUtils;
import com.yy.toolslib.utils.GlideImageEngine;
import com.yy.toolslib.utils.LogUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取oaid
        DeviceOAID.with(this).doGet(iGetter);
    }


    IGetter iGetter = new IGetter() {
        @Override
        public void onDeviceIdGetComplete(@NonNull String oaid) {
            LogUtils.d(TAG, "OAID: " + oaid);
        }

        @Override
        public void onDeviceIdGetError(@NonNull Exception exception) {
            LogUtils.i(TAG, "onDeviceIdGetError: " + exception.getMessage());
        }
    };

    /**
     * 跳转url界面
     */
    private void jumpWebView() {
        Intent mIntent = new Intent(this, WebViewActivity.class);
        mIntent.putExtra("title", "详情");
        mIntent.putExtra("url", "https://www.hao123.com/");
        startActivity(mIntent);
    }

    /**
     * 选择图片
     *
     * @param activity       当前view
     * @param MAX            最大选择数量
     * @param requestCode    请求码
     * @param isAllowPreview 是否允许预览
     */
    public static void takePhotoAndSelectPicture(Activity activity, int MAX, int requestCode, boolean isAllowPreview) {
        Matisse.from(activity)
                .isAllowPreview(isAllowPreview)
                .choose(MimeType.ofImage())//, true //参数1 显示资源类型 参数2 是否可以同时选择不同的资源类型 true表示不可以 false表示可以
                .theme(YyInflaterUtils.getIdByName(activity, "style", "CustomView")) //选择主题 默认是蓝色主题，Matisse_Dracula为黑色主题
                .showSingleMediaType(true)//只展示图片
                .countable(true) //是否显示数字
                .capture(true)  //是否可以拍照
                .captureStrategy(//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        new CaptureStrategy(false, activity.getApplicationContext().getPackageName() + ".fileprovider"))
                .maxSelectable(9)  //最大选择资源数量
//                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K)) //添加自定义过滤器
//                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size)) //设置列宽
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) //设置屏幕方向
                .thumbnailScale(0.85f)  //图片缩放比例
                .imageEngine(new GlideImageEngine())//选择图片加载引擎
                .forResult(1);//设置requestcode,开启Matisse主页面
    }

    public void click(View view) {
        takePhotoAndSelectPicture(this, 9, 1, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {// 操作成功了
            switch (requestCode) {
                case 1:// 图库选择图片
                    Message msg = new Message();
                    msg.obj = Matisse.obtainPathResult(data);
                    msg.what = 11;
                    handler.sendMessage(msg);
                    break;
            }
        }
    }

    private List<String> mPaths;//选择回来的图片地址集合

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    mPaths = (List<String>) msg.obj;
                    break;
            }
        }
    };


}
