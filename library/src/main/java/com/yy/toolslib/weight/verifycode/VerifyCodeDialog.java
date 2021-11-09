package com.yy.toolslib.weight.verifycode;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yy.toolslib.callback.VerifyCodeCallback;
import com.yy.toolslib.utils.YyInflaterUtils;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 适用方式：
 * <p>
 * VerifyCodeDialog.showVrifyCodeDialog(this, new VerifyCodeCallback() {
 *
 * @Override public void onSuccess() {
 * Log.d(TAG,"onSuccess");
 * }
 * @Override public void onFial() {
 * Log.d(TAG,"onFial");
 * <p>
 * }
 * });
 */
public class VerifyCodeDialog {

    private static TextView drag_tv_tips, result_tv;
    private static LinearLayout refresh_ll;
    private static Handler handler;
    private static View vFlash;
    private static long timeTemp;
    private static float timeUse;
    private static VerifyCodeView verifyCodeView;
    private static SeekBar mSeekBar;
    public static VerifyCodeCallback callback;

    public static void showVrifyCodeDialog(final Context mContext, VerifyCodeCallback codeCallback) {
        callback = codeCallback;
        handler = new Handler();
        final Dialog lDialog = new Dialog(mContext);
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = lDialog.getWindow();
        if (null != window) {
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.setWindowAnimations(YyInflaterUtils.getStyle(mContext, "YYCustomDialogVerifyCode"));
        }
        lDialog.setContentView(YyInflaterUtils.getLayout(mContext, "dialog_yy_verify_code_layout"));
        initDialogView(lDialog, mContext);

        WindowManager wm = window.getWindowManager();
        Point windowSize = new Point();
        wm.getDefaultDisplay().getSize(windowSize);
        float size_x = 0;
        float size_y = 0;
        int width = windowSize.x;
        int height = windowSize.y;
        if (width >= height) {// 横屏
            size_x = (0.88f - 0.05f);
            window.getAttributes().width = (int) (windowSize.y * size_x);
            window.getAttributes().height = WRAP_CONTENT;//(int) (windowSize.y * size_y);
//            params.width =
        } else {// 竖屏
            size_x = (0.88f - 0.05f);
            window.getAttributes().width = (int) (windowSize.x * size_x);
            window.getAttributes().height = WRAP_CONTENT;//(int) (windowSize.x * size_y);
        }
        window.setGravity(Gravity.CENTER);

        lDialog.setCancelable(true);
        lDialog.setCanceledOnTouchOutside(false);
        lDialog.show();
    }

    private static void initDialogView(final Dialog containerView, Context mContent) {
        verifyCodeView = containerView.findViewById(YyInflaterUtils.getControl(mContent, "dy_v"));
        mSeekBar = containerView.findViewById(YyInflaterUtils.getControl(mContent, "seekBar"));

        verifyCodeView.setImageResource(YyInflaterUtils.getDrawable(mContent, "bg_verify_code_1"));

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                verifyCodeView.setUnitMoveDistance(verifyCodeView.getAverageDistance(seekBar.getMax()) * progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                timeTemp = System.currentTimeMillis();
                tips2ShowAnime(false);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                timeUse = (System.currentTimeMillis() - timeTemp) / 1000.f;
                verifyCodeView.testPuzzle();
            }
        });

        verifyCodeView.setPuzzleListener(new VerifyCodeView.onPuzzleListener() {
            @Override
            public void onSuccess() {
                resultTvShowAnime(true);
                flashShowAnime();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSeekBar.setProgress(0);
                        verifyCodeView.reSet();

                        tips2ShowAnime(true);
                        resultTvHideAnime();
                        if (null != callback) {
                            callback.onSuccess();
                        }
                        containerView.dismiss();
                    }
                }, 1200);

            }

            @Override
            public void onFail() {
                resultTvShowAnime(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSeekBar.setProgress(0);
                        verifyCodeView.reSet();

                        tips2ShowAnime(true);
                        resultTvHideAnime();
                        if (null != callback) {
                            callback.onFial();
                        }
                    }
                }, 1750);
            }
        });

        drag_tv_tips = (TextView) containerView.findViewById(YyInflaterUtils.getControl(mContent, "drag_tv_tips"));
        result_tv = (TextView) containerView.findViewById(YyInflaterUtils.getControl(mContent, "result_tv"));
        vFlash = (View) containerView.findViewById(YyInflaterUtils.getControl(mContent, "drag_v_flash"));
        refresh_ll = (LinearLayout) containerView.findViewById(YyInflaterUtils.getControl(mContent, "refresh_ll"));

        refresh_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重置
                mSeekBar.setProgress(0);
                verifyCodeView.reSet();
            }
        });
    }

    //滑块提示文本显示隐藏
    private static void tips2ShowAnime(boolean isShow) {
        if ((drag_tv_tips.getVisibility() == View.VISIBLE) == isShow)
            return;
        AlphaAnimation translateAnimation = new AlphaAnimation(isShow ? 0 : 1, isShow ? 1 : 0);
        translateAnimation.setDuration(333);
        //translateAnimation.setInterpolator(new LinearInterpolator());
        drag_tv_tips.setAnimation(translateAnimation);
        drag_tv_tips.setVisibility(isShow ? View.VISIBLE : View.GONE);


    }

    //滑动结果提示文本显示
    private static void resultTvShowAnime(boolean isSuccess) {
        if (isSuccess) {
            int penset = (int) (99 - (timeUse > 1 ? timeUse - 1 : 0) / 0.1f);
            if (penset < 1) penset = 1;
            result_tv.setText(String.format("拼图成功: 耗时%.1f秒,打败了%d%%的用户!", timeUse, penset));
        } else {
            result_tv.setText("拼图失败: 请重新拖曳滑块到正确的位置!");
        }
        AlphaAnimation translateAnimation2 = new AlphaAnimation(0, 1);
        translateAnimation2.setDuration(333);
        result_tv.setAnimation(translateAnimation2);
        result_tv.setBackgroundColor(Color.parseColor(isSuccess ? "#007500" : "#FF3030")); //"#EA0000"
        result_tv.setVisibility(View.VISIBLE);
    }

    //滑动结果提示文本显示
    private static void resultTvHideAnime() {
        AlphaAnimation translateAnimation2 = new AlphaAnimation(1, 0);
        translateAnimation2.setDuration(333);
        result_tv.setAnimation(translateAnimation2);
        result_tv.setVisibility(View.GONE);
    }

    //成功高亮动画
    private static void flashShowAnime() {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, -1f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);
        translateAnimation.setDuration(800);
        //translateAnimation.setInterpolator(new LinearInterpolator());
        vFlash.setAnimation(translateAnimation);
        vFlash.setVisibility(View.VISIBLE);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vFlash.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
