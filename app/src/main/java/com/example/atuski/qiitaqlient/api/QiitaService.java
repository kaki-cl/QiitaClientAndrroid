package com.example.atuski.qiitaqlient.api;

import com.example.atuski.qiitaqlient.model.Article;
import com.example.atuski.qiitaqlient.model.Followee;
import com.example.atuski.qiitaqlient.model.Stock;
import com.example.atuski.qiitaqlient.model.Token;
import com.example.atuski.qiitaqlient.model.UserInfo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface QiitaService {

    @GET("/api/v2/items")
    Observable<List<Article>> getArticles(
            @Query("query") String query,
            @Query("per_page") Integer perPage,
            @Query("page") Integer page,
            @Header("Authorization") String authHeaderValue);


    @POST("/api/v2/access_tokens")
    Observable<Token> getAccessToken(
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret,
            @Query("code") String code);

    @GET("/api/v2/authenticated_user")
    Observable<UserInfo> getUserInfo(@Header("Authorization") String authHeaderValue);

    //https://qiita.com/api/v2/users/kaki4062/stocks?page=1&per_page=20
    @GET("/api/v2/users/{user}/stocks")
    Observable<List<Stock>> getStockItems(
            @Path("user") String userId,
            @Query("page") int page,
            @Query("per_page") int perPage,
            @Header("Authorization") String authHeaderValue);


    @GET("/api/v2/users/{user}/followees")
    Observable<List<Followee>> getFollowees(@Path("user") String userId);

    @PUT("/api/v2/items/{itemId}/stock")
    Completable stockArticle(@Path("itemId") String articleId, @Header("Authorization") String accessToken);

    @PUT("/api/v2/users/{postUserId}/following")
    Completable followPostUser(@Path("postUserId") String postUserId, @Header("Authorization") String accessToken);
}
