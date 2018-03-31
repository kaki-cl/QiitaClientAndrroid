package com.example.atuski.qiitaqlient.ui.qiitalist;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.R;

import com.example.atuski.qiitaqlient.databinding.QiitaActivityListBinding;

public class QiitaListActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "URL";

    private QiitaActivityListBinding binding;
    private MainViewModel mainViewModel;

    public QiitaQlientApp app;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.app = new QiitaQlientApp();

        binding = DataBindingUtil.setContentView(this, R.layout.qiita_activity_list);
        mainViewModel = new MainViewModel(this, getApplicationContext());
        binding.setViewModel(mainViewModel);

        initRecyclerView();
    }

    private void initRecyclerView() {

        //アダプターの設定
        QiitaItemAdapter qiitaItemAdapter = new QiitaItemAdapter(
                getApplicationContext(),
                mainViewModel.itemViewModels);

        binding.qiitaListActivity.setAdapter(qiitaItemAdapter);
        binding.qiitaListActivity.setHasFixedSize(true);
        binding.qiitaListActivity.setLayoutManager(new LinearLayoutManager(this));

        mainViewModel.itemResults.subscribe((itemList) -> {

            // 各アイテムのクリックイベントを実装
            for (ItemViewModel item : itemList) {
                item.clickTimes.subscribe((clickTimes) -> {
                    if (0 < clickTimes) {
                        Intent intent = new Intent(getApplication(), QiitaDetailActivity.class);
                        intent.putExtra(EXTRA_URL, item.repo.get().getUrl());
                        startActivity(intent);
                    }
                });
            }

            // アダプターへの検索結果の更新
            qiitaItemAdapter.clear();
            qiitaItemAdapter.addAll(itemList);
        });

    }
}
