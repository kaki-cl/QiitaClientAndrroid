package com.example.atuski.qiitaqlient.ui.search;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.model.Article;
import com.example.atuski.qiitaqlient.ui.detail.DetailActivity;

import io.reactivex.subjects.BehaviorSubject;


public class SearchItemViewModel {

    public ObservableField<Article> article;

    private final Context context;

    final BehaviorSubject<Integer> clickTimes = BehaviorSubject.createDefault(0);

    public SearchItemViewModel(ObservableField<Article> article, Context context) {
        this.article = article;
        this.context = context;
    }

    public ObservableField<Article> getArticle() {
        return article;
    }

    public void setArticle(ObservableField<Article> article) {
        this.article = article;
    }

    public void onClick(View view) {

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(QiitaQlientApp
                .getInstance()
                .getResourceResolver()
                .getString(R.string.WEB_VIEW_URL), article.get().url);
        context.startActivity(intent);
    }
}

