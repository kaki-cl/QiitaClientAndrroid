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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    //todo ObservableListとitemViewModelsの必要性をまとめないと。
    protected ObservableList<SearchItemViewModel> searchItemViewModels;
    final BehaviorSubject<List<SearchItemViewModel>> itemResults = BehaviorSubject.createDefault(Collections.emptyList());

    public final ObservableArrayList<String> searchHistory = new ObservableArrayList<>();

    public SearchViewModel(Fragment appCompatActivity, Context context) {

        this.appCompatActivity = appCompatActivity;
        this.context = context;
        this.repository = QiitaQlientApp.getInstance().getSearchRepository();
        this.searchItemViewModels = new ObservableArrayList<>();

        // 直近の検索履歴から参照
//        searchHistory.addAll(repository.loadLatestSearchQuery());
    }

    /**
     * 検索イベント処理
     */
    public View.OnKeyListener setOnKeyListener() {

        Log.v("SearchViewModel", "setOnKeyListener init");

        return new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                Log.v("ここまできてる？", "ここまできてる？");

                if (keyCode != keyEvent.KEYCODE_ENTER || keyEvent.getAction() != KeyEvent.ACTION_UP) {
                    return false;
                }

                EditText editText = (EditText) view;
//                InputMethodManager imm = (InputMethodManager) appCompatActivity.getSystemService(context.INPUT_METHOD_SERVICE);
                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

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
                                }

                                itemResults.onNext(articleList
                                        .stream()
                                        .map(article -> new SearchItemViewModel(new ObservableField<>(article)))
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
