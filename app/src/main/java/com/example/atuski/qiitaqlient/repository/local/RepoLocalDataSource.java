package com.example.atuski.qiitaqlient.repository.local;

import android.content.Context;

import com.example.atuski.qiitaqlient.model.OrmaDatabase;
import com.example.atuski.qiitaqlient.model.Query;
import com.example.atuski.qiitaqlient.model.Repo;
import com.github.gfx.android.orma.AccessThreadConstraint;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by atuski on 2018/03/28.
 */
public class RepoLocalDataSource {

    private static RepoLocalDataSource sInstance;

    private OrmaDatabase ormaDatabase;

    private RepoLocalDataSource(Context context) {

        ormaDatabase = OrmaDatabase.builder(context)
                .name("test.db")
                .writeOnMainThread(AccessThreadConstraint.NONE)
                .build();
    }

    public static RepoLocalDataSource getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new RepoLocalDataSource(context);
        }
        return sInstance;
    }

    public long insertQuery(String query) {

        Query queryModel = new Query();
        queryModel.setQuery(query);
        return ormaDatabase.relationOfQuery().queryEq(query).inserter().execute(queryModel);
    }

    public void insertRepos(List<Repo> repoSearchResult) {
        ormaDatabase.prepareInsertIntoRepo().executeAll(repoSearchResult);
    }

    public boolean isEmptyQuery(String query) {
        return ormaDatabase.relationOfQuery().queryEq(query).isEmpty();
    }

    public Observable<List<Repo>> loadRepos(String query) {

        return BehaviorSubject.createDefault(
                ormaDatabase
                        .relationOfRepo()
                        .queryIdEq(findQueryId(query))
                        .selector()
                        .toList()
        );
    }

    private long findQueryId(String query) {
        return ormaDatabase.relationOfQuery().queryEq(query).get(0).getId();
    }
}
