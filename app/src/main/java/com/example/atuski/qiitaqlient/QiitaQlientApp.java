package com.example.atuski.qiitaqlient;

import android.app.Application;

import com.example.atuski.qiitaqlient.repository.search.SearchRepository;
import com.example.atuski.qiitaqlient.repository.trend.TrendRepository;

public class QiitaQlientApp extends Application {

    private static QiitaQlientApp app;

    public static QiitaQlientApp getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.app = this;
    }

    public SearchRepository getSearchRepository() {
        return SearchRepository.getInstance(getApplicationContext());
    }

    public TrendRepository getTrendRepository() {
        return TrendRepository.getInstance();
    }
}
