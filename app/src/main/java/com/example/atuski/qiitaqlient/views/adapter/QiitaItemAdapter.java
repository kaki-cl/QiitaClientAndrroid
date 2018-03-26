package com.example.atuski.qiitaqlient.views.adapter;

import android.content.Context;
import android.databinding.ObservableList;
import android.util.Log;
import android.view.ViewGroup;

import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.databinding.ListItemBinding;
import com.example.atuski.qiitaqlient.viewmodel.ItemViewModel;

import java.util.List;

public class QiitaItemAdapter extends ObservableListRecyclerAdapter<ItemViewModel, BindingHolder<ListItemBinding>> {

    public List<ItemViewModel> list;
    private Context context;

    public QiitaItemAdapter(Context context, ObservableList<ItemViewModel> list) {
        super(context, list);
        this.list = list;
        this.context = context;
    }

    //ここで
    // データの取得とviewHolderへの設定を行う。
    @Override
    public BindingHolder<ListItemBinding> onCreateViewHolder(ViewGroup parent, int viewType) {

        //todo そもそもこれがコールされてない！！
        Log.v("QiitaItemAdapter", "onCreateViewHolder");

//        // アイテム単位のレイアウトファイルのインフレート
//        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//        // viewHolderの生成
//        return new OriginalViewHolder(inflate);
        return new BindingHolder<>(context, parent, R.layout.list_item);
    }

    @Override
    public void onBindViewHolder(BindingHolder<ListItemBinding> holder, int position) {
        //データを取得
        ItemViewModel itemViewModel = this.list.get(position);

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
