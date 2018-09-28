package org.accessibility.mo.haokan;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.accessibility.mo.haokan.webview.InJavaScriptLocalObj;
import org.accessibility.mo.haokan.webview.OnLoadListener;
import org.accessibility.mo.haokan.webview.WebViewBean;
import org.accessibility.mo.haokan.webview.WebViewHelper;

public class MainActivity extends AppCompatActivity {


    private Button mBtn_start_haokan;
    private Button mBtn_start_access;

    private RadioGroup mRg;
    private RadioButton mRb_share;
    private RadioButton mRb_comment;

    private WebView mWebview;


    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    // End Of Content View Elements

    private void bindViews() {

        mBtn_start_haokan = (Button) findViewById(R.id.btn_start_haokan);
        mBtn_start_access = (Button) findViewById(R.id.btn_start_access);
        mRg = (RadioGroup) findViewById(R.id.rg);
        mRb_share = (RadioButton) findViewById(R.id.rb_share);
        mRb_comment = (RadioButton) findViewById(R.id.rb_comment);

        mWebview = (WebView) findViewById(R.id.webview);
    }

    private void bindClick() {

        mBtn_start_haokan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean accessibilitySettingsOn = SystemUtils.isAccessibilitySettingsOn(App.getContext());
                if (!accessibilitySettingsOn) {
                    ToastUtils.shortToast("还没启动辅助功能");
                    return;
                }
                if (App.type == 0) {
                    ToastUtils.shortToast("请选择分享还是评论");
                    return;
                }
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.baidu.haokan", "com.baidu.haokan.app.activity.splash.FakeSplashActivity"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    ToastUtils.shortToast("启动失败");
                }

            }
        });

        mBtn_start_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean accessibilitySettingsOn = SystemUtils.isAccessibilitySettingsOn(App.getContext());
                if (accessibilitySettingsOn) {
                    ToastUtils.shortToast("已经启动");
                } else {
                    //com.baidu.haokan.app.activity.splash.FakeSplashActivity
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                }

            }
        });

        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_share:
                        App.type = 1;
                        break;
                    case R.id.rb_comment:
                        App.type = 2;
                        break;
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        bindClick();
        final WebViewBean webViewBean = new WebViewBean();
        webViewBean.js = new String[]{"document.getElementsByClassName('play-btn')[0].click()",
                "document.getElementsByClassName('icon-btn')[0].click()",
                "document.getElementsByClassName('iconfont icon-home')[0].click()"};
        webViewBean.url = "";
        webViewBean.delay = new long[]{1000, 2000, 3000};
        InJavaScriptLocalObj inJavaScriptLocalObj = new InJavaScriptLocalObj();
        inJavaScriptLocalObj.setEntity(webViewBean);
        WebViewHelper.setDefaultWebSettings(mWebview, inJavaScriptLocalObj);
        WebViewHelper.setWebViewClient(mWebview, new OnLoadListener() {
            @Override
            public boolean overrideUrlLoading(WebView view, String url) {
                Log.i("Main", "overrideUrlLoading = " + url);
                return false;
            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                Log.i("Main", "onPageFinished = " + url);
                for (int i = 0; i < webViewBean.js.length; i++) {
                    final int j = i;
                    long delayTime1 = 0;
                    //看服务器配置，如果服务器配置每个间隔时间，已经叠加上一个间隔的时间，这里就不需要这样处理
                    if (j > 0) {
                        delayTime1 = webViewBean.delay[j - 1];
                    }
                    long delayTime = webViewBean.delay[j];
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            WebViewHelper.loadJs(view, webViewBean.js[j]);
                        }
                    }, delayTime1 + delayTime);
                }
            }

            @Override
            public void onPageStart(WebView view, String url) {
                Log.i("Main", "onPageStart = " + url);
            }

            @Override
            public void onReceivedPageError(WebResourceError error) {
                Log.i("Main", "onReceivedPageError = " + error.toString());
            }
        });
        mWebview.loadUrl(webViewBean.url);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWebview.stopLoading();
    }

    /**
     * https://blog.csdn.net/xuguobiao/article/details/51473016
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebview != null) {
            mWebview.stopLoading();
            mWebview.clearHistory();
            mWebview.clearCache(true);
            mWebview.loadUrl("about:blank");
            mWebview.pauseTimers();
            mWebview.destroy();
            mWebview = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing
        }
    }

    @Override
    public void onAttachedToWindow() {
//        this.getWindow().setType(WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW+4);
        super.onAttachedToWindow();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {//MENU键
            //监控/拦截菜单键
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
