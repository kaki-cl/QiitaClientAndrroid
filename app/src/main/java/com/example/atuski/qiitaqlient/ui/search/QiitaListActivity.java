package com.example.atuski.qiitaqlient.ui.search;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.atuski.qiitaqlient.ViewFragmentPagerAdapter;
import com.example.atuski.qiitaqlient.QiitaQlientApp;

import com.example.atuski.qiitaqlient.databinding.QiitaActivityListBinding;
import com.viewpagerindicator.TitlePageIndicator;

public class QiitaListActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "URL";

    private QiitaActivityListBinding binding;
    private SearchViewModel searchViewModel;

    public QiitaQlientApp app;

    private ActionBarDrawerToggle mDrawerToggle;

    private ViewFragmentPagerAdapter viewPagerAdapter;
    private ViewPager mPager;
    private TitlePageIndicator mIndicator;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.app = new QiitaQlientApp();

//        binding = DataBindingUtil.setContentView(this, R.layout.qiita_activity_list);
//        searchViewModel = new SearchViewModel(this, getApplicationContext());
//        binding.setViewModel(searchViewModel);

        viewPagerAdapter = new ViewFragmentPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new SearchFragment(), "Search");

//        mPager = (ViewPager)findViewById(R.id.pager);
//        mPager.setAdapter(viewPagerAdapter);
//
//        TitlePageIndicator indicator = (TitlePageIndicator)findViewById(R.id.indicator);
//        mIndicator = indicator;
//        indicator.setViewPager(mPager);


//        initDrawerView();
//        initRecyclerView();
    }

    private void initDrawerView() {

        // アクションバーを表示
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ドロワーの開閉イベント
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        mDrawerToggle = new ActionbarDrawerToggleLister(
//                this,          /* host Activity */
//                binding.drawerLayout,  /* DrawerLayout object */
//                toolbar,               /* nav drawer image to replace 'Up' caret */
//                R.string.drawer_open,  /* "open drawer" description for accessibility */
//                R.string.drawer_close  /* "close drawer" description for accessibility */
//        );
//        binding.drawerLayout.setDrawerListener(mDrawerToggle);
//
//        // ドロワーのクリックイベント
//        binding.searchHistoryDrawer.setOnItemClickListener(
//                (AdapterView<?> parent, View view, int position, long id) -> {
//
//            TextView textView = (TextView) view;
//            searchViewModel.onClickedDrawerItem(binding, textView.getText().toString());
//        });
    }

    private void initRecyclerView() {

        //アダプターの設定
//        SearchItemAdapter qiitaItemAdapter = new SearchItemAdapter(
//                getApplicationContext(),
//                searchViewModel.searchItemViewModels);
//
//        binding.qiitaListActivity.setAdapter(qiitaItemAdapter);
//        binding.qiitaListActivity.setHasFixedSize(true);
//        binding.qiitaListActivity.setLayoutManager(new LinearLayoutManager(this));
//
//        searchViewModel.itemResults.subscribe((itemList) -> {
//
//            // 各アイテムのクリックイベントを実装
//            for (SearchItemViewModel item : itemList) {
//                item.clickTimes.subscribe((clickTimes) -> {
//                    if (0 < clickTimes) {
//                        Intent intent = new Intent(getApplication(), DetailActivity.class);
//                        intent.putExtra(EXTRA_URL, item.article.get().getUrl());
//                        startActivity(intent);
//                    }
//                });
//            }
//
//            // アダプターへの検索結果の更新
//            qiitaItemAdapter.clear();
//            qiitaItemAdapter.addAll(itemList);
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return searchViewModel.onOptionsItemSelected(mDrawerToggle, item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchViewModel.resetDrawerItemsList();

        // ドロワーメニュー表示時に、一番上のアイテムを選択する。
        binding.searchHistoryDrawer.setItemChecked(0, true);
        return super.onPrepareOptionsMenu(menu);
    }
}
