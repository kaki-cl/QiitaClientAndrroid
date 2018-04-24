package com.example.atuski.qiitaqlient.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import com.example.atuski.qiitaqlient.MainActivity;
import com.example.atuski.qiitaqlient.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qiita_article_web);

        WebView webView = (WebView) findViewById(R.id.web_view);

        Intent intent = getIntent();
        webView.loadUrl(intent.getStringExtra(MainActivity.EXTRA_URL));
    }

    @Override
    public void onBackPressed() {
        Log.v("DetailActivity", "onBackPressed");
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);
//        super.onBackPressed();
    }
}
