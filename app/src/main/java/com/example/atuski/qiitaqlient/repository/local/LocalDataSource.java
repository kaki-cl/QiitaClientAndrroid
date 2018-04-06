package com.example.atuski.qiitaqlient.repository.local;

import android.content.Context;

import com.example.atuski.qiitaqlient.model.OrmaDatabase;
import com.example.atuski.qiitaqlient.model.Query;
import com.example.atuski.qiitaqlient.model.Article;
import com.github.gfx.android.orma.AccessThreadConstraint;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class LocalDataSource {

    private static LocalDataSource sInstance;

    private OrmaDatabase ormaDatabase;

    private LocalDataSource(Context context) {

        ormaDatabase = OrmaDatabase.builder(context)
                .name("test.db")
                .writeOnMainThread(AccessThreadConstraint.NONE)
                .build();
    }

    public static LocalDataSource getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new LocalDataSource(context);
        }
        return sInstance;
    }

    public long insertQuery(String query) {

        Query queryModel = new Query();
        queryModel.setQuery(query);
        return ormaDatabase.relationOfQuery().queryEq(query).inserter().execute(queryModel);
    }

    public void insertArticles(List<Article> articleSearchResult) {
        ormaDatabase.prepareInsertIntoArticle().executeAll(articleSearchResult);
    }

    public boolean isEmptyQuery(String query) {
        return ormaDatabase.relationOfQuery().queryEq(query).isEmpty();
    }

    public Observable<List<Article>> loadArticles(String query) {

        return BehaviorSubject.createDefault(
                ormaDatabase
                        .relationOfArticle()
                        .queryIdEq(findQueryId(query))
                        .selector()
                        .toList()
        );
    }

    private long findQueryId(String query) {
        return ormaDatabase.relationOfQuery().queryEq(query).get(0).getId();
    }
}
