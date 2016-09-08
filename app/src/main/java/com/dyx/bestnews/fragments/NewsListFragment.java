package com.dyx.bestnews.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.dyx.bestnews.R;
import com.dyx.bestnews.adapter.NewsRecycleAdapter;
import com.dyx.bestnews.base.BaseFragment;
import com.dyx.bestnews.entity.NewsEase;
import com.dyx.bestnews.utils.CommonUrls;
import com.dyx.bestnews.views.RecycleViewDivider;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/8/31 0031.
 */


public class NewsListFragment extends BaseFragment {
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    private String tid;
    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView1;
    NewsRecycleAdapter adapter;
    private LinearLayoutManager layoutManager;
    private boolean isPrepared;//加载是否准备好
    private boolean isVisible;//是否可见
    private boolean isCompleted;//是否已经加载完成。


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {//frahment从不可见到完全可见的时候，会调用该方法
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onInvisible() {

    }

    ;//懒加载的方法,在这个方法里面我们为Fragment的各个组件去添加数据

    protected void onVisible() {
        lazyLoad();
    }

    private void lazyLoad() {
        if (!isPrepared || !isVisible || isCompleted) return;
        showSuccessPage(getRealURL());

    }

    ;//懒加载的方法,在这个方法里面我们为Fragment的各个组件去添加数据

    private NewsRecycleAdapter.OnItemClickListener onItemClickListener = new NewsRecycleAdapter.OnItemClickListener() {
        @Override
        public void itemClick(int viewId, int position) {
            if (viewId == NewsRecycleAdapter.RECYCLER_ITEM) {
                Bundle bundle = new Bundle();
                bundle.putString("docid", adapter.getList().get(position).docid);
                bundle.putString("title",adapter.getList().get(position).title);
                mListener.onFragmentInteraction(viewId, bundle);
            }
        }
    };


    @Override
    protected void initData() {
        isPrepared = true;
        layoutManager = new LinearLayoutManager(getContext());
        Bundle bundle = getArguments();
        adapter = new NewsRecycleAdapter(getContext());
        adapter.setOnItemClickListener(onItemClickListener);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setAdapter(adapter);
        recyclerView1.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL));
        recyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!swipe.isRefreshing()) {
                    int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                        //调用Adapter里的changeMoreStatus方法来改变加载脚View的显示状态为：正在加载...
                        adapter.changeMoreStatus(NewsRecycleAdapter.ISLOADING);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //   adapter.getList().addAll(adapter.getList());
                                adapter.notifyDataSetChanged();
                                //当加载完数据后，再恢复加载脚View的显示状态为：上拉加载更多
                                adapter.changeMoreStatus(NewsRecycleAdapter.NO_MORE_DATA);
                            }
                        }, 3000);
                    }
                }
            }
        });
        //对下拉刷新进行监听，
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //新网址
                showSuccessPage(getRealURL());
            }
        });
        if (bundle != null) {
            tid = bundle.getString("tid");
            //组合成url显示
            lazyLoad();
        }
    }

    @Override
    protected String getRealURL() {
        return CommonUrls.getCommonUrls().getListUrl(tid);
    }

    NewsEase tempEase;

    @Override
    protected void parseRealData(String result) {
        //加载数据完成
        List<NewsEase> newslist = new ArrayList<NewsEase>();
        try {
            JSONArray jsonArray = new JSONObject(result).getJSONArray(tid);
            for (int i = 0; i < jsonArray.length(); i++) {
                NewsEase newsEase = new Gson().fromJson(jsonArray.getString(i), NewsEase.class);
                if (i == 0) {
                    tempEase = newsEase;
                }
                //   if (newsEase.url==null||newsEase.url.equals("null")) continue;
                newslist.add(newsEase);
            }

        } catch (JSONException e) {
            Toast.makeText(NewsListFragment.this.getContext(), result, Toast.LENGTH_SHORT).show();
        }
//        adapter = new NewsRecycleAdapter(newslist, getContext());
//        recyclerView1.setAdapter(adapter);
        adapter.addData(newslist);
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
        isCompleted = true;
    }

    public static NewsListFragment getInstance(Bundle bundle) {
        NewsListFragment fragment = new NewsListFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected int getRealLayout() {
        return R.layout.layout_newslist;
    }
}
