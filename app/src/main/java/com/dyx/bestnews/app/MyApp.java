package com.dyx.bestnews.app;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    x.Ext.init(this);
    }
}
