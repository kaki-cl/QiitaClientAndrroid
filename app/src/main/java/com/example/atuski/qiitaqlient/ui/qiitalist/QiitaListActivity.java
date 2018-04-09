package com.example.atuski.qiitaqlient.ui.qiitalist;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.R;

import com.example.atuski.qiitaqlient.databinding.QiitaActivityListBinding;
import com.example.atuski.qiitaqlient.ui.qiitadetail.QiitaDetailActivity;
import com.example.atuski.qiitaqlient.ui.qiitalist.drawer.ActionbarDrawerToggleLister;

public class QiitaListActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "URL";

    private QiitaActivityListBinding binding;
    private MainViewModel mainViewModel;

    public QiitaQlientApp app;

    private ActionBarDrawerToggle mDrawerToggle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.app = new QiitaQlientApp();

        binding = DataBindingUtil.setContentView(this, R.layout.qiita_activity_list);
        mainViewModel = new MainViewModel(this, getApplicationContext());
        binding.setViewModel(mainViewModel);

        initDrawerView();
        initRecyclerView();
    }

    private void initDrawerView() {

        // アクションバーを表示
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ドロワーの開閉イベント
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        mDrawerToggle = new ActionbarDrawerToggleLister(
                this,          /* host Activity */
                binding.drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );
        binding.drawerLayout.setDrawerListener(mDrawerToggle);

        // ドロワーのクリックイベント
        binding.searchHistoryDrawer.setOnItemClickListener(
                (AdapterView<?> parent, View view, int position, long id) -> {

            TextView textView = (TextView) view;
            mainViewModel.onClickedDrawerItem(binding, textView.getText().toString());
        });
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
                        intent.putExtra(EXTRA_URL, item.article.get().getUrl());
                        startActivity(intent);
                    }
                });
            }

            // アダプターへの検索結果の更新
            qiitaItemAdapter.clear();
            qiitaItemAdapter.addAll(itemList);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mainViewModel.onOptionsItemSelected(mDrawerToggle, item);
    }
}
