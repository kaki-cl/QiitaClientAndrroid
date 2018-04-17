package com.example.atuski.qiitaqlient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.atuski.qiitaqlient.databinding.QiitaActivityListBinding;
import com.example.atuski.qiitaqlient.ui.sub.SubFragment;
import com.example.atuski.qiitaqlient.ui.search.SearchFragment;
import com.example.atuski.qiitaqlient.ui.search.SearchViewModel;
import com.example.atuski.qiitaqlient.ui.trend.TrendFragment;

/**
 * Created by atuski on 2018/04/17.
 */

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "URL";

    private QiitaActivityListBinding binding;
    private SearchViewModel searchViewModel;
    private ActionBarDrawerToggle mDrawerToggle;

    public QiitaQlientApp app;
    private ViewFragmentPagerAdapter viewPagerAdapter;
    private ViewPager mPager;
    private TabLayout mTabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mPager = (ViewPager)findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewFragmentPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new SearchFragment(), "Search");
        viewPagerAdapter.addFragments(new TrendFragment(), "Trend");
        viewPagerAdapter.addFragments(new SubFragment(), "Sub");
        mPager.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mPager);
//        initDrawerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        EditText editText = (EditText) findViewById(R.id.edit_text);
//        Log.v("SearchFragment", editText.getText().toString());
        Log.v("MainActivity", "MainActivity");

    }

    //    private void initDrawerView() {
//
//        // アクションバーを表示
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//
////        Bundle args = new Bundle();
////        args.put("activity", this);
//        DrawerMenuFragment drawerMenuFragment = new DrawerMenuFragment();
////        drawerMenuFragment.setArguments(args);
////
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.activity_main, drawerMenuFragment)
//                .commit();
//    }

//    @Override
//    public void onDrawerClosed(View view) {
//        invalidateOptionsMenu();
//    }
//
//    @Override
//    public void onDrawerOpened(View drawerView) {
//        invalidateOptionsMenu();
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return searchViewModel.onOptionsItemSelected(mDrawerToggle, item);
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        searchViewModel.resetDrawerItemsList();
//
//        // ドロワーメニュー表示時に、一番上のアイテムを選択する。
//        binding.searchHistoryDrawer.setItemChecked(0, true);
//        return super.onPrepareOptionsMenu(menu);
//    }
}
