package com.example.atuski.qiitaqlient.ui.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by e10dokup on 2017/05/05.
 */

public abstract class ArrayRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected final List<T> list;

    final Context context;

    public ArrayRecyclerAdapter(@NonNull Context context) {
        this(context, new ArrayList<T>());
    }

    public ArrayRecyclerAdapter(Context context, @NonNull List<T> list) {
        this.list = list;
        this.context = context;
        Log.v("ArrayRecyclerAdapter", "コンストラクタ");

    }

    @UiThread
    public void reset(Collection<T> items) {
        clear();
        addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {

        Log.v("ArrayRecyclerAdapter", "clear");
        list.clear();
    }

    public void addAll(Collection<T> items) {

        Log.v("ArrayRecyclerAdapter", "addAll");
        list.addAll(items);
    }

    public T getItem(int position) {

        Log.v("ArrayRecyclerAdapter", "getItem");
        return list.get(position);
    }
}
