package com.example.atuski.qiitaqlient.repository.trend;

import android.content.Context;

import com.example.atuski.qiitaqlient.model.Article;
import com.example.atuski.qiitaqlient.api.QiitaClient;
import com.example.atuski.qiitaqlient.api.QiitaService;
import com.example.atuski.qiitaqlient.repository.userinfo.UserInfoRepository;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by atuski on 2018/04/17.
 */

public class TrendRepository {

    private static TrendRepository sInstance;

    private QiitaClient qiitaClient;

    private Context context;

    private TrendRepository(Context context) {

        qiitaClient = QiitaClient.getInstance();
        this.context = context;
    }

    public static TrendRepository getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TrendRepository(context);
        }
        return sInstance;
    }

    public Observable<List<Article>> searchArticle(String query) {

        return qiitaClient.qiitaService.getArticles(query, 20, 1, UserInfoRepository.getInstance(context).getSavedAuthHeaderValue())
                .map((articleSearchResult) -> {
                    return articleSearchResult;
                });
    }



}
