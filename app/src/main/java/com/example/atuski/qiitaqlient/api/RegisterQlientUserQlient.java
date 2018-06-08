package com.example.atuski.qiitaqlient.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterQlientUserQlient {

    private static RegisterQlientUserQlient mQlient;

    public RegisterQlientUserService mService;

    private RegisterQlientUserQlient() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mbr7mvznqh.execute-api.ap-northeast-1.amazonaws.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.mService = retrofit.create(RegisterQlientUserService.class);
    }

    public static RegisterQlientUserQlient getInstance() {
        if (mQlient == null) {
            mQlient = new RegisterQlientUserQlient();
        }
        return mQlient;
    }
}
