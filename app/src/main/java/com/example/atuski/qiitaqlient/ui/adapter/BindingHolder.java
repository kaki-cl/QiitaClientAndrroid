package com.example.atuski.qiitaqlient.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by atuski on 2018/03/26.
 */

public class BindingHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public final T binding;

    public BindingHolder(@NonNull Context context, @NonNull ViewGroup parent, @LayoutRes int layoutResId) {
        // Viewツリーを生成してitemViewフィールドに保存してそう。
        super(LayoutInflater.from(context).inflate(layoutResId, parent, false));
        // このBindingHolderのbindingフィールドとViewツリーを関連付けている。
        // このbindingにアクセスすることでバインドさせたいデータを同期させられる。
        binding = DataBindingUtil.bind(itemView);
    }
}