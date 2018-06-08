package com.example.atuski.qiitaqlient.api;

import com.example.atuski.qiitaqlient.model.LambdaResult;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterDeviceTokenService {

    @POST("/registerDeviceToken")
    Observable<LambdaResult> registerDeviceToken(@Body HashMap<String, String> postParameters);
}
