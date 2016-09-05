package com.dyx.bestnews.acts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.dyx.bestnews.R;
import com.dyx.bestnews.base.BaseFragment;
import com.dyx.bestnews.fragments.FavorFragment;
import com.dyx.bestnews.fragments.HotFragment;
import com.dyx.bestnews.fragments.LoginFragment;
import com.dyx.bestnews.fragments.NewsFragment;

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
        nf = NewsFragment.getInstance(getIntent().getExtras());
        addFragment(nf);
        setListeners();
    }

    private void addFragment(Fragment f) {
        getSupportFragmentManager().beginTransaction().add(R.id.main_content, f, f.getClass().getSimpleName()).commit();
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
                //   getSupportFragmentManager().beginTransaction().show(nf).hide(hf).hide(ff).hide(lf).commit();
                break;
            case R.id.radioButton2:
                showFragment(hf);
                //   getSupportFragmentManager().beginTransaction().show(hf).hide(ff).hide(nf).hide(lf).commit();
                break;
            case R.id.radioButton3:
                showFragment(ff);
                //   getSupportFragmentManager().beginTransaction().show(ff).hide(nf).hide(lf).hide(hf).commit();
                break;
            case R.id.radioButton4:
                showFragment(lf);
                //  getSupportFragmentManager().beginTransaction().show(lf).hide(nf).hide(ff).hide(hf).commit();
                break;
        }
    }

    public void showFragment(Fragment f) {
        Fragment[] fs = {nf, ff, lf, hf};
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        for (Fragment tf : fs) {
            tr.hide(tf);
        }
        tr.show(f).commit();
    }


    @Override
    public void onFragmentInteraction(int viewId, Bundle bundle) {

    }
}
