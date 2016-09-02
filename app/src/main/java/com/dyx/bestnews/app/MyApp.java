package com.dyx.bestnews.app;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class MyApp extends Application {
    private static MyApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        x.Ext.init(this);
    }

    public static MyApp getApp() {
        return app;
    }
}
