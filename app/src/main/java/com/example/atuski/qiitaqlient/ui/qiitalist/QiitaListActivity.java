package com.example.atuski.qiitaqlient.ui.qiitalist;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.R;

import com.example.atuski.qiitaqlient.databinding.QiitaActivityListBinding;

public class QiitaListActivity extends AppCompatActivity {

    private QiitaActivityListBinding binding;
    private MainViewModel viewModel;

    public QiitaQlientApp app;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.app = new QiitaQlientApp();

        binding = DataBindingUtil.setContentView(this, R.layout.qiita_activity_list);
        viewModel = new MainViewModel(this, getApplicationContext());
        binding.setViewModel(viewModel);

        initRecyclerView();
    }

    private void initRecyclerView() {

        //アダプターの設定
        QiitaItemAdapter qiitaItemAdapter = new QiitaItemAdapter(getApplicationContext(), viewModel.itemViewModels);
        binding.qiitaListActivity.setAdapter(qiitaItemAdapter);
        binding.qiitaListActivity.setHasFixedSize(true);
        binding.qiitaListActivity.setLayoutManager(new LinearLayoutManager(this));

        // アダプターへの検索結果の更新
        viewModel.itemResults.subscribe((result) -> {

            qiitaItemAdapter.clear();
            qiitaItemAdapter.addAll(result);
        });

    }
}
