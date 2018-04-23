package com.example.atuski.qiitaqlient.ui.searchhistory;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.atuski.qiitaqlient.MainActivity;
import com.example.atuski.qiitaqlient.R;

import java.util.ArrayList;

/**
 * Created by atuski on 2018/04/20.
 */

public class SearchHistoryFragment extends Fragment {

    private Context context;
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private AppCompatActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.search_history, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.searchHIstoryToolbar);

//        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        setHasOptionsMenu(true);
        //todo ActionBarとToolbarの違いについて

        recyclerView = (RecyclerView) view.findViewById(R.id.search_history_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 適当にデータ作成
        ArrayList<String> array = new ArrayList<>();
        array.add("A");
        array.add("B");
        array.add("C");

        // この辺りはListViewと同じ
        // 今回は特に何もしないけど、一応クリック判定を取れる様にする
        adapter = new TestRecyclerAdapter(context, array);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.v("SearchHistoryFragment", "onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                startActivity(intent);
//                getFragmentManager().popBackStack(); //これだと実際の反応がいまいち。
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
