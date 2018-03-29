package com.example.atuski.qiitaqlient.ui.qiitalist;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.annimon.stream.Stream;
import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.model.OrmaDatabase;
import com.example.atuski.qiitaqlient.model.Repo;
import com.example.atuski.qiitaqlient.model.Repo_Selector;
import com.example.atuski.qiitaqlient.model.User;
import com.example.atuski.qiitaqlient.repository.QiitaListRepository;
import com.github.gfx.android.orma.Inserter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;


public class MainViewModel {

    private AppCompatActivity appCompatActivity;
    private Context context;
    private QiitaListRepository repository;

    //todo ObservableListとitemViewModelsの必要性をまとめないと。
    protected ObservableList<ItemViewModel> itemViewModels;
    final BehaviorSubject<List<ItemViewModel>> itemResults = BehaviorSubject.createDefault(Collections.emptyList());

    public MainViewModel(AppCompatActivity appCompatActivity, Context context) {

        this.appCompatActivity = appCompatActivity;
        this.context = context;
        this.repository = QiitaQlientApp.getInstance().getRepository();
        this.itemViewModels = new ObservableArrayList<>();
    }

    public void onClick(View view) {
//        String str = this.itemViewModels.get(0).repo.get().getTitle();
//        Log.v("MainViewModel onClick", str);

//Orma
//        Repo repo = new Repo();
//        repo.title = "test_title";
////        repo.id = "testID";
//        repo.url = "test url";
//
//        OrmaDatabase ormaDatabase = QiitaListRepository.getOrmaDatabase();
//        Inserter<Repo> inserter = ormaDatabase.prepareInsertIntoRepo();
//        inserter.execute(repo);
//        inserter.execute(repo);
//
//        Repo_Selector selector = ormaDatabase.selectFromRepo();

//        for (Repo r : selector) {
//            Log.v("test DBFlow Title", r.getTitle());
//            Log.v("test DBFlow Id", String.valueOf(r.getId()));
//        }







// DBFlow


//        Repo repo = new Repo();
//        repo.title = "test_title4";
////        repo.id = "testID";
//        repo.url = "test url";
//
//        ModelAdapter<Repo> adapter = FlowManager.getModelAdapter(Repo.class);
//        adapter.insert(repo);
//        adapter.insert(repo);
//        adapter.insert(repo);
//
////
//        List<Repo> repos = SQLite.select()
//                .from(Repo.class)
//                .queryList();
//
//        for (Repo r : repos) {
//            Log.v("test DBFlow Title", r.getTitle());
////            Log.v("test DBFlow Id", r.getId());
//        }
//
//        Log.v("test DBFlow Size", String.valueOf(repos.size()));
//
//        repo.title = "test_title2";
//        adapter.update(repo);
//
//
//        List<RepoTable> repos2 = SQLite.select()
//                .from(RepoTable.class)
//                .queryList();
//
//        Log.v("test DBFlow", repos2.get(0).getTitle());



//        Delete.table(RepoTable.class);
//        context.deleteDatabase(GithubDatabase.NAME);

    }

    public View.OnKeyListener setOnKeyListener() {
        return new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode != keyEvent.KEYCODE_ENTER || keyEvent.getAction() != KeyEvent.ACTION_UP) {
                    return false;
                }

                EditText editText = (EditText) view;
                InputMethodManager imm = (InputMethodManager) appCompatActivity.getSystemService(context.INPUT_METHOD_SERVICE);
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

                repository.searchRepo(text)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Repo>>() {

                            @Override
                            public void onNext(List<Repo> result) {
                            Log.v("MainViewModel", "004");

                            List<Repo> repoList = new ArrayList<>();
                            for (Repo r : result) {
                                Repo repo = new Repo();
                                User user = new User();
//                                user.setProfile_image_url(r.getUser().profile_image_url);
//                                repo.setUser(user);
//                                repo.setId(r.id);
                                repo.setTitle(r.title);
                                repo.setUrl(r.url);
                                repoList.add(repo);

                                Log.d("search debug", r.title);
                                Log.d("search debug", r.url);
                                ///  https://qiita.com//api/v2/items/?query=dargon
                            }

                            // todo
                            //StreamはJava8のAPIの方じゃだめ？
                            itemResults.onNext(Stream.of(repoList).map(repo -> new ItemViewModel(new ObservableField<>(repo))).toList());

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.v("MainViewModel", e.getMessage());
                                Log.v("MainViewModel", e.getLocalizedMessage());
                                e.printStackTrace();

                            }

                            @Override
                            public void onSubscribe(Disposable d) {}

                            @Override
                            public void onComplete() {}
                        });


                return true;
            }
        };
    }
}
