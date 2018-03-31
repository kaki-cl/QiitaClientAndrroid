package com.example.atuski.qiitaqlient.ui.qiitalist;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.databinding.ListItemBinding;
import com.example.atuski.qiitaqlient.ui.adapter.BindingHolder;
import com.example.atuski.qiitaqlient.ui.adapter.ObservableListRecyclerAdapter;
import com.example.atuski.qiitaqlient.ui.qiitalist.ItemViewModel;

import java.util.List;

public class QiitaItemAdapter extends ObservableListRecyclerAdapter<ItemViewModel, BindingHolder<ListItemBinding>> {

    public List<ItemViewModel> list;
    private Context context;

    public QiitaItemAdapter(Context context, ObservableList<ItemViewModel> list) {
        super(context, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public BindingHolder<ListItemBinding> onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.v("QiitaItemAdapter", "onCreateViewHolder");


        // アイテム単位のレイアウトファイルのインフレート
        // viewHolderの生成
        return new BindingHolder<>(context, parent, R.layout.list_item);
    }

    @Override
    public void onBindViewHolder(BindingHolder<ListItemBinding> holder, int position) {

        //データを取得
        ItemViewModel itemViewModel = getItem(position);


        Log.v("QiitaItemAdapteronBindViewHolder", itemViewModel.repo.get().getTitle());

        //データを設定
        ListItemBinding binding = holder.binding;
        binding.setItemViewModel(itemViewModel);
    }

    @Override
    public int getItemCount() {

        Log.v("QiitaItemAdaptergetItemCount", String.valueOf(list.size()));

        return list.size();
    }
}
