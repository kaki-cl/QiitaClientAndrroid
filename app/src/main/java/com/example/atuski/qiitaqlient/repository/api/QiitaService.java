package com.example.atuski.qiitaqlient.repository.api;

import com.example.atuski.qiitaqlient.model.Article;
import com.example.atuski.qiitaqlient.model.Stock;
import com.example.atuski.qiitaqlient.model.Token;
import com.example.atuski.qiitaqlient.model.UserInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface QiitaService {

    @GET("/api/v2/items")
    Observable<List<Article>> getArticles(@Query("query") String query);


    @POST("/api/v2/access_tokens")
    Observable<Token> getAccessToken(
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret,
            @Query("code") String code);

    @GET("/api/v2/authenticated_user")
    Observable<UserInfo> getUserInfo(@Header("Authorization") String accessToken);

    //https://qiita.com/api/v2/users/kaki4062/stocks?page=1&per_page=20
    @GET("/api/v2/users/{user}/stocks")
    Observable<List<Stock>> getStockItems(
            @Path("user") String userId,
            @Query("page") int page,
            @Query("per_page") int perPage);
}
