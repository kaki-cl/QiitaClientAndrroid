package com.example.atuski.qiitaqlient;

import android.app.Application;

import com.example.atuski.qiitaqlient.repository.QiitaListRepository;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by atuski on 2018/03/27.
 */

public class QiitaQlientApp extends Application {

    private static QiitaQlientApp app;

    public static QiitaQlientApp getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public QiitaListRepository getRepository() {
        return QiitaListRepository.getInstance();
    }
}
