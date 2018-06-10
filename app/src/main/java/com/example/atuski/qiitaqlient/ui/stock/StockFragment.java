package com.example.atuski.qiitaqlient.ui.stock;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.databinding.StockFragmentBinding;
import com.example.atuski.qiitaqlient.ui.detail.DetailActivity;
import com.example.atuski.qiitaqlient.ui.search.SearchItemAdapter;
import com.example.atuski.qiitaqlient.ui.trend.TrendViewModel;


public class StockFragment extends Fragment {

    private StockViewModel stockViewModel;
    public StockFragmentBinding binding;

    public StockFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.stock_fragment, container, false);
        initRecyclerView();
        return binding.getRoot();
    }

    private void initRecyclerView() {

        stockViewModel = new StockViewModel(getContext(), getArguments().getString("USER_ID"));
        stockViewModel.initStockItems(getArguments().getString("USER_ID"), binding);

        // アダプターの設定
        StockItemAdapter stockItemAdapter = new StockItemAdapter(
                getContext(),
                stockViewModel.stockItemViewModels);

        binding.stockList.setAdapter(stockItemAdapter);
        binding.stockList.setHasFixedSize(true);
        binding.stockList.setLayoutManager(new LinearLayoutManager(getContext()));

        stockViewModel.itemResults.subscribe((itemList) -> {
            // アダプターへの検索結果の更新
            stockItemAdapter.clear();
            stockItemAdapter.addAll(itemList);
        });
    }
}