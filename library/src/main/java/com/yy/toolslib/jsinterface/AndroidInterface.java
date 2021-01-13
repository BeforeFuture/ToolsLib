package com.yy.toolslib.jsinterface;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.just.agentweb.AgentWeb;
import com.yy.toolslib.utils.CommonUtils;

/**
 * JS回调Android 接口类
 */

public class AndroidInterface {
    private Handler deliver = new Handler(Looper.getMainLooper());
    private Context mContext;
    private String TAG = "AndroidInterface";
    Activity activity;
    AgentWeb agentWeb;

    public AndroidInterface(Activity activity, AgentWeb agentWeb) {
        this.mContext = activity;
        this.activity = activity;
        this.agentWeb = agentWeb;
    }

    public AndroidInterface(Activity activity) {
        this.mContext = activity;
        this.activity = activity;
    }


    /**
     * 复制字符串
     *
     * @param text
     */
    @JavascriptInterface
    public void copy(String text) {
        ClipboardManager mClipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));
        Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 打开APP
     */
    @JavascriptInterface
    public void openApp() {
        if (CommonUtils.checkAppInstalled(mContext, "com.chaotoo.gamecenter")) {
            CommonUtils.openApp(mContext, "com.chaotoo.gamecenter");
            //跳转到具体的activity，前面的是包名，后面的是具体的activity，前提是要这个activity开了权限，否则会报错
//                    //CommonUtils.openAppWithActivity(mContext, "com.gobest.hngh", "com.gobest.hngh.activity.login.LoginActivity");
        } else {
//            CommonUtils.downLoadGameApp(mContext, "com.chaotoo.gamecenter");
            final Uri uri1 = Uri.parse("https://www.pgyer.com/xxjlb");
            final Intent it1 = new Intent(Intent.ACTION_VIEW, uri1);
            mContext.startActivity(it1);
//            mWebView.loadUrl("https://www.pgyer.com/app/install/83c807e4f2acf0c885c9048de7d83c8e");
        }
    }

    @JavascriptInterface
    public void jumpDownApp(String info) {

    }

}
