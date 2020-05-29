package cn.edu.hebtu.software.listendemo.Mine.index.notify;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;

public class NotifyDetailActivity extends AppCompatActivity {
    private WebView webView;
    private String url;
    private WebSettings settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_detail);
        StatusBarUtil.setStatusBarColor(this, R.color.backgray);
        StatusBarUtil.statusBarLightMode(this);

        webView = findViewById(R.id.wv_notify_detail);
        settings = webView.getSettings();
        url = getIntent().getStringExtra("url");

        webView.loadUrl(url);
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("GBK");
        marginTopStateBar();
    }

    private void marginTopStateBar() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = StatusBarUtil.getStatusBarHeight(this);
        webView.setLayoutParams(layoutParams);
    }
}
