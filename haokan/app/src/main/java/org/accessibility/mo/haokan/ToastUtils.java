package org.accessibility.mo.haokan;

import android.widget.Toast;

/**
 * 作者 create by moziqi on 2018/9/27
 * 邮箱 709847739@qq.com
 * 说明
 **/
public class ToastUtils {
    public static void longToast(String msg) {
        Toast.makeText(App.getContext(), msg, Toast.LENGTH_LONG).show();

    }
    public static void shortToast(String msg) {
        Toast.makeText(App.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
