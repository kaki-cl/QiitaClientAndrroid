package com.example.atuski.qiitaqlient.api;

import com.example.atuski.qiitaqlient.model.LambdaResult;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterQlientUserService {

    @POST("/registerQlientUser")
    Observable<LambdaResult> registerQlientUser(@Body HashMap<String, String> postParameters);
}
