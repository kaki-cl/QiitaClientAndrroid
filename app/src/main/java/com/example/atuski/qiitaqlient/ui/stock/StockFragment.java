package com.example.atuski.qiitaqlient.ui.stock;

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
import com.example.atuski.qiitaqlient.databinding.StockFragmentBinding;


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

        stockViewModel = new StockViewModel();

        // アダプターの設定
        StockItemAdapter stockItemAdapter = new StockItemAdapter(
                getContext(),
                stockViewModel.stockItemViewModels);

        binding.stockList.setAdapter(stockItemAdapter);
        binding.stockList.setHasFixedSize(true);
        binding.stockList.setLayoutManager(new LinearLayoutManager(getContext()));

        stockViewModel.initStockItems(getArguments().getString("USER_ID"), binding);
    }
}