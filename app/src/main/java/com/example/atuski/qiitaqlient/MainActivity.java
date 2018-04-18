package com.example.atuski.qiitaqlient;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.atuski.qiitaqlient.ui.searchhistory.SearchHistoryActivity;
import com.example.atuski.qiitaqlient.ui.sub.SubFragment;
import com.example.atuski.qiitaqlient.ui.search.SearchFragment;
import com.example.atuski.qiitaqlient.ui.search.SearchViewModel;
import com.example.atuski.qiitaqlient.ui.trend.TrendFragment;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "URL";

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


        Intent intent = getIntent();
        String searchHistory = intent.getStringExtra(SearchHistoryActivity.FROM_SEARCH_HISTORY);
        SearchFragment searchFragment = new SearchFragment();
        if (searchHistory != null && searchHistory.length() != 0) {
            Bundle bundle = new Bundle();
            bundle.putString(SearchHistoryActivity.FROM_SEARCH_HISTORY, searchHistory);
            searchFragment.setArguments(bundle);
        }

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mPager = (ViewPager)findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewFragmentPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragments(searchFragment, "Search");
        viewPagerAdapter.addFragments(new TrendFragment(), "Trend");
        viewPagerAdapter.addFragments(new SubFragment(), "Sub");
        mPager.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_history:
                Intent intent = new Intent(this, SearchHistoryActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
