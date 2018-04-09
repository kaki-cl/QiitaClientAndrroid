package com.example.atuski.qiitaqlient.ui.qiitalist;

import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import com.example.atuski.qiitaqlient.model.Article;

import io.reactivex.subjects.BehaviorSubject;


public class ItemViewModel {

    public ObservableField<Article> article;

    final BehaviorSubject<Integer> clickTimes = BehaviorSubject.createDefault(0);

    public ItemViewModel(ObservableField<Article> article) {
        this.article = article;
    }

    public ObservableField<Article> getArticle() {
        Log.v("ItemViewModel", "getRepo " + article.get().getTitle());
        return article;
    }

    // todo 双方向バインドするときに使う？
    // 今は viewModel -> View のOneWayでしか使ってない。
    public void setArticle(ObservableField<Article> article) {
        Log.v("ItemViewModel", "setRepo");
        this.article = article;
    }

    public void onClick(View view) {
        Log.v("ItemViewModel : onClick", "ttttttttt");
        Log.v("ItemViewModel : onClick", article.get().getUrl());
        clickTimes.onNext(clickTimes.getValue() + 1);
    }
}

