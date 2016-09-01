package com.dyx.bestnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dyx.bestnews.entity.NetEaseType;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.bg_logo)
    RelativeLayout bgLogo;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        intent = new Intent(MainActivity.this, NewsActivity.class);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        bgLogo.startAnimation(anim);
        ivLogo.startAnimation(anim);
        //1.logo显示
        //2.后台延时
        //3.初始化数据库
        //4.获取新闻分类数据()
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 1000);
//
        getList();
    }

    private void getList() {
        final long time = System.currentTimeMillis();
        String url = "http://c.m.163.com/nc/topicset/android/subscribe/manage/listspecial.html";
        RequestParams entity = new RequestParams(url);
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                NetEaseType netEaseType = gson.fromJson(result, NetEaseType.class);
                //集合的元素必须可序列化
                //用ArrayList<>保存，不能用List
                ivLogo.setVisibility(View.VISIBLE);
                intent.putExtra("list", netEaseType.gettList());


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                long endTime = System.currentTimeMillis();
                if ((endTime - time) < 1500) {
                    try {
                        Thread.sleep(2000 - (endTime - time));
                    } catch (Exception e) {
                    }
                }
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
            }
        });

    }
}
