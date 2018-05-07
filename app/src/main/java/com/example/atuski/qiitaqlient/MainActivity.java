package com.example.atuski.qiitaqlient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.atuski.qiitaqlient.model.UserInfo;
import com.example.atuski.qiitaqlient.ui.searchhistory.SearchHistoryFragment;
import com.example.atuski.qiitaqlient.ui.toolbar.ToolbarFragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;


public class MainActivity extends AppCompatActivity {

    final BehaviorSubject<String> loginStatus = BehaviorSubject.createDefault("init");

    private UserInfo loginUserInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("MainActivityonCreate", "onCreate");

        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            loadUserAccount();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        if (loginUserInfo.isLogin) {
            MenuItem loginItem = menu.findItem(R.id.login);
            loginItem.setVisible(false);
        } else {
            MenuItem logoutItem = menu.findItem(R.id.logout);
            logoutItem.setVisible(false);
        }

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
                break;

            case R.id.login:
                String mClientId = "dfd44c0b8c380894cac1ea43ff4b815a2661e461";
                String mScope = "read_qiita write_qiita";
                String mState = "bb17785d811bb1913ef54b0a7657de780defaa2d";//todo to be random
                String uri = "https://qiita.com/api/v2/oauth/authorize?" +
                        "client_id=" + mClientId +
                        "&scope=" + mScope +
                        "&state=" + mState;
                Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(browseIntent);
                break;

            case R.id.logout:
                deleteLocalUserInfo();
                Log.v("deleteLocalUserInfo", "deleteLocalUserInfo");
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("CheckResult")
    private void loadUserAccount() {

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

        // 認証ページから戻ってきたときの処理
        Uri uri = getIntent().getData();
        if (uri != null) {
            fetchUserInfo(uri);
            return;
        }

        if (loadLocalUserInfo()) {
            Log.v("ログイン判定", "ログインユーザー");
            loginStatus.onNext("loginUser");
        } else {
            Log.v("ログイン判定", "ゲストユーザー");
            loginStatus.onNext("guestUser");
        }
    }

    private void initViewPagerFragment(Boolean isLogin) {

        Bundle bundle = new Bundle();
        bundle.putBoolean(getResources().getString(R.string.IS_LOGIN), isLogin);

        if (isLogin) {
            bundle.putString(getResources().getString(R.string.USER_ID), loginUserInfo.id);
//            bundle.putString(getResources().getString(R.string.USER_NAME), loginUserInfo.name);
            bundle.putString(getResources().getString(R.string.PROFILE_IMAGE_URL), loginUserInfo.profile_image_url);
        }

        String lastQuery = getIntent().getStringExtra(getResources().getString(R.string.LAST_QUERY));
        if (lastQuery != null) {
            bundle.putString(getResources().getString(R.string.LAST_QUERY), lastQuery);
        }

        ToolbarFragment toolbarFragment = new ToolbarFragment();
        toolbarFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, toolbarFragment)
                .addToBackStack(null)
                .commit();
    }

    @SuppressLint("CheckResult")
    private void fetchUserInfo(Uri uri) {

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

                                if (userInfo != null) {
                                    saveUserInfo(userInfo);
                                }

                                if (loadLocalUserInfo()) {
                                    loginStatus.onNext("loginUser");
                                } else {
                                    loginStatus.onNext("guestUser");
                                }
                            }));
                }));
    }

    private boolean loadLocalUserInfo() {

        Log.v("loadLocalUserInfo", "loadLocalUserInfo");
        SharedPreferences data = getSharedPreferences(getResources().getString(R.string.USER_INFO), Context.MODE_PRIVATE);
        boolean isLogin = data.getBoolean(getResources().getString(R.string.IS_LOGIN), false);

        if (!isLogin) {
            Log.v("isLogin", "false");

            UserInfo userInfo = new UserInfo();
            userInfo.isLogin = false;
            loginUserInfo = userInfo;
            return false;
        }

        UserInfo userInfo = new UserInfo();
        userInfo.isLogin = isLogin;
        userInfo.id = data.getString(getResources().getString(R.string.USER_ID), null);
        userInfo.profile_image_url = data.getString(getResources().getString(R.string.PROFILE_IMAGE_URL), null);
        loginUserInfo = userInfo;
        return true;
    }


    private void saveUserInfo(UserInfo latestUserInfo) {

        SharedPreferences data = getSharedPreferences(getResources().getString(R.string.USER_INFO), getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(getResources().getString(R.string.IS_LOGIN), true);
        editor.putString(getResources().getString(R.string.USER_ID), latestUserInfo.id);
        editor.putString(getResources().getString(R.string.PROFILE_IMAGE_URL), latestUserInfo.profile_image_url);
        editor.apply();
    }

    private void deleteLocalUserInfo() {
        deleteSharedPreferences(getResources().getString(R.string.USER_INFO));
    }
}