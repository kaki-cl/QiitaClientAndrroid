package com.example.atuski.qiitaqlient;

import android.app.Application;
import android.content.Context;

import com.example.atuski.qiitaqlient.repository.search.SearchRepository;
import com.example.atuski.qiitaqlient.repository.stock.StockRepository;
import com.example.atuski.qiitaqlient.repository.trend.TrendRepository;
import com.example.atuski.qiitaqlient.ui.util.helper.ResourceResolver;

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

    public StockRepository getStockRepository() {
        return StockRepository.getInstance();
    }

    public ResourceResolver getResourceResolver() {
        return ResourceResolver.getInstance(getApplicationContext());
    }
}
