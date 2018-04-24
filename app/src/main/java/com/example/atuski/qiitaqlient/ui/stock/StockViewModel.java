package com.example.atuski.qiitaqlient.ui.stock;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.model.Article;
import com.example.atuski.qiitaqlient.model.Stock;
import com.example.atuski.qiitaqlient.repository.stock.StockRepository;
import com.example.atuski.qiitaqlient.repository.trend.TrendRepository;
import com.example.atuski.qiitaqlient.ui.search.SearchItemViewModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class StockViewModel {

    private Context context;

    protected ObservableList<StockItemViewModel> stockItemViewModels;

    final BehaviorSubject<List<StockItemViewModel>> itemResults = BehaviorSubject.createDefault(Collections.emptyList());

    private StockRepository repository;

    public StockViewModel(Context context, String userId) {

        this.context = context;
        this.repository = QiitaQlientApp.getInstance().getStockRepository();
        this.stockItemViewModels = new ObservableArrayList<>();

        initStockItems(userId);
    }

    private void initStockItems(String userId) {

        repository.searchStockItems(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Stock>>() {

                    @Override
                    public void onNext(List<Stock> result) {

                        List<Stock> stockList = new ArrayList<>();
                        for (Stock r : result) {
                            Stock stock = new Stock();
                            stock.setTitle(r.title);
                            stock.setUser(r.user);
                            stock.setUrl(r.url);
                            stock.setLikesCount(r.likesCount);
                            stockList.add(stock);
                        }

                        itemResults.onNext(stockList
                                .stream()
                                .map(stock -> new StockItemViewModel(new ObservableField<>(stock)))
                                .collect(Collectors.toList())
                        );
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onComplete() {}
                });
    }
}
