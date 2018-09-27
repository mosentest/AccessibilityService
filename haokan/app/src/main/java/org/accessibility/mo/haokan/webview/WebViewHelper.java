package org.accessibility.mo.haokan.webview;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * 作者 create by moziqi on 2018/9/26
 * 邮箱 709847739@qq.com
 * 说明
 **/
public class WebViewHelper {

    @TargetApi(11)
    private static final void removeJavascriptInterfaces(WebView webView) {
        try {
            if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
                webView.removeJavascriptInterface("searchBoxJavaBridge_");
                webView.removeJavascriptInterface("accessibility");
                webView.removeJavascriptInterface("accessibilityTraversal");
            }
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
    }

    public static void setDefaultWebSettings(WebView webView, InJavaScriptLocalObj jsBridge) {
        WebSettings webSettings = webView.getSettings();
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        //允许js代码
        webSettings.setJavaScriptEnabled(true);
        //允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(false);
        //禁用放缩
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);
        //禁用文字缩放
        webSettings.setTextZoom(100);
        //10M缓存，api 18后，系统自动管理。
        webSettings.setAppCacheMaxSize(2 * 1024 * 1024);
        //关闭webview中缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //允许缓存，设置缓存位置
        webSettings.setAppCacheEnabled(false);
        //webSettings.setAppCachePath(context.getDir("appcache", 0).getPath());
        //允许WebView使用File协议
        webSettings.setAllowFileAccess(true);
        //不保存密码
        webSettings.setSavePassword(false);
        //设置UA
        webSettings.setUserAgentString(webSettings.getUserAgentString());
        //移除部分系统JavaScript接口
        removeJavascriptInterfaces(webView);
        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //注入js
        if (jsBridge != null) {
            webView.addJavascriptInterface(jsBridge, "local_obj");
        }
    }


    public static void setWebViewClient(WebView webView, OnLoadListener onLoadListener) {
        MeWebViewClient meWebViewClient = new MeWebViewClient();
        meWebViewClient.setListener(onLoadListener);
        webView.setWebViewClient(meWebViewClient);
    }

    public static void loadJs(WebView webView, String jsContent) {
        if (TextUtils.isEmpty(jsContent)) {
            return;
        }
        // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
        final int version = Build.VERSION.SDK_INT;
        String content = "";
        if (version < 19) {
            if (!jsContent.startsWith("javascript:")) {
                content = "javascript:" + jsContent;
            } else {
                content = jsContent;
            }
            Log.i("Main", "content = " + content);
            webView.loadUrl(content);
        } else {
            if (jsContent.startsWith("javascript:")) {
                content = jsContent.replace("javascript:", "");
            } else {
                content = jsContent;
            }
            Log.i("Main", "content = " + content);
            webView.evaluateJavascript(content, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.i("Main", "v" + value);
                }
            });
        }


    }
}
