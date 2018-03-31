package com.example.atuski.qiitaqlient.repository;

import android.content.Context;
import android.util.Log;

import com.example.atuski.qiitaqlient.api.QiitaService;
import com.example.atuski.qiitaqlient.model.Repo;
import com.example.atuski.qiitaqlient.repository.local.RepoLocalDataSource;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by atuski on 2018/03/27.
 */
public class QiitaListRepository {

    //To avoid that Non Static field cannot be referenced from a static context
    private static QiitaListRepository sInstance;

    private Retrofit retrofit;

    private QiitaService qiitaService;

    private RepoLocalDataSource repoLocalDataSource;

    private QiitaListRepository(Context context) {

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://qiita.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.qiitaService = retrofit.create(QiitaService.class);

        repoLocalDataSource = RepoLocalDataSource.getInstance(context);
    }

    public static QiitaListRepository getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new QiitaListRepository(context);
        }
        return sInstance;
    }

    public Observable<List<Repo>> searchRepo(String query) {

        if (repoLocalDataSource.isEmptyQuery(query)) {
            Log.v("Repository", "新しい検索クエリだった。");

            long queryId = repoLocalDataSource.insertQuery(query);

            return this.qiitaService.listRepos(query)
                    .map((repoSearchResult) -> {
                        for (Repo r : repoSearchResult) {
                            Log.v("Repository", "r.getUser().getProfile_image_url()");
                            Log.v("Repository", r.user.getProfile_image_url());

                            r.setQueryId(queryId);
                        }

                        // 検索結果を保存
                        repoLocalDataSource.insertRepos(repoSearchResult);
                        return repoSearchResult;
                    });
        }

        Log.v("Repository", "すでに検索されたクエリだった。");
        return repoLocalDataSource.loadRepos(query);
    }
}
