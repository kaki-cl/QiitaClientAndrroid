package com.example.atuski.qiitaqlient.ui.search;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.atuski.qiitaqlient.model.Article;
import com.example.atuski.qiitaqlient.ui.util.helper.EndlessScrollListener;
import com.example.atuski.qiitaqlient.ui.util.helper.ResourceResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;


public class SearchFragment extends Fragment {

    private SearchFragmentBinding binding;
    private SearchViewModel searchViewModel;

    private String searchHistory;
    private ResourceResolver resourceResolver;

    public SearchFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourceResolver = QiitaQlientApp.getInstance().getResourceResolver();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.v("SearchFragment", "onCreateView");

        if (savedInstanceState == null) {
            Log.v("SearchFragment", "savedInstanceState null");
            binding = DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false);
            searchViewModel = new SearchViewModel(this, getContext());
            binding.setViewModel(searchViewModel);
            setRetainInstance(true);
            initRecyclerView();
        }
        initSearchHistoryContainer(savedInstanceState);

        return binding.getRoot();
    }

    private void initRecyclerView() {

        Log.v("initRecyclerView", "initRecyclerView");

        SearchItemAdapter searchItemAdapter = new SearchItemAdapter(
                getContext(),
                searchViewModel.searchItemViewModels);
        binding.qiitaListActivity.setAdapter(searchItemAdapter);
        binding.qiitaListActivity.setHasFixedSize(true);
        binding.qiitaListActivity.setLayoutManager(new LinearLayoutManager(getContext()));

        searchViewModel.itemResults
                .subscribe((itemList) -> {
                    // アダプターへの検索結果の更新
                    searchItemAdapter.clear();
                    searchItemAdapter.addAll(itemList);
                });

        RecyclerView recyclerView = binding.searchFragmentContainer.findViewById(R.id.qiita_list_activity);
        searchViewModel.initRecyclerViewEvent(recyclerView);

        searchViewModel.itemResults2.subscribe(items -> {
            searchItemAdapter.addAll(items);
        });
    }

    ViewTreeObserver.OnWindowFocusChangeListener m;

    private void initSearchHistoryContainer(Bundle savedInstanceState) {

        Log.v("initSearchHistoryContainer", "initSearchHistoryContainer");

        /**
         * getArgumentsにあれば、別Activityからの再生成
         * savedInstanceStateにあれば、画面ローテーション時の再生成
         */
        // getArguments
        Bundle bundle = getArguments();
        searchHistory = bundle.getString(resourceResolver.getString(R.string.LAST_QUERY));
        bundle.putString(resourceResolver.getString(R.string.LAST_QUERY), null);
//        if (bundle != null) {
//        }

        if (searchHistory == null && savedInstanceState != null) {
            searchHistory = savedInstanceState.getString(resourceResolver.getString(R.string.LAST_QUERY));
        }


        if (searchHistory != null) {

            Log.v("searchHistorysearchHistory", searchHistory);
            // 画面回転用
//            searchHistory = savedInstanceState.getString(resourceResolver.getString(R.string.LAST_QUERY));
            View view = binding.getRoot().findViewById(R.id.search_fragment_container);
            ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            viewTreeObserver.addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
                @Override
                public void onWindowFocusChanged(boolean hasFocus) {

                    Log.v("onWindowFocusChanged", "検索");

                    if (!hasFocus || searchHistory == null) {

                        Log.v("onWindowFocusChanged", String.valueOf(hasFocus));
                        Log.v("onWindowFocusChanged", "null");
                        return;
                    }
                    Log.v("searchHistoryTest", searchHistory);

                    EditText editText = binding.getRoot().findViewById(R.id.search_edit_text);
                    editText.setText(searchHistory);
                    editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                    editText.setSelection(editText.getText().length());

                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);

                    Log.v("キーボード isActive", String.valueOf(imm.isActive()));

//                    imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//                    imm.toggleSoftInputFromWindow(binding.getRoot().getWindowToken(), 0, InputMethodManager.HIDE_IMPLICIT_ONLY);
//                    imm.toggleSoftInputFromWindow(binding.getRoot().getWindowToken(), 0, 0);

                    Log.v("after hideSoftInputFromWindow", String.valueOf(imm.isActive()));
//                    binding.getRoot().getViewTreeObserver().removeOnWindowFocusChangeListener(this);
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        Log.v("SearchFragment", "onSaveInstanceState");
        if (searchViewModel.getLastQuery() != null) {
            Log.v("SearchFragment", searchViewModel.getLastQuery());
            outState.putString(resourceResolver.getString(R.string.LAST_QUERY), searchViewModel.getLastQuery());
        }
        super.onSaveInstanceState(outState);
    }
}