package com.example.atuski.qiitaqlient.api;

import android.util.Log;

import com.example.atuski.qiitaqlient.model.LambdaResult;
import com.example.atuski.qiitaqlient.model.SyncronizePostUserResult;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncronizePostUserQlient {

    private static SyncronizePostUserQlient mQlient;

    public SyncronizePostUserService mService;

    private SyncronizePostUserQlient() {

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://en1044oaqd.execute-api.ap-northeast-1.amazonaws.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.mService = retrofit.create(SyncronizePostUserService.class);
    }

    public static SyncronizePostUserQlient getInstance() {

        if (mQlient == null) {
            mQlient = new SyncronizePostUserQlient();
        }
        return mQlient;
    }

    public void requestSynchronization(String qlientUserId, String postUserId) {

        Log.v("requestSyncronize", "ここまできてる？");
        if (qlientUserId == null || postUserId == null) {
            return;
        }
        Log.v("requestSyncronize", qlientUserId);
        Log.v("requestSyncronize", postUserId);

        HashMap<String, String> postParameters = new HashMap<>();
        postParameters.put("qlientUserId", qlientUserId);
        postParameters.put("postUserId", postUserId);

        mService.requestSyncronizing(postParameters)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lambdaResult -> {
                    Log.v("requestSyncronize", "finish");
                }, error -> {
                    Log.v("requestSyncronizing", "ERROR");
                    error.printStackTrace();
                });
    }
}
