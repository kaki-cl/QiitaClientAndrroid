package com.example.atuski.qiitaqlient.api;

import com.example.atuski.qiitaqlient.model.SyncronizePostUserResult;

import java.util.HashMap;

import io.reactivex.Observable;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SyncronizePostUserService {

    @POST("/syncronizePostUser")
    Observable<SyncronizePostUserResult> requestSyncronizing(@Body HashMap<String, String> postParameters);
}
