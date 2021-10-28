package com.yy.toolslib;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebConfig;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.PermissionInterceptor;
import com.just.agentweb.WebChromeClient;
import com.yy.toolslib.agentweb.UIController;
import com.yy.toolslib.jsinterface.AndroidInterface;
import com.yy.toolslib.utils.Logger;
import com.yy.toolslib.utils.YyInflaterUtils;
import com.yy.toolslib.utils.CommonUtils;
import com.yy.toolslib.utils.StatusBarUtil;

import org.json.JSONObject;

import java.util.HashMap;

public class WebViewActivity extends ToolsBaseActivity {

    private LinearLayout webview;
    private ImageView progress_gif_iv;

    private String TAG = "WebViewActivity";

    //AgentWeb
    private AgentWeb mAgentWeb;

    public final String KEY_URL = "url";

    private String url = "";//要展示链接
    private String title = "";//标题

    private TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTransparentForImageViewInFragment(this, null);
        StatusBarUtil.setStatusBarMode(this, true);
        setContentView(getLayout("activity_web_view"));

        webview = findViewById(getId("webview"));
        progress_gif_iv = findViewById(getId("progress_gif_iv"));
        RelativeLayout titleLayout = findViewById(YyInflaterUtils.getControl(this, "title_layout"));
        titleLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        View view = findViewById(YyInflaterUtils.getControl(this, "fakeStatusBarView"));
        view.setBackgroundColor(Color.parseColor("#ffffff"));
        txtTitle = findViewById(YyInflaterUtils.getControl(this, "tv_header_title"));
        txtTitle.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        txtTitle.setTextColor(Color.parseColor("#000000"));
        if (null != getIntent() && getIntent().hasExtra("title")) {
            title = getIntent().getStringExtra("title");
        } else {
            title = getIntent().getStringExtra("game_center");
        }
        txtTitle.setText(title);
        ImageView ivBack = findViewById(YyInflaterUtils.getControl(this, "iv_header_back"));
        ivBack.setImageResource(YyInflaterUtils.getDrawable(this, "ic_back_black"));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        init();
    }

    protected void init() {
        url = getIntent().getStringExtra(KEY_URL);

        Glide.with(this)
                .load(YyInflaterUtils.getDrawable(this, "loading"))
                .asGif()
                .into(progress_gif_iv);

        initWebview();
    }


    /**
     * 初始化webview
     */
    private void initWebview() {
        try {
            mAgentWeb = AgentWeb.with(WebViewActivity.this)//
                    .setAgentWebParent(webview, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
                    .useDefaultIndicator(0, 0)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                    .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                    .setWebViewClient(mWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                    .setWebChromeClient(mWebChromeClient) //WebChromeClient
                    .addJavascriptInterface("chaotoo", new AndroidInterface(WebViewActivity.this, mAgentWeb) {
                        @JavascriptInterface
                        public void copy(String text) {
                            Logger.d( "copy:" + text);
                            ClipboardManager mClipboardManager = (ClipboardManager) WebViewActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                            mClipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));
                            Toast.makeText(WebViewActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
                        }

                        @JavascriptInterface
                        public void openApp() {
                            Logger.d( "openApp:");
                            if (CommonUtils.checkAppInstalled(WebViewActivity.this, "com.chaotoo.gamecenter")) {
                                Logger.d( "已安装");
                                CommonUtils.openApp(WebViewActivity.this, "com.chaotoo.gamecenter");
                            } else {
                                Logger.d( "没有安装");
                                CommonUtils.downLoadGameApp(WebViewActivity.this, "com.chaotoo.gamecenter");
                            }
                        }

                        @JavascriptInterface
                        public void jumpDownApp(String info) {
                            Logger.d( "jumpDownApp: " + info);
                            try {
                                JSONObject jsonObject = new JSONObject(info);
                                if (jsonObject.optInt("type") == 2) {
                                    final Uri uri1 = Uri.parse(jsonObject.optString("linkUrl"));
                                    final Intent it1 = new Intent(Intent.ACTION_VIEW, uri1);
                                    startActivity(it1);
                                } else {
                                    String packageName = jsonObject.optString("packageName");
                                    if (packageName.equals("com.taobao.taobao")) {
                                        String tbPath = jsonObject.optString("linkUrl");
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        Uri uri = Uri.parse(tbPath); // 商品地址
                                        intent.setData(uri);
                                        intent.setClassName("com.taobao.taobao", "com.taobao.tao.detail.activity.DetailActivity");
                                        startActivity(intent);
                                    } else {
                                        if (CommonUtils.checkAppInstalled(WebViewActivity.this, packageName)) {
                                            Logger.d( "已安装");
                                            CommonUtils.openApp(WebViewActivity.this, packageName);
                                        } else {
                                            CommonUtils.downLoadGameApp(WebViewActivity.this, packageName);
                                            Logger.d( "没有安装");
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Logger.d( "jumpDownApp-JSONException" + e.getMessage());
                            }
                        }

                    })//注入JS// new AndroidInterface(WebViewActivity.this, mAgentWeb)
                    .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
                    .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                    .setAgentWebUIController(new UIController(WebViewActivity.this)) //自定义UI  AgentWeb3.0.0 加入。
                    .setMainFrameErrorView(getLayout("agentweb_error_page"), -1) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                    .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                    .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                    .createAgentWeb()//创建AgentWeb。
                    .ready()//设置 WebSettings。
                    .go(url); //WebView载入该url地址的页面并显示。

            // AgentWeb 没有把WebView的功能全面覆盖 ，所以某些设置 AgentWeb 没有提供 ， 请从WebView方面入手设置。
            mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
            mAgentWeb.getWebCreator().getWebView().setVerticalScrollBarEnabled(false);
            mAgentWeb.getWebCreator().getWebView().setHorizontalScrollBarEnabled(false);
            FrameLayout frameLayout = mAgentWeb.getWebCreator().getWebParentLayout();
            //设置webview背景色，为白色
            frameLayout.setBackgroundColor(Color.TRANSPARENT);
            toCleanWebCache();
            Logger.e( "webview-UserAgent：" + mAgentWeb.getAgentWebSettings().getWebSettings().getUserAgentString());
            /**
             * 启用mixed content    android 5.0以上默认不支持Mixed Content
             *
             * 5.0以上允许加载http和https混合的页面(5.0以下默认允许，5.0+默认禁止)
             * */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAgentWeb.getWebCreator().getWebView().getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }

            /**设置适应Html5的一些方法*/
            mAgentWeb.getWebCreator().getWebView().getSettings().setDomStorageEnabled(true);

        } catch (Exception e) {
            Logger.e( "webview报错：" + e.getMessage());
        }
    }


    private com.just.agentweb.WebViewClient mWebViewClient = new com.just.agentweb.WebViewClient() {
        private HashMap<String, Long> timer = new HashMap<>();

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Logger.e( "getDescription：" + error.getDescription());
            Logger.e( "getErrorCode：" + error.getErrorCode());
            Logger.e( "getUrl：" + request.getUrl());
            super.onReceivedError(view, request, error);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return shouldOverrideUrlLoading(view, request.getUrl() + "");
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            //一旦页面中有其他链接被点击了，则显示关闭页面按钮
            Logger.e( "mWebViewClient shouldOverrideUrlLoading:" + url);
            if (url.contains("chaotoo://xxjlb")) {
//                if (!url.startsWith("http")) {
                try {
                    // 以下固定写法
                    final Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } catch (Exception e) {
                    // 防止没有安装的情况
                    e.printStackTrace();
                    Logger.i( "您所打开的第三方App未安装！");
                    CommonUtils.downLoadGameApp(WebViewActivity.this, "com.chaotoo.gamecenter");
                }
                return true;
            }

            return false;
        }

        @Override
        public void onPageStarted(WebView view, String URL, Bitmap favicon) {
//            Logger.e( "mUrl:" + url + " onPageStarted  target:" + getUrl());
            super.onPageStarted(view, URL, favicon);

            timer.put(URL, System.currentTimeMillis());
            if (URL.equals(url)) {
//                pageNavigator(View.GONE);
            } else {
//                pageNavigator(View.VISIBLE);
            }
//            showLoading();
        }

        //        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (timer.get(url) != null) {
                long overTime = System.currentTimeMillis();
                Long startTime = timer.get(url);
            }

            progress_gif_iv.setVisibility(View.GONE);
            webview.setVisibility(View.VISIBLE);

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);

//            Logger.e( "onReceivedHttpError:" + "  request:" + request.getUrl() + "  errorResponse:" + errorResponse.getReasonPhrase());
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //接受证书
            handler.proceed();
        }
    };

    protected WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
//            super.onProgressChanged(view, newProgress);
//            Logger.e( "onProgressChanged:" + newProgress + "  view:" + view);
            if (newProgress >= 99) {
                progress_gif_iv.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String newTitle) {
            super.onReceivedTitle(view, newTitle);
            if (newTitle.length() > 10) {
                newTitle = newTitle.substring(0, 10).concat("...");
            }

            if (!title.equals("用户协议")) {
                txtTitle.setText(newTitle);
            }
        }
    };


    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {

        /**
         * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
         * @param url
         * @param permissions
         * @param action
         * @return true 该Url对应页面请求权限进行拦截 ，false 表示不拦截。
         */
        @Override
        public boolean intercept(String url, String[] permissions, String action) {
//            Logger.e( "mUrl:" + url + "  permission:" + mGson.toJson(permissions) + " action:" + action);

            return false;
        }
    };

    /**
     * 清除 WebView 缓存
     */
    private void toCleanWebCache() {
        if (this.mAgentWeb != null) {
            //清理所有跟WebView相关的缓存 ，数据库， 历史记录 等。
            this.mAgentWeb.clearWebCache();
//            //Toast.makeText(mContext, "已清理缓存", Toast.LENGTH_SHORT).show();//不提示
            //清空所有 AgentWeb 硬盘缓存，包括 WebView 的缓存 , AgentWeb 下载的图片 ，视频 ，apk 等文件。
            AgentWebConfig.clearDiskCache(this);
        }
    }

    /**
     * @return IAgentWebSettings
     */
    public IAgentWebSettings getSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;

            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;

            }

        };
    }


    @Override
    public void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d( "requestCode---  " + requestCode);
        Logger.d( "resultCode---  " + resultCode);
        if (null != data) {
            if (data.hasExtra("type")) {
                Logger.d( "type---  " + data.getStringExtra("type"));
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
