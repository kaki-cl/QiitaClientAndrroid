package com.example.atuski.qiitaqlient.api;

import com.example.atuski.qiitaqlient.model.LambdaResult;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterDeviceTokenQlient {

    private static RegisterDeviceTokenQlient mQlient;

    public RegisterDeviceTokenService mService;

    private RegisterDeviceTokenQlient() {

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://lwsb02oc01.execute-api.ap-northeast-1.amazonaws.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.mService = retrofit.create(RegisterDeviceTokenService.class);
    }

    public static RegisterDeviceTokenQlient getInstance() {
        if (mQlient == null) {
            mQlient = new RegisterDeviceTokenQlient();
        }
        return mQlient;
    }

    public Observable<LambdaResult> registerDeviceToken(HashMap<String, String> postParameters) {
        return mService.registerDeviceToken(postParameters);
    }
}
