package com.dyx.bestnews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.main_content)
    FrameLayout mainContent;
    @BindView(R.id.radiogroup1)
    RadioGroup radiogroup1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
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
                break;
            case R.id.radioButton2:
                break;
            case R.id.radioButton3:
                break;
            case R.id.radioButton4:
                break;
        }
    }
}
