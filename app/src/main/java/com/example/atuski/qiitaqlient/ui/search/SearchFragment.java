package com.example.atuski.qiitaqlient.ui.search;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.databinding.SearchFragmentBinding;
import com.example.atuski.qiitaqlient.ui.detail.DetailActivity;
import com.example.atuski.qiitaqlient.ui.searchhistory.SearchHistoryActivity;

public class SearchFragment extends Fragment {

    public static final String EXTRA_URL = "URL";

    private SearchFragmentBinding binding;
    private SearchViewModel searchViewModel;

    public QiitaQlientApp app;
    private String searchHistory;

    public SearchFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.app = new QiitaQlientApp();
        super.onCreate(savedInstanceState);
        searchViewModel = new SearchViewModel(this, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false);
        binding.setViewModel(searchViewModel);

        initRecyclerView();
        initSearchHistoryContainer();

        return binding.getRoot();
    }

    private void initRecyclerView() {

        //アダプターの設定
        SearchItemAdapter searchItemAdapter = new SearchItemAdapter(
                getContext(),
                searchViewModel.searchItemViewModels);

        binding.qiitaListActivity.setAdapter(searchItemAdapter);
        binding.qiitaListActivity.setHasFixedSize(true);
        binding.qiitaListActivity.setLayoutManager(new LinearLayoutManager(getContext()));

        searchViewModel.itemResults.subscribe((itemList) -> {

            // 各アイテムのクリックイベントを実装
            for (SearchItemViewModel item : itemList) {
                item.clickTimes.subscribe((clickTimes) -> {
                    if (0 < clickTimes) {
                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        intent.putExtra(EXTRA_URL, item.article.get().getUrl());
                        startActivity(intent);
                    }
                });
            }

            // アダプターへの検索結果の更新
            searchItemAdapter.clear();
            searchItemAdapter.addAll(itemList);
        });
    }

    private void initSearchHistoryContainer() {

        if (getArguments() != null) {
            searchHistory = getArguments().getString(SearchHistoryActivity.FROM_SEARCH_HISTORY);
            getArguments().putString(SearchHistoryActivity.FROM_SEARCH_HISTORY, null);
        }

        View view = binding.getRoot().findViewById(R.id.search_fragment_container);
        view.getViewTreeObserver().addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
            @Override
            public void onWindowFocusChanged(boolean hasFocus) {

                Log.v("SearchFragment", "onWindowFocusChanged");

                if (!hasFocus) {
                    return;
                }

                EditText editText = binding.getRoot().findViewById(R.id.search_edit_text);
                if (editText.getText().length() == 0 && searchHistory == null) {
                    return;
                }

                if (searchHistory != null && searchHistory.length() != 0) {
                    Log.v("SearchFragment", "second true");
                    editText.setText(searchHistory);
                    searchHistory = null;
                }

                editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                editText.setSelection(editText.getText().length());
                //todo キーボードを閉じるようにする
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
