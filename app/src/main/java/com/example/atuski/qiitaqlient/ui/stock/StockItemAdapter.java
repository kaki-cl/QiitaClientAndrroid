package com.example.atuski.qiitaqlient.ui.stock;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.databinding.StockListItemBinding;
import com.example.atuski.qiitaqlient.model.Stock;
import com.example.atuski.qiitaqlient.model.User;
import com.example.atuski.qiitaqlient.ui.search.SearchItemViewModel;
import com.example.atuski.qiitaqlient.ui.util.adapter.BindingHolder;
import com.example.atuski.qiitaqlient.ui.util.adapter.ObservableListRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StockItemAdapter extends ObservableListRecyclerAdapter<StockItemViewModel, BindingHolder<StockListItemBinding>> {

    public List<StockItemViewModel> list;
    private Context context;

    public StockItemAdapter(Context context, ObservableList<StockItemViewModel> list) {
        super(context, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public BindingHolder<StockListItemBinding> onCreateViewHolder(ViewGroup parent, int viewType) {

        // アイテム単位のレイアウトファイルのインフレート
        // viewHolderの生成
        return new BindingHolder<>(context, parent, R.layout.stock_list_item);
    }

    @Override
    public void onBindViewHolder(BindingHolder<StockListItemBinding> holder, int position) {

        // データを取得
        StockItemViewModel stockItemViewModel = getItem(position);

        // データを設定
        StockListItemBinding binding = holder.binding;
        binding.setItemViewModel(stockItemViewModel);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @BindingAdapter("android:imageUrl")
    public static void setImage(ImageView imageView, User user) {
        Picasso.get().load(user.getProfile_image_url()).into(imageView);
    }
}
