package com.example.atuski.qiitaqlient.ui.adapter;

import android.content.Context;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class ObservableListRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends ArrayRecyclerAdapter<T, VH> {

    public ObservableListRecyclerAdapter(@NonNull Context context, @NonNull ObservableList<T> list) {
        super(context, list);

        list.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<T>>() {
            @Override
            public void onChanged(ObservableList<T> ts) {
                Log.v("ObservableListRecyclerAdapter", "onChanged");
                notifyDataSetChanged(); //データセットの変更をすべてのobserverに通知
            }

            @Override
            public void onItemRangeChanged(ObservableList<T> ts, int i, int i1) {
                Log.v("ObservableListRecyclerAdapter", "onItemRangeChanged");
                notifyItemRangeChanged(i, i1); // iからi1の範囲において、データの変更があったことをすべてのobserverに通知
            }

            @Override
            public void onItemRangeInserted(ObservableList<T> ts, int i, int i1) {
                Log.v("ObservableListRecyclerAdapter", "onItemRangeInserted");
                notifyDataSetChanged(); //データセットの変更をすべてのobserverに通知
//                notifyItemRangeInserted(i, i1); // iからi1の範囲において、データの挿入があったことをすべてのobserverに通知
            }

            @Override
            public void onItemRangeMoved(ObservableList<T> ts, int i, int i1, int i2) {
                Log.v("ObservableListRecyclerAdapter", "onItemRangeMoved");
                notifyItemMoved(i, i1); // iからi1へitemが移動したことをobserverに通知
            }

            @Override
            public void onItemRangeRemoved(ObservableList<T> ts, int i, int i1) {
                Log.v("ObservableListRecyclerAdapter", "onItemRangeRemoved");
                notifyItemRangeRemoved(i, i1); // iからi1の範囲のitemがデータセットから削除されたことを通知
            }
        });
    }
}
