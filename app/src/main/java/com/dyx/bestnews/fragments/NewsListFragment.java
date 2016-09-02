package com.dyx.bestnews.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.dyx.bestnews.R;
import com.dyx.bestnews.adapter.NewsRecycleAdapter;
import com.dyx.bestnews.base.BaseFragment;
import com.dyx.bestnews.entity.NewsEase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class NewsListFragment extends BaseFragment {
    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView1;
    //1。懒加载，只加载当前页面，不提前加载其他页面
    //2.父类中重复使用了rootView,重复进行inflate，在destoryview中手动移除rootview的父容器
    private boolean isPrepared;//加载是否准备好
    private boolean isVisible;//是否可见
    private boolean isCompleted;//是否已经加载完成。

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {//frahment从不可见到完全可见的时候，会调用该方法
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("newListFragment:", "setUserVisibleHint: ");
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isCompleted) return;
        Bundle bundle = getArguments();
        if (bundle != null) {
            String tid = bundle.getString("tid");
            //组合成url显示
            gatherData(tid);
        }
    }

    protected void onInvisible() {

    }

    ;//懒加载的方法,在这个方法里面我们为Fragment的各个组件去添加数据

    protected void onVisible() {
        lazyLoad();
    }

    @Override
    protected void initData() {
        isPrepared = true;
        //根据传进来的tid去获取json数据
        lazyLoad();
    }


    private void gatherData(final String tid) {
        String url = "http://c.m.163.com/nc/article/list/" + tid + "/0-20.html";

        x.http().get(new RequestParams(url), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String tname = getArguments().getString("tname");
                //加载数据完成
                List<NewsEase> newslist = new ArrayList<NewsEase>();
                try {
                    JSONArray jsonArray = new JSONObject(result).getJSONArray(tid);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        NewsEase newsEase = new Gson().fromJson(jsonArray.getString(i), NewsEase.class);
                        newslist.add(newsEase);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NewsRecycleAdapter adapter = new NewsRecycleAdapter(newslist, getContext());
                recyclerView1.setAdapter(adapter);
                recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
                isCompleted = true;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static NewsListFragment getInstance(Bundle bundle) {
        NewsListFragment fragment = new NewsListFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_newslist;
    }


    public static class MyDecoration extends RecyclerView.ItemDecoration{

    }
}
