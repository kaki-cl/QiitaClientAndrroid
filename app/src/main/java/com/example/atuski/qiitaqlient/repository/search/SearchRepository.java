package com.example.atuski.qiitaqlient.repository.search;

import android.content.Context;

import com.example.atuski.qiitaqlient.model.Query;
import com.example.atuski.qiitaqlient.model.Token;
import com.example.atuski.qiitaqlient.model.UserInfo;
import com.example.atuski.qiitaqlient.api.QiitaClient;

import com.example.atuski.qiitaqlient.model.Article;
import com.example.atuski.qiitaqlient.repository.userinfo.UserInfoRepository;

import java.util.List;

import io.reactivex.Observable;

public class SearchRepository {

    //To avoid that Non Static field cannot be referenced from a static context
    private static SearchRepository sInstance;

    private QiitaClient qiitaClient;

    private Context context;

    private LocalDataSource localDataSource;

    private SearchRepository(Context context) {

        qiitaClient = QiitaClient.getInstance();
        localDataSource = LocalDataSource.getInstance(context);
        this.context = context;
    }

    public static SearchRepository getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new SearchRepository(context);
        }
        return sInstance;
    }

    public Observable<List<Article>> searchArticle(String query, Integer page) {

        Integer perPage = 20;
        return qiitaClient.qiitaService.getArticles(query, perPage, page, UserInfoRepository.getInstance(context).getSavedAuthHeaderValue());
    }

    public Observable<List<Query>> loadLatestSearchQuery() {

        return localDataSource.loadLatestSearchQuery();
    }
}
