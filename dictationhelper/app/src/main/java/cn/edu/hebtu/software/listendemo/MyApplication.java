package cn.edu.hebtu.software.listendemo;

import android.app.Application;
import android.content.Context;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义 Application
 */

public class MyApplication extends Application {

    private static MyApplication app;
    private Context mContext;

    public static MyApplication getInstance() {
        return app;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        mContext = getApplicationContext();
        JPushInterface.setDebugMode(true);  // 打开调试模式
        JPushInterface.init(this);
    }
}
