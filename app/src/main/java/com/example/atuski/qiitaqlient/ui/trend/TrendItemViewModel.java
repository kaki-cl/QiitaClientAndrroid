package com.example.atuski.qiitaqlient.ui.trend;

import android.databinding.ObservableField;
import android.view.View;

import com.example.atuski.qiitaqlient.model.Article;

import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by atuski on 2018/04/17.
 */

public class TrendItemViewModel {

    public ObservableField<Article> article;

    final BehaviorSubject<Integer> clickTimes = BehaviorSubject.createDefault(0);

    public TrendItemViewModel(ObservableField<Article> article) {
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
