package com.example.atuski.qiitaqlient.ui.util.adapter;

import android.content.Context;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.Collection;
import java.util.List;

public abstract class ObservableListRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected final List<T> list;

    final Context context;

    public ObservableListRecyclerAdapter(@NonNull Context context, @NonNull ObservableList<T> list) {
        this.list = list;
        this.context = context;

        list.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<T>>() {
            @Override
            public void onChanged(ObservableList<T> ts) {
                Log.v("ObservableListRecyclerAdapter", "onChanged");
                notifyDataSetChanged(); //データセットの変更をすべてのobserverに通知
            }

            @Override
            public void onItemRangeChanged(ObservableList<T> ts, int i, int i1) {
                notifyItemRangeChanged(i, i1); // iからi1の範囲において、データの変更があったことをすべてのobserverに通知
            }

            @Override
            public void onItemRangeInserted(ObservableList<T> ts, int i, int i1) {
                notifyDataSetChanged(); //データセットの変更をすべてのobserverに通知
            }

            @Override
            public void onItemRangeMoved(ObservableList<T> ts, int i, int i1, int i2) {
                notifyItemMoved(i, i1); // iからi1へitemが移動したことをobserverに通知
            }

            @Override
            public void onItemRangeRemoved(ObservableList<T> ts, int i, int i1) {
                notifyItemRangeRemoved(i, i1); // iからi1の範囲のitemがデータセットから削除されたことを通知
            }
        });
    }

    @UiThread
    public void reset(Collection<T> items) {
        clear();
        addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
    }

    public void addAll(Collection<T> items) {


        Log.v("ObservableListRecyclerAdapter", "ここまできてる？");
        list.addAll(items);
    }

    public T getItem(int position) {
        return list.get(position);
    }

}
