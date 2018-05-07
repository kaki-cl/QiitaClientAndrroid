package com.example.atuski.qiitaqlient.ui.searchhistory;

//import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.atuski.qiitaqlient.MainActivity;
import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.ui.detail.DetailActivity;
import com.example.atuski.qiitaqlient.ui.search.SearchFragment;
import com.example.atuski.qiitaqlient.ui.util.helper.ResourceResolver;

//import com.example.atuski.qiitaqlient.databinding.SearchHistoryBinding;

import java.util.ArrayList;

/**
 * Created by atuski on 2018/04/20.
 */

public class SearchHistoryFragment extends Fragment {

    private Context context;
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private AppCompatActivity activity;

    private SearchHistoryMainViewModel mainViewModel;

    private ResourceResolver resourceResolver;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        mainViewModel = new SearchHistoryMainViewModel();

        resourceResolver = QiitaQlientApp.getInstance().getResourceResolver();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.search_history, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.searchHistoryToolbar);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);

        setHasOptionsMenu(true);
        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        SearchHistoryItemAdapter itemAdapter = new SearchHistoryItemAdapter(
                getContext(),
                mainViewModel.searchHistoryItemViewModels);

        recyclerView = (RecyclerView) view.findViewById(R.id.search_history_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(itemAdapter);

        mainViewModel.queryResults.subscribe((itemList) -> {

            for (SearchHistoryItemViewModel item : itemList) {
                item.clickTimes.subscribe((clickTimes) -> {
                    if (0 < clickTimes) {
                        Log.v("SearchHistoryFragment", item.query.get().getQuery());
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra(resourceResolver.getString(R.string.LAST_QUERY), item.query.get().getQuery());
                        startActivity(intent);

                    }
                });
            }

            itemAdapter.clear();
            itemAdapter.addAll(itemList);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.v("SearchHistoryFragment", "onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                // 一個前の画面に戻る
                activity.getSupportFragmentManager()
                        .popBackStackImmediate();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
