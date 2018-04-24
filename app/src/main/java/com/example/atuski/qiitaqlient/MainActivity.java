package com.example.atuski.qiitaqlient;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.atuski.qiitaqlient.ui.search.SearchItemViewModel;
import com.example.atuski.qiitaqlient.ui.searchhistory.SearchHistoryFragment;
import com.example.atuski.qiitaqlient.ui.toolbar.ToolbarFragment;
//import com.example.atuski.qiitaqlient.databinding.MainActivityBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "URL";

    public QiitaQlientApp app;
    private ViewFragmentPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    final BehaviorSubject<String> accessToken = BehaviorSubject.createDefault("");

    final BehaviorSubject<String> isLogin = BehaviorSubject.createDefault("init");

    private String userId;

//    public MainActivityBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("MainActivityonCreate", "onCreate");
        setContentView(R.layout.main_activity);
        loadUserAccount();

        // SearchHistory用の処理
        // todo ViewPagerの処理を他に譲渡するかどうか
        /**
         * ViewPagerFragmentを作って、そっちにBundle経由で渡すという方法がある。
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
    protected void onStart() {
        super.onStart();

        Log.v("MainActivityonCreate", "onStart");
        // こっちでやったら記事のページから戻ってもおかしくならない？
//        setContentView(R.layout.main_activity);
//        loadUserAccount();

        isLogin.subscribe((isLogin) -> {
            if (isLogin == "guestUser") {
                Log.v("isLogin", "false");

                Bundle bundle = new Bundle();
                bundle.putBoolean("isLogin", false);
                ToolbarFragment toolbarFragment = new ToolbarFragment();
                toolbarFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, toolbarFragment)
                        .commit();
            } else if (isLogin == "loginUser"){
                Log.v("isLogin", "true");

                Bundle bundle = new Bundle();
                bundle.putBoolean("isLogin", true);

                bundle.putString("USER_ID", userId);

                ToolbarFragment toolbarFragment = new ToolbarFragment();
                toolbarFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, toolbarFragment)
                        .commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void loadUserAccount() {

        Uri uri = getIntent().getData();
        if (uri == null) {
            Log.v("loadUserAccount", "Guest User");
            isLogin.onNext("guestUser");
            return;
        }

        QiitaQlientApp.getInstance().getSearchRepository()
                .fetchAccessToken(uri.getQueryParameter("code").toString())
                .subscribeOn(Schedulers.io())
                .subscribe((token -> {
                    Log.v("accessToken", token.getToken());
                    QiitaQlientApp.getInstance().getSearchRepository()
                            .fetchUserInfo(token.getToken())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe((userInfo -> {
                                userId = userInfo.getId();
                                Log.v("id", userInfo.getId());
                                Log.v("name", userInfo.getName());
                                isLogin.onNext("loginUser");
                            }));
                }));
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
                break;

            case R.id.login:
                String mClientId = "dfd44c0b8c380894cac1ea43ff4b815a2661e461";
                String mScope = "read_qiita write_qiita";
                String mState = "bb17785d811bb1913ef54b0a7657de780defaa2d";//todo to be random
                String uri = "https://qiita.com/api/v2/oauth/authorize?" +
                        "client_id=" + mClientId +
                        "&scope=" + mScope +
                        "&state=" + mState;

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
