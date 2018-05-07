package com.example.atuski.qiitaqlient.ui.search;


import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
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
//    final BehaviorSubject<Map<SearchItemAdapter, List<SearchItemViewModel>>> itemResults = BehaviorSubject.createDefault(Collections.emptyList());

    public final ObservableArrayList<String> searchHistory = new ObservableArrayList<>();

    public SearchViewModel(Fragment appCompatActivity, Context context) {

        this.appCompatActivity = appCompatActivity;
        this.context = context;
        this.repository = QiitaQlientApp.getInstance().getSearchRepository();
        this.searchItemViewModels = new ObservableArrayList<>();

        // 直近の検索履歴から参照
//        searchHistory.addAll(repository.loadLatestSearchQuery());

        Log.v("SearchViewModel", "コンストラクタ itemResults change?");
        Log.v("SearchViewModel", String.valueOf(itemResults.hashCode()));
    }

    /**
     * 検索イベント処理
     */
    public View.OnKeyListener setOnKeyListener() {

        return new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                Log.v("keyboard test", "start");

                if (keyCode != keyEvent.KEYCODE_ENTER || keyEvent.getAction() != KeyEvent.ACTION_UP) {
                    return false;
                }

                EditText editText = (EditText) view;
//                InputMethodManager imm = (InputMethodManager) appCompatActivity.getSystemService(context.INPUT_METHOD_SERVICE);
                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);


                Log.v("keyboard test isActive", String.valueOf(imm.isActive()));
                Log.v("keyboard test isAcceptingText", String.valueOf(imm.isAcceptingText()));


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
                repository.searchArticle(text)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Article>>() {

                            @Override
                            public void onNext(List<Article> result) {
                                List<Article> articleList = new ArrayList<>();
                                for (Article r : result) {
                                    Article article = new Article();
                                    article.setTitle(r.title);
                                    article.setUrl(r.url);
                                    article.setUser(r.user);
                                    articleList.add(article);
                                    Log.v("SearchViewModel", article.title);

                                }

                                Log.v("SearchViewModel", "SearchViewModel itemResults hashCode2");
                                Log.v("SearchViewModel", String.valueOf(itemResults.hashCode()));

                                itemResults.onNext(articleList
                                        .stream()
                                        .map(article -> new SearchItemViewModel(new ObservableField<>(article), context))
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

                // 表示内容更新時に、一番上までスクロールする。
//                RecyclerView recyclerView = (RecyclerView) appCompatActivity.findViewById(R.id.qiita_list_activity);
//                recyclerView.scrollToPosition(0);
                return true;
            }
        };
    }
}
