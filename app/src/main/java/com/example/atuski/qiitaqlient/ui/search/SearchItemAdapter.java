package com.example.atuski.qiitaqlient.ui.search;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.databinding.SearchListItemBinding;
import com.example.atuski.qiitaqlient.model.User;
import com.example.atuski.qiitaqlient.ui.util.adapter.BindingHolder;
import com.example.atuski.qiitaqlient.ui.util.adapter.ObservableListRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchItemAdapter extends ObservableListRecyclerAdapter<SearchItemViewModel, BindingHolder<SearchListItemBinding>> {

    public List<SearchItemViewModel> list;
    private Context context;

    public SearchItemAdapter(Context context, ObservableList<SearchItemViewModel> list) {
        super(context, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public BindingHolder<SearchListItemBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
        // アイテム単位のレイアウトファイルのインフレート
        // viewHolderの生成
        return new BindingHolder<>(context, parent, R.layout.search_list_item);
    }

    @Override
    public void onBindViewHolder(BindingHolder<SearchListItemBinding> holder, int position) {

        // データを取得
        SearchItemViewModel searchItemViewModel = getItem(position);

        // データを設定
        SearchListItemBinding binding = holder.binding;
        binding.setSearchItemViewModel(searchItemViewModel);
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
