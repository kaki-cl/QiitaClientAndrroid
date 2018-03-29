package com.example.atuski.qiitaqlient.api;

import com.example.atuski.qiitaqlient.data.Repo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by atuski on 2018/03/24.
 */

public interface QiitaService{
    @GET("/api/v2/items")
    Observable<List<Repo>> listRepos(@Query("query") String query);
}
