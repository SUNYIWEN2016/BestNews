package com.dyx.bestnews.acts;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dyx.bestnews.R;
import com.dyx.bestnews.entity.NewsBody;
import com.dyx.bestnews.utils.CommonUrls;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BrowserActivity extends AppCompatActivity {

    @BindView(R.id.webView1)
    WebView webView1;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private PopupWindow pop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        ButterKnife.bind(this);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        tvTitle.setText(getIntent().getStringExtra("title"));
        final String docid = getIntent().getStringExtra("docid");
        String url = CommonUrls.getCommonUrls().getFullUrl(docid);
        webView1.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                imgReset();
            }
        });
        x.http().get(new RequestParams(url), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    String str = new JSONObject(result).getString(docid);
                    NewsBody newsBody = new Gson().fromJson(str, NewsBody.class);
                    String before = "<img src=\"";
                    String after = "\"/> </img>";
                    for (NewsBody.Img img : newsBody.img) {
                        newsBody.body = newsBody.body.replace(img.ref, before + img.src + after);
                    }
                    webView1.loadDataWithBaseURL(null, newsBody.body, "text/html", "utf-8", null);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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


    @Override
    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView1.canGoBack()) {
            webView1.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.img_right)
    public void onClick() {
        View view = View.inflate(this, R.layout.layout_popup, null);
        if (pop == null) {
            pop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            view.findViewById(R.id.btn_comment).setOnClickListener(popListener);
            view.findViewById(R.id.btn_favor).setOnClickListener(popListener);
            view.findViewById(R.id.btn_share).setOnClickListener(popListener);
            pop.setTouchable(true);
            pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        pop.showAsDropDown(findViewById(R.id.img_right));

    }

    private View.OnClickListener popListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_comment:
                    Toast.makeText(BrowserActivity.this, "评论", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_favor:
                    Toast.makeText(BrowserActivity.this, "收藏", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_share:
                    Toast.makeText(BrowserActivity.this, "分享", Toast.LENGTH_SHORT).show();
                    break;
            }
            pop.dismiss();
        }
    };

    private void imgReset() {
        webView1.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%';   " +
                "}" +
                "})()");
    }
}
