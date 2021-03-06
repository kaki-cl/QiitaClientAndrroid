package com.example.atuski.qiitaqlient.repository.api;

import com.example.atuski.qiitaqlient.model.Article;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QiitaService{

    @GET("/api/v2/items")
    Observable<List<Article>> getArticles(@Query("query") String query);
}
