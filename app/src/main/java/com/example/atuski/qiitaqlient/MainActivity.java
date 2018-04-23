package com.example.atuski.qiitaqlient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.atuski.qiitaqlient.ui.searchhistory.SearchHistoryFragment;
import com.example.atuski.qiitaqlient.ui.toolbar.ToolbarFragment;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "URL";

    public QiitaQlientApp app;
    private ViewFragmentPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // レイアウトファイルからFragmentを呼び込むようにした。
        setContentView(R.layout.main_activity);

        Log.v("MainActivity", "onCreate");

        ToolbarFragment toolbarFragment = new ToolbarFragment();
//        getFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_container, toolbarFragment)
//                .commit();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, toolbarFragment)
                .commit();


        // SearchHistory用の処理
        // todo ViewPagerの処理を他に譲渡するかどうか
        /**
         * ViewPagerFragmentを作って、
         *   そっちにBundle経由で渡すという方法がある。
         *   これをやるには他のFragmentの例を見てもう少し経験値が必要そう。
         */
//        Intent intent = getIntent();
//        String searchHistory = intent.getStringExtra(SearchHistoryActivity.FROM_SEARCH_HISTORY);
//        SearchFragment searchFragment = new SearchFragment();
//        if (searchHistory != null && searchHistory.length() != 0) {
//            Bundle bundle = new Bundle();
//            bundle.putString(SearchHistoryActivity.FROM_SEARCH_HISTORY, searchHistory);
//            searchFragment.setArguments(bundle);
//        }
//
//        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        viewPagerAdapter = new ViewFragmentPagerAdapter(getSupportFragmentManager());
//        viewPagerAdapter.addFragments(searchFragment, "Search");
//        viewPagerAdapter.addFragments(new TrendFragment(), "Trend");
//        viewPagerAdapter.addFragments(new SubFragment(), "Sub");
//        viewPager.setAdapter(viewPagerAdapter);
//        tabLayout.setupWithViewPager(viewPager);
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
                SearchHistoryFragment searchHistoryFragment = new SearchHistoryFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, searchHistoryFragment)
                        .addToBackStack(null)
                        .commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.v("MainActivity", "onBackPressed");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
