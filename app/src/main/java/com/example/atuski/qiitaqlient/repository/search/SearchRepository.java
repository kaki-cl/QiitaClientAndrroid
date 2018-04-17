package com.example.atuski.qiitaqlient.repository.search;

import android.content.Context;
import android.util.Log;

import com.example.atuski.qiitaqlient.repository.api.QiitaClient;
import com.example.atuski.qiitaqlient.repository.api.QiitaService;

import com.example.atuski.qiitaqlient.model.Article;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchRepository {

    //To avoid that Non Static field cannot be referenced from a static context
    private static SearchRepository sInstance;

    private QiitaClient qiitaClient;

//    private Retrofit retrofit;
//
//    private QiitaService qiitaService;

    private LocalDataSource localDataSource;

    private SearchRepository(Context context) {

//        Gson gson = new GsonBuilder()
//                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//                .excludeFieldsWithoutExposeAnnotation()
//                .create();
//
//        this.retrofit = new Retrofit.Builder()
//                .baseUrl("https://qiita.com")
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build();

        qiitaClient = QiitaClient.getInstance();

//        this.qiitaService = retrofit.create(QiitaService.class);

        localDataSource = LocalDataSource.getInstance(context);
    }

    public static SearchRepository getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new SearchRepository(context);
        }
        return sInstance;
    }

    public Observable<List<Article>> searchArticle(String query) {

        if (localDataSource.isEmptyQuery(query) && localDataSource.isOldQuery(query)) {
            Log.v("Repository", "Need to search via api for updating Articles");

            long queryId = localDataSource.upsertQuery(query);
            return qiitaClient.qiitaService.getArticles(query)
                    .map((articleSearchResult) -> {
                        for (Article r : articleSearchResult) {
                            r.setQueryId(queryId);
                        }

                        // 検索結果を保存
                        localDataSource.insertArticles(articleSearchResult);
                        return articleSearchResult;
                    });
        }

        Log.v("Repository", "Fetch From Local DataSource");
        localDataSource.upsertQuery(query);
        return localDataSource.loadArticles(query);
    }

    public List<String> loadLatestSearchQuery() {

        return localDataSource.loadLatestSearchQuery();
    }
}
