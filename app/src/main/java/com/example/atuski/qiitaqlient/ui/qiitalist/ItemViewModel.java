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
        return article;
    }

    public void setArticle(ObservableField<Article> article) {
        this.article = article;
    }

    public void onClick(View view) {
        clickTimes.onNext(clickTimes.getValue() + 1);
    }
}

