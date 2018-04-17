package com.example.atuski.qiitaqlient.ui.trend;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.model.Article;
import com.example.atuski.qiitaqlient.repository.trend.TrendRepository;
import com.example.atuski.qiitaqlient.ui.search.SearchItemViewModel;

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

public class TrendViewModel {

    private Context context;

    //todo trendItemViewModelに変更
    protected ObservableList<SearchItemViewModel> searchItemViewModels;
    final BehaviorSubject<List<SearchItemViewModel>> itemResults = BehaviorSubject.createDefault(Collections.emptyList());
    private TrendRepository repository;

    public TrendViewModel(Context context) {

        this.context = context;
        this.repository = QiitaQlientApp.getInstance().getTrendRepository();
        this.searchItemViewModels = new ObservableArrayList<>();
    }

    /**
     * 検索イベント処理
     */
    public View.OnKeyListener setOnKeyListener() {
        return new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                Log.v("ここまできてる？", "ここまできてる？");

                if (keyCode != keyEvent.KEYCODE_ENTER || keyEvent.getAction() != KeyEvent.ACTION_UP) {
                    return false;
                }

                EditText editText = (EditText) view;
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
