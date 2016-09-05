package com.dyx.bestnews.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.dyx.bestnews.R;
import com.dyx.bestnews.adapter.NewsRecycleAdapter;
import com.dyx.bestnews.base.BaseFragment;
import com.dyx.bestnews.entity.NewsEase;
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
    private String tid;
    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView1;
    NewsRecycleAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void initData() {
        layoutManager = new LinearLayoutManager(getContext());
        Bundle bundle = getArguments();
        if (bundle != null) {
            tid = bundle.getString("tid");
            //组合成url显示
            showSuccessPage();
        }
    }

    @Override
    protected String getRealURL() {
        String url = "http://c.m.163.com/nc/article/list/" + tid + "/0-20.html";
        return url;
    }

    @Override
    protected void parseRealData(String result) {
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
            Toast.makeText(NewsListFragment.this.getContext(), "吐司失败", Toast.LENGTH_SHORT).show();
        }
        adapter = new NewsRecycleAdapter(newslist, getContext());
        recyclerView1.setAdapter(adapter);
//                recyclerView1.addItemDecoration(new MyDecoration());
        recyclerView1.setLayoutManager(layoutManager);

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
