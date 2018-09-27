package org.accessibility.mo.haokan.webview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 作者 create by moziqi on 2018/9/26
 * 邮箱 709847739@qq.com
 * 说明
 **/
public class MeWebViewClient extends WebViewClient {
    private OnLoadListener listener;


    public void setListener(OnLoadListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (listener != null) {
            listener.onPageStart(view, url);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        WebViewHelper.loadJs(view, "javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML);");
        if (listener != null) {
            listener.onPageFinished(view, url);
        }
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        if (listener != null) {
            listener.onReceivedPageError(error);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (null != listener) {
            if (listener.overrideUrlLoading(view, url))
                return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }
}