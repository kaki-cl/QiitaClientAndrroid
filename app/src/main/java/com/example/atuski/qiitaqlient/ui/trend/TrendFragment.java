package com.example.atuski.qiitaqlient.ui.trend;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.databinding.TrendFragmentBinding;
import com.example.atuski.qiitaqlient.ui.search.SearchItemAdapter;

/**
 * Created by atuski on 2018/04/17.
 */

public class TrendFragment extends Fragment {

    private TrendViewModel trendViewModel;
    private TrendFragmentBinding binding;

    public TrendFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trendViewModel = new TrendViewModel(getContext());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.trend_fragment, container, false);
        binding.setViewModel(trendViewModel);
        initRecyclerView();
        return binding.getRoot();
    }

    private void initRecyclerView() {

        //アダプターの設定
        SearchItemAdapter searchItemAdapter = new SearchItemAdapter(
                getContext(),
                trendViewModel.searchItemViewModels);

        binding.trendList.setAdapter(searchItemAdapter);
        binding.trendList.setHasFixedSize(true);
        binding.trendList.setLayoutManager(new LinearLayoutManager(getContext()));

        trendViewModel.itemResults.subscribe((itemList) -> {

            // 各アイテムのクリックイベントを実装
//            for (SearchItemViewModel item : itemList) {
//                item.clickTimes.subscribe((clickTimes) -> {
//                    if (0 < clickTimes) {
//                        Intent intent = new Intent(getContext(), DetailActivity.class);
//                        intent.putExtra(EXTRA_URL, item.article.get().getUrl());
//                        startActivity(intent);
//                    }
//                });
//            }

            // アダプターへの検索結果の更新
            searchItemAdapter.clear();
            searchItemAdapter.addAll(itemList);
        });
    }
}
