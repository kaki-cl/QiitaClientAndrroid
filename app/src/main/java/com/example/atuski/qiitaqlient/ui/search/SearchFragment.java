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

import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.databinding.SearchFragmentBinding;
import com.example.atuski.qiitaqlient.ui.util.helper.ResourceResolver;



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

            SearchItemAdapter searchItemAdapter = new SearchItemAdapter(
                    getContext(),
                    searchViewModel.searchItemViewModels);
            binding.qiitaListActivity.setAdapter(searchItemAdapter);
            binding.qiitaListActivity.setHasFixedSize(true);
            binding.qiitaListActivity.setLayoutManager(new LinearLayoutManager(getContext()));
            searchViewModel.binding = binding;

            searchViewModel.initRecyclerViewEvent();
        }
        loadLastSearchHistory(savedInstanceState);

        return binding.getRoot();
    }

    ViewTreeObserver.OnWindowFocusChangeListener m;

    /**
     * getArgumentsにあれば、別Activityからの再生成
     * savedInstanceStateにあれば、画面ローテーション時の再生成
     */
    private void loadLastSearchHistory(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        searchHistory = bundle.getString(resourceResolver.getString(R.string.LAST_QUERY));
        bundle.putString(resourceResolver.getString(R.string.LAST_QUERY), null);

        if (searchHistory == null && savedInstanceState != null) {
            searchHistory = savedInstanceState.getString(resourceResolver.getString(R.string.LAST_QUERY));
        }
        searchViewModel.setOnwindowFocusChangedListener(searchHistory);
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