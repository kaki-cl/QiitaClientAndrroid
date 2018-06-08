package com.example.atuski.qiitaqlient.repository.followee;


import com.example.atuski.qiitaqlient.api.QiitaClient;
import com.example.atuski.qiitaqlient.model.Followee;

import java.util.List;

import io.reactivex.Observable;

public class FolloweeRepository {

    private static FolloweeRepository sInstance;

    private QiitaClient qiitaClient;

    private FolloweeRepository() {
        qiitaClient = QiitaClient.getInstance();
    }

    public static FolloweeRepository getInstance() {
        if (sInstance == null) {
            sInstance = new FolloweeRepository();
        }
        return sInstance;
    }

    public Observable<List<Followee>> searchFollowees(String userId) {

        return qiitaClient.qiitaService.getFollowees(userId);
    }
}
