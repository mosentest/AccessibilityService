package org.accessibility.mo.haokan;

import android.app.Application;
import android.content.Context;

/**
 * 作者 create by moziqi on 2018/9/27
 * 邮箱 709847739@qq.com
 * 说明
 **/
public class App extends Application {


    static String[] comment = new String[]{"你是魔鬼吗？我还以为是爸爸去哪儿，大俊说完之后就火了",
            "喜塔腊尔晴 我劝你善良、早上好尔晴除外",
            "没有龙哥的位置？",
            "我生活在2018嘛？图四什么鬼",
            "小猪佩奇身上纹掌声送给社会人",
            "不好意思图4看不懂",
            "难道不是:吃柠檬吧",
            "你家是不是有矿？",
            "老司机带带我",
            "待你长发及腰，我送你一把剪刀。"};
    private static Context context;

    public static int type = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
