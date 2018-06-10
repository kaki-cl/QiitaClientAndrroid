package com.example.atuski.qiitaqlient.ui.stock;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.databinding.StockFragmentBinding;
import com.example.atuski.qiitaqlient.repository.stock.StockRepository;


public class StockViewModel {

    protected ObservableList<StockItemViewModel> stockItemViewModels;

    private StockRepository stockRepository;

    public StockViewModel() {
        this.stockRepository = QiitaQlientApp.getInstance().getStockRepository();
        this.stockItemViewModels = new ObservableArrayList<>();
    }

    public void initStockItems(String userId, StockFragmentBinding binding) {

        StockItemAdapter stockItemAdapter = (StockItemAdapter) binding.stockList.getAdapter();

        stockRepository.stocks.subscribe((stockItemList) -> {
            stockItemAdapter.clear();
            stockItemAdapter.addAll(stockItemList);
        });

        stockRepository.searchStockItems(userId);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) binding.stockSwipeRefresh;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                Log.v("onRefresh", "onRefresh");
                stockRepository.searchStockItems(userId);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);// インディケーターがずっと回り続けないように
                }
            }
        });
    }
}
