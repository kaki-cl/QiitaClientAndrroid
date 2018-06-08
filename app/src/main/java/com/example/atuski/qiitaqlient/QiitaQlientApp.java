package com.example.atuski.qiitaqlient;

import android.app.Application;

import com.example.atuski.qiitaqlient.repository.search.SearchRepository;
import com.example.atuski.qiitaqlient.repository.stock.StockRepository;
import com.example.atuski.qiitaqlient.repository.trend.TrendRepository;
import com.example.atuski.qiitaqlient.ui.util.helper.ResourceResolver;
import com.google.firebase.FirebaseApp;

public class QiitaQlientApp extends Application {

    private static QiitaQlientApp app;

    public static QiitaQlientApp getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.app = this;
        FirebaseApp.initializeApp(this);
    }

    public SearchRepository getSearchRepository() {
        return SearchRepository.getInstance(getApplicationContext());
    }

    public TrendRepository getTrendRepository() {
        return TrendRepository.getInstance();
    }

    public StockRepository getStockRepository() {
        return StockRepository.getInstance();
    }

    public ResourceResolver getResourceResolver() {
        return ResourceResolver.getInstance(getApplicationContext());
    }
}
