package com.example.atuski.qiitaqlient.ui.stock;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.databinding.StockFragmentBinding;
import com.example.atuski.qiitaqlient.ui.detail.DetailActivity;
import com.example.atuski.qiitaqlient.ui.search.SearchItemAdapter;
import com.example.atuski.qiitaqlient.ui.trend.TrendViewModel;

import static com.example.atuski.qiitaqlient.MainActivity.EXTRA_URL;

public class StockFragment extends Fragment {

    private StockViewModel stockViewModel;
    private StockFragmentBinding binding;

    public StockFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stockViewModel = new StockViewModel(getContext(), getArguments().getString("USER_ID"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.stock_fragment, container, false);
        initRecyclerView();
        return binding.getRoot();
    }

    private void initRecyclerView() {

        // アダプターの設定
        StockItemAdapter stockItemAdapter = new StockItemAdapter(
                getContext(),
                stockViewModel.stockItemViewModels);

        binding.stockList.setAdapter(stockItemAdapter);
        binding.stockList.setHasFixedSize(true);
        binding.stockList.setLayoutManager(new LinearLayoutManager(getContext()));

        stockViewModel.itemResults.subscribe((itemList) -> {

            // 各アイテムのクリックイベントを実装
            for (StockItemViewModel item : itemList) {
                item.clickTimes.subscribe((clickTimes) -> {
                    if (0 < clickTimes) {
                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        intent.putExtra(EXTRA_URL, item.stock.get().getUrl());
                        startActivity(intent);
                    }
                });
            }

            // アダプターへの検索結果の更新
            stockItemAdapter.clear();
            stockItemAdapter.addAll(itemList);
        });
    }
}