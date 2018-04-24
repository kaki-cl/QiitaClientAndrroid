package com.example.atuski.qiitaqlient.ui.searchhistory;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;


import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.model.Query;
import com.example.atuski.qiitaqlient.repository.search.SearchRepository;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by atuski on 2018/04/23.
 */

public class SearchHistoryMainViewModel {

    protected ObservableList<SearchHistoryItemViewModel> searchHistoryItemViewModels;

    final BehaviorSubject<List<SearchHistoryItemViewModel>> queryResults = BehaviorSubject.createDefault(Collections.emptyList());

//    public final ObservableArrayList<String> searchHistory = new ObservableArrayList<>();
//    public final ObservableArrayList<Query> searchHistory = new ObservableArrayList<>();

    private SearchRepository repository;

    public SearchHistoryMainViewModel() {
        this.searchHistoryItemViewModels = new ObservableArrayList<>();
        this.repository = QiitaQlientApp.getInstance().getSearchRepository();
        loadLatestSearchQuery();
    }

    private void loadLatestSearchQuery() {
//        searchHistory.addAll(repository.loadLatestSearchQuery());

//        searchHistory.addAll(repository.loadLatestSearchQuery());

        repository.loadLatestSearchQuery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Query>>() {

                    @Override
                    public void onNext(List<Query> queries) {
                        queryResults.onNext(queries
                                .stream()
                                .map(query -> new SearchHistoryItemViewModel(new ObservableField<>(query)))
                                .collect(Collectors.toList())
                        );
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onComplete() {}
                });
    }

}
