package com.dyx.bestnews.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.dyx.bestnews.app.MyApp;

/**
 * Created by Administrator on 2016/9/2 0002.
 */
public class UIUtils {
    public static Context getContext() {
        return MyApp.getApp();
    }

    public static Resources getResource() {
        return getContext().getResources();
    }

    public static int getColor(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResource().getColor(colorId, null);
        } else {
            return getResource().getColor(colorId);
        }
    }

    public static Drawable getDrawable(int drawableId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResource().getDrawable(drawableId, null);
        } else {
            return getResource().getDrawable(drawableId);

        }
    }

    public static View infate(int layoutId) {
        return View.inflate(getContext(), layoutId, null);
    }
}
