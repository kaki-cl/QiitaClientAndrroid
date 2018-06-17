package com.example.atuski.qiitaqlient.repository.stock;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.ObservableField;
import android.util.Log;

import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.api.QiitaClient;
import com.example.atuski.qiitaqlient.repository.userinfo.UserInfoRepository;
import com.example.atuski.qiitaqlient.ui.stock.StockItemViewModel;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class StockRepository {

    private static StockRepository sInstance;

    private QiitaClient qiitaClient;

    private Context context;

    public BehaviorSubject<List<StockItemViewModel>> stocks = BehaviorSubject.createDefault(Collections.emptyList());

    private StockRepository(Context context) {

        this.context = context;
        qiitaClient = QiitaClient.getInstance();
    }

    public static StockRepository getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new StockRepository(context);
        }
        return sInstance;
    }

    public void searchStockItems(String userId) {

        qiitaClient.qiitaService.getStockItems(userId, 1, 20, UserInfoRepository.getInstance(context).getSavedAuthHeaderValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fetchedStocks -> {
                    stocks.onNext(fetchedStocks
                            .stream()
                            .map(stock -> new StockItemViewModel(new ObservableField<>(stock), context))
                            .collect(Collectors.toList()));
                }, error -> {
                    Log.v("searchStockItems", "ERROR");
                    error.printStackTrace();
                });
    }

    public Completable stockArticle(String articleId) {

        Log.v("stockArticle", "stockArticle");
        SharedPreferences data = context.getSharedPreferences(context.getResources().getString(R.string.USER_INFO), Context.MODE_PRIVATE);
        String authHeaderValue = data.getString(context.getResources().getString(R.string.AUTHORIZATION_HEADER_VALUE), null);

        return qiitaClient.qiitaService
                .stockArticle(articleId, authHeaderValue)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
