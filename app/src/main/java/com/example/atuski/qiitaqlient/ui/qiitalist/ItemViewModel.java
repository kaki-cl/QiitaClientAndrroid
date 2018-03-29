package com.example.atuski.qiitaqlient.ui.qiitalist;

import android.databinding.ObservableField;
import android.util.Log;

import com.example.atuski.qiitaqlient.model.Repo;



public class ItemViewModel {

    public ObservableField<Repo> repo;

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
}

