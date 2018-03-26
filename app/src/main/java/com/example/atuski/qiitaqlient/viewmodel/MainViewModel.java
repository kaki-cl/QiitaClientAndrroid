package com.example.atuski.qiitaqlient.viewmodel;

/**
 * Created by atuski on 2018/03/26.
 */


import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import java.util.List;


/**
 * todo
 *
 * クリックイベントをこっちに引っ越し
 *
 *
 */
public class MainViewModel {

//    public ObservableList<ItemViewModel> itemViewModels;
    public ObservableList<ItemViewModel> itemViewModels;

    public MainViewModel() {
        this.itemViewModels = new ObservableArrayList<>();
    }

    public void onResume() {

    }


    public void onClick(View view) {
        String str = this.itemViewModels.get(0).repo.get().getTitle();
        Log.v("MainViewModel onClick", str);

    }

}
