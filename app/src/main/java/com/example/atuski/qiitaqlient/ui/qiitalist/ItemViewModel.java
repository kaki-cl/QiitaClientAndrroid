package com.example.atuski.qiitaqlient.ui.qiitalist;

import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import com.example.atuski.qiitaqlient.model.Repo;

import java.util.Collections;
import java.util.List;

import io.reactivex.subjects.BehaviorSubject;


public class ItemViewModel {

    public ObservableField<Repo> repo;

    final BehaviorSubject<Integer> clickTimes = BehaviorSubject.createDefault(0);

    public ItemViewModel(ObservableField<Repo> repo) {

        this.repo = repo;
        repo.get().getTitle();
    }

    public ObservableField<Repo> getRepo() {
        Log.v("ItemViewModel", "getRepo " + repo.get().getTitle());
        return repo;
    }

    // todo 双方向バインドするときに使う？
    // 今は viewModel -> View のOneWayでしか使ってない。
    public void setRepo(ObservableField<Repo> repo) {
        Log.v("ItemViewModel", "setRepo");
        this.repo = repo;
    }

    public void onClick(View view) {
        Log.v("ItemViewModel : onClick", "ttttttttt");
        Log.v("ItemViewModel : onClick", repo.get().getUrl());
        clickTimes.onNext(clickTimes.getValue() + 1);
    }
}

