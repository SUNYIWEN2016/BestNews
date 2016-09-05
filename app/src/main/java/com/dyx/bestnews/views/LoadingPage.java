package com.dyx.bestnews.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dyx.bestnews.R;
import com.dyx.bestnews.app.MyApp;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2016/9/5 0005.
 */
public abstract class LoadingPage extends FrameLayout {
    public static final int STAT_LOADING = 0;
    public static final int STAT_ERROR = 1;
    public static final int STAT_EMPTY = 2;
    public static final int STAT_SUCESS = 3;
    private int currentState = 3;//当前状态

    private View loadingView;
    private View errorView;
    private View emptyView;
    private View sucessView;

    public LoadingPage(Context context) {
        super(context);
        initViews();
    }

    private void initViews() {
        if (loadingView == null) {
            loadingView = View.inflate(getContext(), R.layout.layout_loading, null);
            addView(loadingView);
        }

        if (errorView == null) {
            errorView = View.inflate(getContext(), R.layout.layout_error, null);
            addView(errorView);
        }

        if (emptyView == null) {
            emptyView = View.inflate(getContext(), R.layout.layout_empty, null);
            addView(emptyView);
        }
        showPage();

    }

    public void startNetWork() {
        String url = getUrl();
        if (url == null) {
            currentState = STAT_SUCESS;
            showPage();
        } else {
            x.http().get(new RequestParams(url), new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    if (TextUtils.isEmpty(result)) {
                        currentState = STAT_EMPTY;
                        Toast.makeText(getContext(), "空", Toast.LENGTH_SHORT).show();
                    } else {
                        currentState = STAT_SUCESS;
                        parseData(result);
                    }
                    showPage();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    currentState = STAT_ERROR;
                    showPage();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {
                }
            });
        }
    }

    protected abstract void parseData(String result);

    protected abstract String getUrl();

    protected abstract void bindView(View sucessView);

    protected abstract int getScucessLayout();

    private void showPage() {

        MyApp.handler.post(new Runnable() {
            @Override
            public void run() {
                loadingView.setVisibility(currentState == STAT_LOADING ? View.VISIBLE : View.INVISIBLE);
                errorView.setVisibility(currentState == STAT_ERROR ? View.VISIBLE : View.INVISIBLE);
                emptyView.setVisibility(currentState == STAT_EMPTY ? View.VISIBLE : View.INVISIBLE);
                if (sucessView == null) {
                    sucessView = View.inflate(getContext(), getScucessLayout(), null);

                    addView(sucessView);
                    //碎片中绑定id
                    bindView(sucessView);

                }
                sucessView.setVisibility(currentState == STAT_SUCESS ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }


}
