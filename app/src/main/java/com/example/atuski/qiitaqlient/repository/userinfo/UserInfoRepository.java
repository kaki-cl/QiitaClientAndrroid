package com.example.atuski.qiitaqlient.repository.userinfo;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.api.QiitaClient;
import com.example.atuski.qiitaqlient.api.RegisterQlientUserQlient;
import com.example.atuski.qiitaqlient.model.Token;
import com.example.atuski.qiitaqlient.model.User;
import com.example.atuski.qiitaqlient.model.UserInfo;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class UserInfoRepository {

    public BehaviorSubject<UserInfo> userInfoResult = BehaviorSubject.create();

    private Context context;

    private static UserInfoRepository sInstance;

    private QiitaClient qiitaClient;

    private UserInfoRepository(Context context) {
        this.context = context;
        qiitaClient = QiitaClient.getInstance();
    }

    public static UserInfoRepository getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new UserInfoRepository(context);
        }
        return sInstance;
    }

    public Observable<UserInfo> loadUserInfo(Uri uri) {

        Log.v("UserInfoRepository", "loadUserInfo");

        if (uri == null) {
            return this.loadLocalUserInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }

        return this.fetchRemoteUserInfo(uri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<UserInfo> fetchRemoteUserInfo(Uri uri) {

        Log.v("UserInfoRepository", "fetchRemoteUserInfo");

        String code = uri.getQueryParameter("code").toString();

        this.fetchAccessToken(code)
                .subscribeOn(Schedulers.io())
                .subscribe(token -> {
                    this.getUserInfoFromAccessToken(token.token)
                    .subscribeOn(Schedulers.io())
                    .subscribe(userInfo -> {
                        userInfo.isLogin = true;
                        this.saveUserInfo(userInfo);
                        this.registerQlientUser(userInfo.id);
                        userInfoResult.onNext(userInfo);
                    });
                });

        return userInfoResult;
    }

    private Observable<Token> fetchAccessToken(String code) {

        Log.v("UserInfoRepository", "fetchAccessToken");

        //todo strings.xmlへ
        String clientId = "dfd44c0b8c380894cac1ea43ff4b815a2661e461";
        String clientSecret = "093660d3c232d54d33c09b7c2d9465ad8bb60202";
        return qiitaClient.qiitaService.getAccessToken(clientId, clientSecret, code);
    }

    private Observable<UserInfo> getUserInfoFromAccessToken(String accessToken) {

        Log.v("UserInfoRepository", "getUserInfoFromAccessToken");

        String baseValue = "Bearer ";
        String value = baseValue + accessToken;
        Log.v("accessToken", value);

        SharedPreferences data = context.getSharedPreferences(context.getResources().getString(R.string.USER_INFO), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(context.getResources().getString(R.string.AUTHORIZATION_HEADER_VALUE), value);
        editor.apply();

        return qiitaClient.qiitaService.getUserInfo(value);
    }

    private void saveUserInfo(UserInfo latestUserInfo) {

        Log.v("UserInfoRepository", "saveUserInfo");

        SharedPreferences data = context.getSharedPreferences(context.getResources().getString(R.string.USER_INFO), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(context.getResources().getString(R.string.IS_LOGIN), true);
        editor.putString(context.getResources().getString(R.string.USER_ID), latestUserInfo.id);
        editor.putString(context.getResources().getString(R.string.PROFILE_IMAGE_URL), latestUserInfo.profile_image_url);
        editor.apply();
    }

    private void registerQlientUser(String qlientUserId) {

        Log.v("UserInfoRepository", "registerQlientUser");

        HashMap<String, String> postParameters= new HashMap<>();
        postParameters.put("qlientUserId", qlientUserId);
        postParameters.put("deviceToken", FirebaseInstanceId.getInstance().getToken());
        RegisterQlientUserQlient.getInstance().mService
                .registerQlientUser(postParameters)
                .subscribeOn(Schedulers.io())
                .subscribe(lambdaResult -> {
                    Log.v("registerQlientUser", "OK");
                    Log.v("RegisterQlientUserQlient", lambdaResult.result);
                }, error -> {
                    Log.v("registerQlientUser", "ERROR");
                    error.printStackTrace();
                });
    }

    private Observable<UserInfo> loadLocalUserInfo() {

        Log.v("loadLocalUserInfo", "loadLocalUserInfo");
        SharedPreferences data = context.getSharedPreferences(context.getResources().getString(R.string.USER_INFO), Context.MODE_PRIVATE);
        boolean isLogin = data.getBoolean(context.getResources().getString(R.string.IS_LOGIN), false);

        if (!isLogin) {
            Log.v("isLogin", "false");

            UserInfo userInfo = new UserInfo();
            userInfo.isLogin = false;
            return BehaviorSubject.createDefault(userInfo);
        }

        UserInfo userInfo = new UserInfo();
        userInfo.isLogin = isLogin;
        userInfo.id = data.getString(context.getResources().getString(R.string.USER_ID), null);
        userInfo.profile_image_url = data.getString(context.getResources().getString(R.string.PROFILE_IMAGE_URL), null);
        return BehaviorSubject.createDefault(userInfo);
    }

    public void deleteLocalUserInfo() {
        context.deleteSharedPreferences(context.getResources().getString(R.string.USER_INFO));;
    }

    public void startAuthViewIntent() {
        //todo 定数に
        String mClientId = "dfd44c0b8c380894cac1ea43ff4b815a2661e461";
        String mScope = "read_qiita write_qiita";
        String mState = "bb17785d811bb1913ef54b0a7657de780defaa2d";//todo to be random
        String uri = "https://qiita.com/api/v2/oauth/authorize?" +
                "client_id=" + mClientId +
                "&scope=" + mScope +
                "&state=" + mState;
        Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(browseIntent);
    }

    public String getSavedAuthHeaderValue() {

        SharedPreferences data = context.getSharedPreferences(context.getResources().getString(R.string.USER_INFO), context.MODE_PRIVATE);
        return data.getString(context.getResources().getString(R.string.AUTHORIZATION_HEADER_VALUE), null);
    }
}
