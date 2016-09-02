package com.dyx.bestnews.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.dyx.bestnews.R;
import com.dyx.bestnews.adapter.NewsRecycleAdapter;
import com.dyx.bestnews.base.BaseFragment;
import com.dyx.bestnews.entity.NewsEase;
import com.dyx.bestnews.utils.UIUtils;
import com.dyx.bestnews.views.MySwipeRefreshLayout;
import com.dyx.bestnews.views.RecycleViewDivider;
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
    @BindView(R.id.swipe)
    MySwipeRefreshLayout swipe;
    NewsRecycleAdapter adapter;
    //1。懒加载，只加载当前页面，不提前加载其他页面
    //2.父类中重复使用了rootView,重复进行inflate，在destoryview中手动移除rootview的父容器
    private boolean isPrepared;//加载是否准备好
    private boolean isVisible;//是否可见
    private boolean isCompleted;//是否已经加载完成。
    private String tid = "";
    private LinearLayoutManager layoutManager;
    private List<NewsEase> tempList = new ArrayList<>();//测试用数据

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

    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isCompleted) return;
        Bundle bundle = getArguments();
        if (bundle != null) {
            tid = bundle.getString("tid");
            //组合成url显示
            gatherData();
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
        layoutManager = new LinearLayoutManager(getContext());
        swipe.setColorSchemeColors(Color.BLUE, Color.RED);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            recyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    loadMore(newState);
                }
            });
        } else {
            recyclerView1.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    loadMore(newState);
                }
            });

        }
        //根据传进来的tid去获取json数据
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //开线程5秒之后告诉它刷新完了。
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addItem(nnn, 0);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipe.setRefreshing(false);
                            }
                        });
                    }
                }).start();

            }
        });
        lazyLoad();

    }

    private void loadMore(int newState) {
        if (!swipe.isRefreshing()) {
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                //调用Adapter里的changeMoreStatus方法来改变加载脚View的显示状态为：正在加载...
                adapter.changeMoreStatus(NewsRecycleAdapter.ISLOADING);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        adapter.addItem(nnn, adapter.getItemCount() - 2);
//                        adapter.notifyItemInserted(adapter.getItemCount() - 2);
                    //    tempList.clear();
                    //    tempList.add(nnn);
                    //    adapter.addDataList(tempList);
//                        if (tempList.size()==0){

//                        }else{
                     //      adapter.addDataList(tempList);
                        adapter.notifyDataSetChanged();
                       //当加载完数据后，再恢复加载脚View的显示状态为：上拉加载更多
                        //  adapter.changeMoreStatus(NewsRecycleAdapter.PULLUP_LOAD_MORE);
//                        }
                        adapter.changeMoreStatus(NewsRecycleAdapter.NO_MORE_DATA);
                    }
                }, 1500);
            }
        }
    }


    private NewsEase nnn;

    private void gatherData() {
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
                        if (i == 0) nnn = newsEase;
                        newslist.add(newsEase);

                    }

                } catch (JSONException e) {
                    Toast.makeText(NewsListFragment.this.getContext(), "吐司失败", Toast.LENGTH_SHORT).show();
                }
                adapter = new NewsRecycleAdapter(newslist, getContext());
                recyclerView1.setAdapter(adapter);
                recyclerView1.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL));
//                recyclerView1.addItemDecoration(new MyDecoration());
                recyclerView1.setLayoutManager(layoutManager);


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


    public static class MyDecoration extends RecyclerView.ItemDecoration {
        int space = 3;
        private Paint paint = new Paint();

        {
            paint.setAntiAlias(true);
            paint.setColor(UIUtils.getColor(R.color.orange));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom += space;
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                View v = parent.getChildAt(i);
                c.drawRect(v.getLeft(), v.getBottom(), parent.getRight(), v.getBottom() + space, paint);
            }
        }
    }
}
