package cn.share;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by luo on 2017/3/21.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
