package com.dyx.bestnews.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dyx.bestnews.R;
import com.dyx.bestnews.adapter.NewsTypeAdapter;
import com.dyx.bestnews.base.BaseFragment;
import com.dyx.bestnews.entity.NetEaseType;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class NewsFragment extends BaseFragment {

    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.indicator)
    TabPageIndicator indicator;
    @BindView(R.id.pager)
    ViewPager pager;
    NewsTypeAdapter adapter;

    //新闻首页
    //viewpager
    //分了很多分类，要从网络获取，
    //viewpager indicator
    //新闻列表，recyclerview,多布局，上拉下拉
    @Override
    protected void initData() {
        //先给viewpager设置适配器
        //适配器里要有标题
        //
        //  getList();
        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<NetEaseType.TList> list = (ArrayList<NetEaseType.TList>) bundle.getSerializable("list");
            adapter = new NewsTypeAdapter(getFragmentManager(), list);
            pager.setAdapter(adapter);
            indicator.setViewPager(pager);
            indicator.setVisibility(View.VISIBLE);
        }

    }

    //添加了一个静态的工厂方法，保存传递过来的参数
    public static NewsFragment getInstance(Bundle bundle) {
        NewsFragment nf = new NewsFragment();
        if (bundle != null) {
            nf.setArguments(bundle);
        }
        return nf;
    }


    @Override
    public int getLayoutId() {
        return R.layout.layout_news;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
