package com.example.atuski.qiitaqlient;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.atuski.qiitaqlient.model.UserInfo;
import com.example.atuski.qiitaqlient.ui.search.SearchFragment;
import com.example.atuski.qiitaqlient.ui.search.SearchItemViewModel;
import com.example.atuski.qiitaqlient.ui.searchhistory.SearchHistoryFragment;
import com.example.atuski.qiitaqlient.ui.toolbar.ToolbarFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;


public class MainActivity extends AppCompatActivity {

    final BehaviorSubject<String> loginStatus = BehaviorSubject.createDefault("init");

    private UserInfo loginUserInfo;

    private String userId;

    private String userName;

    private String profileImageUrl;

    private boolean mIsLogin = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("MainActivityonCreate", "onCreate");

        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            loadUserAccount(savedInstanceState);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // このメソッドを呼び出すためには、invalidateOptionsMenu();をコールする

        return super.onPrepareOptionsMenu(menu);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(getResources().getString(R.string.IS_LOGIN), mIsLogin);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void loadUserAccount(Bundle savedInstanceState) {

        loginStatus.subscribe((loginStatus) -> {
            switch (loginStatus) {
                case "guestUser":
                    Log.v("loadUserAccount", "guestUser");
                    initViewPagerFragment(false);
                    break;
                case "loginUser":
                    Log.v("loadUserAccount", "loginUser");
                    initViewPagerFragment(true);
                    break;
                default:
                    break;
            }
        });

        Uri uri = getIntent().getData();

        if (savedInstanceState != null && savedInstanceState.getBoolean("isLogin")) {
            // 認証中の情報を使う。今はとりあえず再リクエストする。
            //　本来はセッションチェック
            Log.v("ログイン判定", "ログイン済み");
            QiitaQlientApp
                    .getInstance()
                    .getSearchRepository()
                    .fetchAccessToken(uri.getQueryParameter("code").toString())
                    .subscribeOn(Schedulers.io())
                    .subscribe((token -> {
                        Log.v("accessToken", token.getToken());
                        QiitaQlientApp.getInstance().getSearchRepository()
                                .fetchUserInfo(token.getToken())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe((userInfo -> {

                                    loginUserInfo = userInfo;

                                    if (loginUserInfo != null) {
                                        Log.v("loginUserInfo", "nullじゃない");
                                        Log.v("loginUserInfo", loginUserInfo.getId());

                                    } else {
                                        Log.v("loginUserInfo", "null");

                                    }

                                    userId = userInfo.getId();
//                                    userName = userInfo.getName();
//                                    profileImageUrl = userInfo.getProfile_image_url();

                                    if (loginUserInfo == null) {
                                        Log.v("loginUserInfo", "ゲストユーザー");
                                        loginStatus.onNext("guestUser");
                                    }

                                    mIsLogin = true;

                                    Log.v("loginUserInfo", "ログインユーザー");

                                    loginStatus.onNext("loginUser");
                                }));
                    }));
            return;
        }

        if (uri == null) {
            Log.v("ログイン判定", "ゲストユーザー");
            loginStatus.onNext("guestUser");
            return;
        }

        fetchLoginInfo(uri);
    }

    private void initViewPagerFragment(Boolean isLogin) {

        Bundle bundle = new Bundle();
        bundle.putBoolean(getResources().getString(R.string.IS_LOGIN), isLogin);

        if (isLogin) {
            bundle.putString(getResources().getString(R.string.USER_ID), userId);
        }

        String lastQuery = getIntent().getStringExtra(getResources().getString(R.string.LAST_QUERY));
        if (lastQuery != null) {
            bundle.putString(getResources().getString(R.string.LAST_QUERY), lastQuery);
            Log.v("MainActivity", lastQuery);
        }

        ToolbarFragment toolbarFragment = new ToolbarFragment();
        toolbarFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, toolbarFragment)
                .addToBackStack(null)
                .commit();
    }

    private void fetchLoginInfo(Uri uri) {

        Log.v("ログイン判定", "ログインチャレンジ");
        // ログインチャレンジ
        QiitaQlientApp
                .getInstance()
                .getSearchRepository()
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
                                if (loginUserInfo == null) {
                                    loginStatus.onNext("guestUser");
                                }
                                mIsLogin = true;
                                loginStatus.onNext("loginUser");
                            }));
                }));
    }
}