package com.example.atuski.qiitaqlient.ui.search;


import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.databinding.SearchFragmentBinding;
import com.example.atuski.qiitaqlient.model.Article;
import com.example.atuski.qiitaqlient.repository.search.SearchRepository;
import com.example.atuski.qiitaqlient.ui.util.helper.EndlessScrollListener;
import com.trello.rxlifecycle2.RxLifecycle;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;


public class SearchViewModel {

    private Fragment appCompatActivity;
    private Context context;
    private SearchRepository repository;

    private String lastQuery;

    public String getLastQuery() {
        return lastQuery;
    }

    //todo ObservableListとitemViewModelsの必要性をまとめないと。
    protected ObservableList<SearchItemViewModel> searchItemViewModels;

    BehaviorSubject<List<SearchItemViewModel>> itemResults = BehaviorSubject.createDefault(Collections.emptyList());
    BehaviorSubject<List<SearchItemViewModel>> itemResults2 = BehaviorSubject.createDefault(Collections.emptyList());
//    final BehaviorSubject<Map<SearchItemAdapter, List<SearchItemViewModel>>> itemResults = BehaviorSubject.createDefault(Collections.emptyList());

    public final ObservableArrayList<String> searchHistory = new ObservableArrayList<>();

    private RecyclerView recyclerView;

    public SearchViewModel(Fragment appCompatActivity, Context context) {

        this.appCompatActivity = appCompatActivity;
        this.context = context;
        this.repository = QiitaQlientApp.getInstance().getSearchRepository();
        this.searchItemViewModels = new ObservableArrayList<>();

    }

    public void initRecyclerViewEvent(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;

        this.recyclerView.addOnScrollListener(new EndlessScrollListener((LinearLayoutManager) this.recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page) {
                // Load
                Log.v("onLoadMore", "onLoadMore");
                Log.v("onLoadMore", String.valueOf(page));

                QiitaQlientApp.getInstance().getSearchRepository()
                        .searchArticle(lastQuery, page)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(articles -> {
                            List<Article> articleList = new ArrayList<>();
                            for (Article r : articles) {
                                Article article = new Article();
                                article.setTitle(r.title);
                                article.setUrl(r.url);
                                article.setUser(r.user);
                                article.setId(r.id);
                                articleList.add(article);
                                Log.v("SearchViewModel", article.title);
                                Log.v("SearchViewModel", (r.id));
                            }
                            itemResults2.onNext(
                                    articleList
                                            .stream()
                                            .map(article -> new SearchItemViewModel(new ObservableField<>(article), context))
                                            .collect(Collectors.toList())
                            );

                        });
            }
        });
    }

    /**
     * 検索イベント処理
     */
    public View.OnKeyListener setOnKeyListener() {

        return new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode != keyEvent.KEYCODE_ENTER || keyEvent.getAction() != KeyEvent.ACTION_UP) {
                    return false;
                }

                EditText editText = (EditText) view;
//                InputMethodManager imm = (InputMethodManager) appCompatActivity.getSystemService(context.INPUT_METHOD_SERVICE);
                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
//                    imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                String text = editText.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    return true;
                }

                // URLエンコード
                try {
                    text = URLEncoder.encode(text, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return true;
                }

                lastQuery = text;
                repository.searchArticle(text, 1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(articles -> {

                            List<Article> articleList = new ArrayList<>();
                            for (Article r : articles) {
                                Article article = new Article();
                                article.setTitle(r.title);
                                article.setUrl(r.url);
                                article.setUser(r.user);
                                article.setId(r.id);
                                articleList.add(article);
                                Log.v("SearchViewModel", article.title);
                                Log.v("SearchViewModel", (r.id));
                            }

                            Log.v("SearchViewModel", "SearchViewModel itemResults hashCode2");
                            Log.v("SearchViewModel", String.valueOf(itemResults.hashCode()));

                            itemResults.onNext(articleList
                                    .stream()
                                    .map(article -> new SearchItemViewModel(new ObservableField<>(article), context))
                                    .collect(Collectors.toList())
                            );


                        });


                // 表示内容更新時に、一番上までスクロールする。
//                RecyclerView recyclerView = (RecyclerView) appCompatActivity.findViewById(R.id.qiita_list_activity);
//                recyclerView.scrollToPosition(0);
                return true;
            }
        };
    }
}
