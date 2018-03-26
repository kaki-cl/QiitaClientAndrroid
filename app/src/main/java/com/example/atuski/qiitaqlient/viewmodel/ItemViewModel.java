package com.example.atuski.qiitaqlient.viewmodel;

import android.databinding.ObservableField;
import android.util.Log;

import com.example.atuski.qiitaqlient.data.Repo;

/**
 * Created by atuski on 2018/03/26.
 */

public class ItemViewModel {

    public ObservableField<String> title;
    public ObservableField<String> url;

    public ObservableField<Repo> repo;

    public ItemViewModel(ObservableField<Repo> repo) {

        this.repo = repo;

        repo.get().getTitle();
    }

    public ObservableField<Repo> getRepo() {
        Log.v("ItemViewModel", repo.get().getTitle());
        return repo;
    }

    public void setRepo(ObservableField<Repo> repo) {


        this.repo = repo;
    }


}

