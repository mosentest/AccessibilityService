package org.accessibility.mo.haokan.webview;

import android.util.Log;
import android.webkit.JavascriptInterface;


public class InJavaScriptLocalObj {

    private WebViewBean entity;

    public void setEntity(WebViewBean entity) {
        this.entity = entity;
    }

    @JavascriptInterface
    public void showSource(String html) {
        Log.i("InJavaScriptLocalObj", entity.url + " = showSource= " + html);
    }
}
 
 
