package com.example.atuski.qiitaqlient;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ListView;

import com.example.atuski.qiitaqlient.viewmodel.MainViewModel;
import com.example.atuski.qiitaqlient.views.adapter.QiitaItemAdapter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.atuski.qiitaqlient.databinding.QiitaActivityListBinding;

public class QiitaListActivity extends AppCompatActivity {

    private QiitaActivityListBinding binding;
    MainViewModel viewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.qiita_activity_list);
        viewModel = new MainViewModel();
        binding.setViewModel(viewModel);


        initListView();
    }

    private void initListView() {

        //アダプタの設定
        QiitaItemAdapter adapter = new QiitaItemAdapter(getApplicationContext(), viewModel.itemViewModels);
        binding.qiitaListActivity.setAdapter(adapter);
        binding.qiitaListActivity.setHasFixedSize(true);
        binding.qiitaListActivity.setLayoutManager(new LinearLayoutManager(this));




        // キーボード
        binding.editText.setOnKeyListener(new OnKeyListener(this, getApplicationContext(), adapter));
    }
}
