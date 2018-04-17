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
import android.widget.EditText;

import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.databinding.SearchFragmentBinding;
import com.example.atuski.qiitaqlient.ui.detail.DetailActivity;

public class SearchFragment extends Fragment {

    public static final String EXTRA_URL = "URL";

    private SearchFragmentBinding binding;
    private SearchViewModel searchViewModel;

    public QiitaQlientApp app;


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
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        //todo dispatchKeyEventが動かない。
        EditText editText = binding.getRoot().findViewById(R.id.search_edit_text);
        Log.v("SearchFragment", editText.getKeyListener().getClass().toString());
        Log.v("SearchFragment", String.valueOf(editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))));
        editText.setSelection(editText.getText().length());
//        binding.editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
//        binding.editText.setSelection(binding.editText.getText().toString().length());
        Log.v("SearchFragment", binding.searchEditText.getText().toString());
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
}
