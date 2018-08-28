package com.example.atuski.qiitaqlient.ui.searchhistory;

import android.databinding.ObservableField;
import android.view.View;

import com.example.atuski.qiitaqlient.model.Query;

import io.reactivex.subjects.BehaviorSubject;

//todo やっぱりこのクラスいる。各アイテムのクリックイベントを実装したいから。
//拡張することを考えて、StringではなくQueryを使おう。
public class SearchHistoryItemViewModel {

    public ObservableField<Query> query;

    final BehaviorSubject<Integer> clickTimes = BehaviorSubject.createDefault(0);

    public SearchHistoryItemViewModel(ObservableField<Query> query) {
        this.query = query;
    }

    public ObservableField<Query> getQuery() {
        return query;
    }

    public void setQuery(ObservableField<Query> query) {
        this.query = query;
    }

    public void onClick(View view) {
        clickTimes.onNext(1);
//        TextView textView = (TextView) view.findViewById(R.id.search_history_item);
    }
}
