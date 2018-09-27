package org.accessibility.mo.haokan;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private Button mBtn_start_haokan;
    private Button mBtn_start_access;

    private RadioGroup mRg;
    private RadioButton mRb_share;
    private RadioButton mRb_comment;

    // End Of Content View Elements

    private void bindViews() {

        mBtn_start_haokan = (Button) findViewById(R.id.btn_start_haokan);
        mBtn_start_access = (Button) findViewById(R.id.btn_start_access);
        mRg = (RadioGroup) findViewById(R.id.rg);
        mRb_share = (RadioButton) findViewById(R.id.rb_share);
        mRb_comment = (RadioButton) findViewById(R.id.rb_comment);
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
    }


}
