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
import com.example.atuski.qiitaqlient.ui.util.helper.ResourceResolver;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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

        super.onCreate(savedInstanceState);
        searchViewModel = new SearchViewModel(this, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false);
        binding.setViewModel(searchViewModel);
        setRetainInstance(true);

        initRecyclerView();
        initSearchHistoryContainer(savedInstanceState);

        return binding.getRoot();
    }

    /**
     * RecyclerViewの設定
     */
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
        if (bundle != null) {
            searchHistory = bundle.getString(resourceResolver.getString(R.string.LAST_QUERY));
            bundle.putString(resourceResolver.getString(R.string.LAST_QUERY), null);
        }

        if (savedInstanceState != null) {

            Log.v("SearchFragment savedInstanceState", "nullじゃない");


            // 画面回転用
            searchHistory = searchViewModel.getLastQuery();
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

        } else {

            Log.v("SearchFragment savedInstanceState", "null");


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

                    EditText editText = binding.getRoot().findViewById(R.id.search_edit_text);
                    editText.setText(searchHistory);
                    editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                    editText.setSelection(editText.getText().length());

                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                    Log.v("キーボード isActive", String.valueOf(imm.isActive()));
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
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