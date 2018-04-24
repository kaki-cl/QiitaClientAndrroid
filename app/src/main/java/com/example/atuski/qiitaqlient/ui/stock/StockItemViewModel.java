package com.example.atuski.qiitaqlient.ui.stock;

import android.databinding.ObservableField;
import android.view.View;

import com.example.atuski.qiitaqlient.model.Stock;

import io.reactivex.subjects.BehaviorSubject;

public class StockItemViewModel {

    public ObservableField<Stock> stock;

    final BehaviorSubject<Integer> clickTimes = BehaviorSubject.createDefault(0);

    public StockItemViewModel(ObservableField<Stock> stock) {
        this.stock = stock;
    }

    public ObservableField<Stock> getStock() {
        return stock;
    }

    public void setStock(ObservableField<Stock> stock) {
        this.stock = stock;
    }

    public void onClick(View view) {
        clickTimes.onNext(clickTimes.getValue() + 1);
    }
}
