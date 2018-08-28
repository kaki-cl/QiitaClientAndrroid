package com.example.atuski.qiitaqlient.repository.followee;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.api.QiitaClient;
import com.example.atuski.qiitaqlient.model.Followee;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FollowRepository {

    private static FollowRepository sInstance;

    private QiitaClient qiitaClient;

    private Context context;

    private FollowRepository(Context context) {

        this.context = context;
        qiitaClient = QiitaClient.getInstance();
    }

    public static FollowRepository getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new FollowRepository(context);
        }
        return sInstance;
    }

    public Observable<List<Followee>> searchFollowees(String userId) {

        return qiitaClient.qiitaService.getFollowees(userId).subscribeOn(Schedulers.io());
    }

    public Completable followPostUser(String postUserId) {

        SharedPreferences data = context.getSharedPreferences(context.getResources().getString(R.string.USER_INFO), Context.MODE_PRIVATE);
        String authHeaderValue = data.getString(context.getResources().getString(R.string.AUTHORIZATION_HEADER_VALUE), null);

        return qiitaClient.qiitaService
                .followPostUser(postUserId, authHeaderValue)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
