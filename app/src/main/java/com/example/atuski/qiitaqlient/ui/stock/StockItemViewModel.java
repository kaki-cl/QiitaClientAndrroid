package com.example.atuski.qiitaqlient.ui.stock;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.view.View;

import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.model.Stock;
import com.example.atuski.qiitaqlient.ui.detail.DetailActivity;

import io.reactivex.subjects.BehaviorSubject;

public class StockItemViewModel {

    public ObservableField<Stock> stock;

    private final Context context;

    final BehaviorSubject<Integer> clickTimes = BehaviorSubject.createDefault(0);

    public StockItemViewModel(ObservableField<Stock> stock, Context context) {
        this.stock = stock;
        this.context = context;
    }

    public ObservableField<Stock> getStock() {
        return stock;
    }

    public void setStock(ObservableField<Stock> stock) {
        this.stock = stock;
    }

    public void onClick(View view) {

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(QiitaQlientApp
                .getInstance()
                .getResourceResolver()
                .getString(R.string.WEB_VIEW_URL), stock.get().url);
        context.startActivity(intent);

    }
}
