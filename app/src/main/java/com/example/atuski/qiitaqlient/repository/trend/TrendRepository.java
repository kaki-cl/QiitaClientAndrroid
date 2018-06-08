package com.example.atuski.qiitaqlient.repository.trend;

import com.example.atuski.qiitaqlient.model.Article;
import com.example.atuski.qiitaqlient.api.QiitaClient;
import com.example.atuski.qiitaqlient.api.QiitaService;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by atuski on 2018/04/17.
 */

public class TrendRepository {

    private static TrendRepository sInstance;

    private QiitaClient qiitaClient;

    private TrendRepository() {

        qiitaClient = QiitaClient.getInstance();
    }

    public static TrendRepository getInstance() {
        if (sInstance == null) {
            sInstance = new TrendRepository();
        }
        return sInstance;
    }

    public Observable<List<Article>> searchArticle(String query) {

        return qiitaClient.qiitaService.getArticles(query)
                .map((articleSearchResult) -> {

//                    for (Article r : articleSearchResult) {
//                        r.setQueryId(queryId);
//                    }

                    // 検索結果を保存
//                    localDataSource.insertArticles(articleSearchResult);
                    return articleSearchResult;
                });
    }



}
