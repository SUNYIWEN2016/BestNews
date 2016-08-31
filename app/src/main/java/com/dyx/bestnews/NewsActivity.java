package com.dyx.bestnews;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.dyx.bestnews.base.BaseFragment;
import com.dyx.bestnews.fragments.FavorFragment;
import com.dyx.bestnews.fragments.HotFragment;
import com.dyx.bestnews.fragments.LoginFragment;
import com.dyx.bestnews.fragments.NewsFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.main_content)
    FrameLayout mainContent;
    @BindView(R.id.radiogroup1)
    RadioGroup radiogroup1;
    FavorFragment ff;
    HotFragment hf;
    LoginFragment lf;
    NewsFragment nf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        ff = new FavorFragment();
        hf = new HotFragment();
        lf = new LoginFragment();
        nf = new NewsFragment();
        addFragment(R.id.main_content, nf);
        setListeners();
    }

    private void setListeners() {

        radiogroup1.setOnCheckedChangeListener(this);
    }


    //单选组的选中监听
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.radioButton1:
                showFragment(nf);
                break;
            case R.id.radioButton2:
                showFragment(hf);
                break;
            case R.id.radioButton3:
                showFragment(ff);
                break;
            case R.id.radioButton4:
                showFragment(lf);
                break;
        }
    }

    //添加
    public void addFragment(int contenerId, Fragment f) {
        getSupportFragmentManager().beginTransaction().add(contenerId, f).commit();

    }

    public void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> list = fm.getFragments();
        if (list == null) return;
        Fragment tempFragment = null;
        for (Fragment f : list
                ) {
            if (f != fragment) {
                fm.beginTransaction().hide(f).commit();
            } else {
                tempFragment = f;
            }
        }
        if (tempFragment == null) {
            tempFragment = fragment;
            addFragment(R.id.main_content, fragment);
        }
        fm.beginTransaction().show(tempFragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
