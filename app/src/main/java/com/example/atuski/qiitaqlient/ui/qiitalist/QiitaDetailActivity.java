package com.example.atuski.qiitaqlient.ui.qiitalist;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import com.example.atuski.qiitaqlient.R;

/**
 * Created by atuski on 2018/03/30.
 */

public class QiitaDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qiita_article_web);

        WebView webView = (WebView) findViewById(R.id.web_view);

        Intent intent = getIntent();
        webView.loadUrl(intent.getStringExtra(QiitaListActivity.EXTRA_URL));

        Log.v("QiitaDetailActivity", "test");
        Log.v("QiitaDetailActivity", intent.getStringExtra(QiitaListActivity.EXTRA_URL));
    }
}
