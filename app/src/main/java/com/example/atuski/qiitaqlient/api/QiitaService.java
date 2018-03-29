package com.example.atuski.qiitaqlient.api;

import com.example.atuski.qiitaqlient.model.Repo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QiitaService{
    @GET("/api/v2/items")
    Observable<List<Repo>> listRepos(@Query("query") String query);
}
