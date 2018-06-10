package com.example.atuski.qiitaqlient.repository.search;

import android.content.Context;
import android.util.Log;

import com.example.atuski.qiitaqlient.model.Query;
import com.example.atuski.qiitaqlient.model.Token;
import com.example.atuski.qiitaqlient.model.UserInfo;
import com.example.atuski.qiitaqlient.api.QiitaClient;
import com.example.atuski.qiitaqlient.api.QiitaService;

import com.example.atuski.qiitaqlient.model.Article;

import java.util.List;

import io.reactivex.Observable;

public class SearchRepository {

    //To avoid that Non Static field cannot be referenced from a static context
    private static SearchRepository sInstance;

    private QiitaClient qiitaClient;

    private LocalDataSource localDataSource;

    private SearchRepository(Context context) {

        qiitaClient = QiitaClient.getInstance();
        localDataSource = LocalDataSource.getInstance(context);
    }

    public static SearchRepository getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new SearchRepository(context);
        }
        return sInstance;
    }

    public Observable<List<Article>> searchArticle(String query, Integer page) {

        Integer perPage = 20;
        return qiitaClient.qiitaService.getArticles(query, perPage, page);



//        if (localDataSource.isEmptyQuery(query) && localDataSource.isOldQuery(query)) {
//            Log.v("Repository", "Need to search via api for updating Articles");
//
//            long queryId = localDataSource.upsertQuery(query);
//            return qiitaClient.qiitaService.getArticles(query, perPage, page)
//                    .map((articleSearchResult) -> {
//                        for (Article r : articleSearchResult) {
//                            r.setQueryId(queryId);
//                        }
//
//                        // 検索結果を保存
//                        localDataSource.insertArticles(articleSearchResult);
//                        return articleSearchResult;
//                    });
//        }
//
//        Log.v("Repository", "Fetch From Local DataSource");
//        localDataSource.upsertQuery(query);
//        return localDataSource.loadArticles(query);
    }

//    public List<String> loadLatestSearchQuery() {
//
//        return localDataSource.loadLatestSearchQuery();
//    }


//    public List<Query> loadLatestSearchQuery() {
//
//        return localDataSource.loadLatestSearchQuery();
//    }

    public Observable<List<Query>> loadLatestSearchQuery() {

        return localDataSource.loadLatestSearchQuery();
    }


    public Observable<Token> fetchAccessToken(String code) {

        String clientId = "dfd44c0b8c380894cac1ea43ff4b815a2661e461";
        String clientSecret = "093660d3c232d54d33c09b7c2d9465ad8bb60202";
        return qiitaClient.qiitaService.getAccessToken(clientId, clientSecret, code);
    }

    public Observable<UserInfo> fetchUserInfo(String accessToken) {

        String baseValue = "Bearer ";
        String value = baseValue + accessToken;

        return qiitaClient.qiitaService.getUserInfo(value);
    }


}
