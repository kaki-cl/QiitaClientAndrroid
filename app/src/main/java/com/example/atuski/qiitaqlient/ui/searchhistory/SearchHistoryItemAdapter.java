package com.example.atuski.qiitaqlient.ui.searchhistory;

import android.content.Context;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.example.atuski.qiitaqlient.databinding.SearchHistoryRowBinding;
import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.model.Query;
import com.example.atuski.qiitaqlient.ui.util.adapter.BindingHolder;
import com.example.atuski.qiitaqlient.ui.util.adapter.ObservableListRecyclerAdapter;

import java.util.List;


public class SearchHistoryItemAdapter extends ObservableListRecyclerAdapter<SearchHistoryItemViewModel, BindingHolder<SearchHistoryRowBinding>> {

    public List<SearchHistoryItemViewModel> list;
    private Context context;

    public SearchHistoryItemAdapter(Context context, ObservableList<SearchHistoryItemViewModel> list) {
        super(context, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BindingHolder<SearchHistoryRowBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BindingHolder<>(context, parent, R.layout.search_history_row);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingHolder<SearchHistoryRowBinding> holder, int position) {

//        SearchHistoryItemViewModel itemViewModel = getItem(position);

        SearchHistoryItemViewModel itemViewModel = getItem(position);

        SearchHistoryRowBinding binding = holder.binding;
//        binding.setQueryObj(query);
        binding.setItemViewModel(itemViewModel);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
