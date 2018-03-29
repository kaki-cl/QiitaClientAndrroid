package com.example.atuski.qiitaqlient.repository;

import com.example.atuski.qiitaqlient.api.QiitaService;
import com.example.atuski.qiitaqlient.data.Repo;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by atuski on 2018/03/27.
 */

public class QiitaListRepository {

    /**
     * 1. ここのメソッドをたたいてもらう
     * 2. DBにあるかサーチ
     * 3. なければapiにリクエスト
     * 4. DBに結果保存
     * 5. DBから結果を取り出す
     *
     *
     * dbインスタンス
     * daoインスタンス
     * serviceインスタンス
     * repositoryインスタンス
     *
     * repositoryインスタンスのんかにserviceインスタンスを保存するとかでも良さそう。
     */

    //To avoid that Non Static field cannot be referenced from a static context
    private static QiitaListRepository sInstance;

    private Retrofit retrofit;
    private QiitaService qiitaService;

    private QiitaListRepository() {

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://qiita.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.qiitaService = retrofit.create(QiitaService.class);
    }

    public static QiitaListRepository getInstance() {

        if (sInstance == null) {
            sInstance = new QiitaListRepository();
        }
        return sInstance;
    }

    public Observable<List<Repo>> searchRepo(String query){
         if (false) {
             // todo DBに問い合わせ
         }

        return this.qiitaService.listRepos(query);
    }

}
