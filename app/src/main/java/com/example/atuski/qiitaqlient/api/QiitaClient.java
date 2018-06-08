package com.example.atuski.qiitaqlient.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by atuski on 2018/04/17.
 */

public class QiitaClient {

    private static QiitaClient qiitaClient;

    public QiitaService qiitaService;

    private QiitaClient() {

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://qiita.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.qiitaService = retrofit.create(QiitaService.class);
    }

    public static QiitaClient getInstance(){
        if (qiitaClient == null) {
            qiitaClient = new QiitaClient();
        }
        return qiitaClient;
    }
}
