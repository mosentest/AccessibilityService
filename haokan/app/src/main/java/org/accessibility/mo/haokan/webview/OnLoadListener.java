package org.accessibility.mo.haokan.webview;

import android.webkit.WebResourceError;
import android.webkit.WebView;

/**
 * 作者 create by moziqi on 2018/9/26
 * 邮箱 709847739@qq.com
 * 说明
 **/
public interface OnLoadListener {

//    void onReceivedPageTitle(WebView view, String title);
//
//    void onProgressChanged(WebView view, int newProgress);

    boolean overrideUrlLoading(WebView view, String url);

    void onPageFinished(WebView view, String url);

    void onPageStart(WebView view, String url);

    void onReceivedPageError(WebResourceError error);
}
