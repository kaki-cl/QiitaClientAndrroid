package com.example.atuski.qiitaqlient.ui.qiitadetail;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.ui.qiitalist.QiitaListActivity;

public class QiitaDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qiita_article_web);

        WebView webView = (WebView) findViewById(R.id.web_view);

        Intent intent = getIntent();
        webView.loadUrl(intent.getStringExtra(QiitaListActivity.EXTRA_URL));
    }
}
