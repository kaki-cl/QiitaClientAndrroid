package com.example.atuski.qiitaqlient.ui.search;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.databinding.SearchFragmentBinding;
import com.example.atuski.qiitaqlient.repository.search.SearchRepository;
import com.example.atuski.qiitaqlient.ui.util.helper.EndlessScrollListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;


public class SearchViewModel {

    private Fragment appCompatActivity;
    private Context context;
    private SearchRepository repository;

    private String lastQuery;

    public SearchFragmentBinding binding;

    public String getLastQuery() {
        return lastQuery;
    }

    //todo ObservableListとitemViewModelsの必要性をまとめないと。
    protected ObservableList<SearchItemViewModel> searchItemViewModels;

    BehaviorSubject<List<SearchItemViewModel>> itemResults = BehaviorSubject.createDefault(Collections.emptyList());
    BehaviorSubject<List<SearchItemViewModel>> addedItemResults = BehaviorSubject.createDefault(Collections.emptyList());

    public final ObservableArrayList<String> searchHistory = new ObservableArrayList<>();

    public SearchViewModel(Fragment appCompatActivity, Context context) {

        this.appCompatActivity = appCompatActivity;
        this.context = context;
        this.repository = QiitaQlientApp.getInstance().getSearchRepository();
        this.searchItemViewModels = new ObservableArrayList<>();

    }

    public void initRecyclerViewEvent() {

        /** 検索結果の反映 */
        itemResults.subscribe((itemList) -> {
            SearchItemAdapter adapter = (SearchItemAdapter) binding.qiitaListActivity.getAdapter();
            adapter.clear();
            adapter.addAll(itemList);
        });

        /** onLoadによる追加検索結果の反映 */
        addedItemResults.subscribe(itemList -> {
            SearchItemAdapter adapter = (SearchItemAdapter) binding.qiitaListActivity.getAdapter();
            adapter.addAll(itemList);
        });

        /** 追加検索結果の実装 */
        RecyclerView searchItemsView = binding.searchFragmentContainer.findViewById(R.id.qiita_list_activity);
        searchItemsView.addOnScrollListener(new EndlessScrollListener((LinearLayoutManager) searchItemsView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page) {
                QiitaQlientApp.getInstance().getSearchRepository()
                        .searchArticle(lastQuery, page)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(articles -> {
                            addedItemResults.onNext(
                                    articles.stream()
                                            .map(article -> new SearchItemViewModel(new ObservableField<>(article), context))
                                            .collect(Collectors.toList())
                            );
                        });
            }
        });
    }

    public void setOnwindowFocusChangedListener(String searchQuery) {

        View view = binding.getRoot().findViewById(R.id.search_fragment_container);

        view.getViewTreeObserver().addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
            @Override
            public void onWindowFocusChanged(boolean hasFocus) {

                if (!hasFocus || searchQuery == null) {
                    return;
                }
                EditText editText = binding.getRoot().findViewById(R.id.search_edit_text);
                editText.setText(searchQuery);
                editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                editText.setSelection(editText.getText().length());

                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
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
                            itemResults.onNext(articles
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
