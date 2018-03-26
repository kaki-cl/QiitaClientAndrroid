package com.example.atuski.qiitaqlient;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.atuski.qiitaqlient.data.Repo;
import com.example.atuski.qiitaqlient.data.User;
import com.example.atuski.qiitaqlient.viewmodel.ItemViewModel;
import com.example.atuski.qiitaqlient.views.adapter.QiitaItemAdapter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import com.annimon.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class OnKeyListener implements View.OnKeyListener {

    private AppCompatActivity app;
    private Context context;
    private QiitaItemAdapter qiitaItemAdapter;

    OnKeyListener(AppCompatActivity app, Context context, QiitaItemAdapter qiitaItemAdapter) {
        this.app = app;
        this.context = context;
        this.qiitaItemAdapter = qiitaItemAdapter;

    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

        Log.v("OnKeyListener", "onKey");

        if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyCode == keyEvent.KEYCODE_ENTER) {

            //todo なにしてる？
            EditText editText = (EditText) view;
            InputMethodManager imm = (InputMethodManager) app.getSystemService(context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

            String text = editText.getText().toString();
            if (TextUtils.isEmpty(text)) {
                return true;
            }

            // URLエンコード
            try {
                text = URLEncoder.encode(text, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e("", e.toString());
                return true;
            }


            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://qiita.com")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();


            QiitaClient.QiitaServise servise = retrofit.create(QiitaClient.QiitaServise.class);
            Observable<List<Repo>> observable = servise.listRepos(text)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            observable.subscribe((result) -> {

                qiitaItemAdapter.clear();

                List<Repo> repos = new ArrayList<>();
                for (Repo r : result) {
                            Repo repo = new Repo();
                            User user = new User();
                            user.setProfile_image_url(r.getUser().profile_image_url);
                            repo.setUser(user);
                            repo.setId(r.id);
                            repo.setTitle(r.title);
                            repo.setUrl(r.url);
                            repos.add(repo);

                    Log.d("search debug", r.title);
                    Log.d("search debug", r.url);
                }
                //todo
                // List<ItemViewModel>への変換を行ってから、addAllメソッドに突っ込んでる。
                //StreamはJava8のAPIの方じゃだめ？
                qiitaItemAdapter.list.addAll(Stream.of(repos).map(repo -> new ItemViewModel(new ObservableField<>(repo))).toList());

            });




        }

        return false;
    }
}
