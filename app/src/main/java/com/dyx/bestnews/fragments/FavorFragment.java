package com.dyx.bestnews.fragments;

import android.widget.TextView;

import com.dyx.bestnews.R;
import com.dyx.bestnews.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class FavorFragment extends BaseFragment {
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void initData() {
        textView.setText("收藏");
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_news;
    }

}
