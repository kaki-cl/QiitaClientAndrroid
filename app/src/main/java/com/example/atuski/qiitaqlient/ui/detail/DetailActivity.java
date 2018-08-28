package com.example.atuski.qiitaqlient.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.ui.util.helper.ResourceResolver;

public class DetailActivity extends AppCompatActivity {

    private Intent receivedIntent;
    private ResourceResolver resourceResolver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resourceResolver = QiitaQlientApp.getInstance().getResourceResolver();

        setContentView(R.layout.qiita_article_web);
        WebView webView = (WebView) findViewById(R.id.web_view);

        receivedIntent = getIntent();
        webView.loadUrl(receivedIntent.getStringExtra(resourceResolver.getString(R.string.WEB_VIEW_URL)));
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.putExtra(resourceResolver.getString(R.string.LAST_QUERY),
//                receivedIntent.getStringExtra(resourceResolver.getString(R.string.LAST_QUERY)));
//        startActivity(intent);
        super.onBackPressed();
    }
}
