package com.dyx.bestnews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dyx.bestnews.entity.NetEaseType;
import com.dyx.bestnews.fragments.NewsListFragment;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class NewsTypeAdapter extends FragmentPagerAdapter {

    private List<NetEaseType.TList> titleList;


    //标题集合
    public NewsTypeAdapter(FragmentManager fm, List<NetEaseType.TList> titleList) {
        super(fm);
        this.titleList = titleList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList == null ? "未命名" : titleList.get(position).getTname();
    }

    @Override
    public Fragment getItem(int position) {
        return new NewsListFragment();
    }

    @Override
    public int getCount() {
        return titleList.size();
    }
}
