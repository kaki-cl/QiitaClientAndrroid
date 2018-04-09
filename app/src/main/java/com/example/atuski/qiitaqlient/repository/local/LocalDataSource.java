package com.example.atuski.qiitaqlient.repository.local;

import android.content.Context;
import android.util.Log;

import com.example.atuski.qiitaqlient.model.OrmaDatabase;
import com.example.atuski.qiitaqlient.model.Query;
import com.example.atuski.qiitaqlient.model.Article;
import com.example.atuski.qiitaqlient.model.Query_Relation;
import com.github.gfx.android.orma.AccessThreadConstraint;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class LocalDataSource {

    private static LocalDataSource sInstance;

    private OrmaDatabase ormaDatabase;

    private LocalDataSource(Context context) {

        ormaDatabase = OrmaDatabase.builder(context)
                .name("qiita.db")
                .writeOnMainThread(AccessThreadConstraint.NONE)
                .build();
    }

    public static LocalDataSource getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new LocalDataSource(context);
        }
        return sInstance;
    }

    public long upsertQuery(String query) {

        // 存在してる場合はupdatedAtだけ更新
        if (!isEmptyQuery(query)) {
            return ormaDatabase.updateQuery().queryEq(query).updatedAt(new Date()).execute();
        }
        Query queryModel = new Query();
        queryModel.setQuery(query);
        queryModel.setUpdatedAt(new Date());

        return ormaDatabase.relationOfQuery().queryEq(query).inserter().execute(queryModel);
    }

    private long findQueryId(String query) {
        return ormaDatabase.relationOfQuery().queryEq(query).get(0).getId();
    }

    public boolean isEmptyQuery(String query) {
        return ormaDatabase.relationOfQuery().queryEq(query).isEmpty();
    }

    /**
     * 現在時刻から1時間以上前に検索されたキーワードならtrueを返す。
     *
     * @param query
     * @return
     */
    public boolean isOldQuery(String query) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, -1);
        return ormaDatabase.relationOfQuery().queryEq(query).updatedAtLe(calendar.getTime()).isEmpty();
    }

    public void insertArticles(List<Article> articleSearchResult) {
        ormaDatabase.prepareInsertIntoArticle().executeAll(articleSearchResult);
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

    public List<String> loadLatestSearchQuery() {

        return ormaDatabase
                .selectFromQuery()
                .orderByUpdatedAtDesc()
                .limit(20)
                .toList()
                .stream()
                .map(query -> query.getQuery())
                .collect(Collectors.toList());
    }
}
