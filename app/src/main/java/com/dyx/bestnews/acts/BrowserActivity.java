package com.dyx.bestnews.acts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.dyx.bestnews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrowserActivity extends AppCompatActivity {

    @BindView(R.id.webView1)
    WebView webView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        ButterKnife.bind(this);
        final String url = getIntent().getStringExtra("url");
        if (getIntent().getBooleanExtra("user",true)){
            webView1.loadUrl(url);
            webView1.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }
            });
        }else{
            Toast.makeText(BrowserActivity.this, "需要手动解析", Toast.LENGTH_SHORT).show();
//            x.http().get(new RequestParams(url), new Callback.CommonCallback<String>() {
//                @Override
//                public void onSuccess(String result) {
//       //             String postid=url.substring();
//                //    webView1.loadDataWithBaseURL(null,null,"text/html","utf-8",null);
//                }
//
//                @Override
//                public void onError(Throwable ex, boolean isOnCallback) {
//
//                }
//
//                @Override
//                public void onCancelled(CancelledException cex) {
//
//                }
//
//                @Override
//                public void onFinished() {
//
//                }
//            });



        }

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
}
