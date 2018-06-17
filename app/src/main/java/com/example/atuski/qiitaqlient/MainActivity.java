package com.example.atuski.qiitaqlient;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.atuski.qiitaqlient.api.SyncronizePostUserQlient;
import com.example.atuski.qiitaqlient.model.Followee;
import com.example.atuski.qiitaqlient.model.UserInfo;
import com.example.atuski.qiitaqlient.repository.followee.FollowRepository;
import com.example.atuski.qiitaqlient.repository.userinfo.UserInfoRepository;
import com.example.atuski.qiitaqlient.ui.searchhistory.SearchHistoryFragment;
import com.example.atuski.qiitaqlient.ui.toolbar.ToolbarFragment;
import com.google.firebase.iid.FirebaseInstanceId;

import io.reactivex.subjects.BehaviorSubject;

/**
 * todo
 *
 * ＠追加したい機能
 *
 * followee一覧ページを作成する。
 * followeeをクリックするとfolloweeの投稿ページへ。
 *
 * ソートなど検索条件を選べるようにする。
 */
public class MainActivity extends AppCompatActivity {

    final BehaviorSubject<String> loginStatus = BehaviorSubject.createDefault("init");

    private UserInfo loginUserInfo;

//    private Bundle savedInstanceState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("MainActivityonCreate", "onCreate");

        String token = FirebaseInstanceId.getInstance().getToken();
        if (token != null) {
            Log.v("MainActivityonCreate", token);
        } else {
            Log.v("MainActivityonCreate", "token null");
        }



        setContentView(R.layout.main_activity);
        Uri uri = getIntent().getData();
        if (savedInstanceState != null && uri == null) {
            Log.v("MainActivityonCreate", "return");

            UserInfoRepository.getInstance(getApplicationContext())
                    .loadUserInfo(uri)
                    .subscribe(userInfo -> {
                        loginUserInfo = userInfo;
                    });

            return;
        }

        UserInfoRepository.getInstance(getApplicationContext())
                .loadUserInfo(uri)
                .subscribe(userInfo -> {
                    loginUserInfo = userInfo;
                    if (userInfo.isLogin) {
                        // todo loginStatusをhashMapにするのもありかも。
                        // そしたら loginUserInfoをpackage privateにする必要がなくなる
                        loginStatus.onNext("loginUser");
                    } else {
                        loginStatus.onNext("guestUser");
                    }
                });

        loginStatus.subscribe((loginStatus) -> {
            switch (loginStatus) {
                case "loginUser":
                    Log.v("loadUserAccount", "loginUser");
                    initViewPagerFragment(true);
                    break;
                case "guestUser":
                    Log.v("loadUserAccount", "guestUser");
                    initViewPagerFragment(false);
                    break;
                default:
                    break;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v("MainActivityonCreate", "onCreateOptionsMenu");
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        if (loginUserInfo.isLogin) {
            menu.findItem(R.id.login).setVisible(false);

        } else {
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.syncronizePostUser).setVisible(false);
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
                UserInfoRepository.getInstance(getApplicationContext()).startAuthViewIntent();
                break;
            case R.id.logout:
                UserInfoRepository.getInstance(getApplicationContext()).deleteLocalUserInfo();
                Log.v("deleteLocalUserInfo", "deleteLocalUserInfo");
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
                break;
            case R.id.syncronizePostUser:
                QiitaQlientApp.getInstance().getFollowRepository()
                        .searchFollowees(loginUserInfo.id)
                        .subscribe(followees -> {
                            for (Followee followee : followees) {
                                SyncronizePostUserQlient.getInstance().requestSynchronization(loginUserInfo.id, followee.id);
                            }
                        });
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
}