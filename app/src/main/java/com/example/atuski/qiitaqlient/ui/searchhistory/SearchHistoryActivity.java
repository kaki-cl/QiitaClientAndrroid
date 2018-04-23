package com.example.atuski.qiitaqlient.ui.searchhistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.example.atuski.qiitaqlient.MainActivity;
import com.example.atuski.qiitaqlient.R;

import java.util.ArrayList;

public class SearchHistoryActivity extends AppCompatActivity {

    public static final String FROM_SEARCH_HISTORY = "fromSearchHistory";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        ArrayList<String> awayStrings = new ArrayList<>();
        awayStrings.add("gas");
        awayStrings.add("cook");
        awayStrings.add("Sunset");
        awayStrings.add("MidMorning");
        awayStrings.add("Good Morning");
        awayStrings.add("Breakfast");
        awayStrings.add("Lunch");

        RecyclerView view = (RecyclerView) findViewById(R.id.search_history_list);
        view.setLayoutManager(new LinearLayoutManager(this));
        TestRecyclerAdapter adapter = new TestRecyclerAdapter(getApplicationContext(), awayStrings);
        view.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // 戻るボタンがクリックされるとMainActivityに戻る。
        switch (item.getItemId()) {
            case android.R.id.home:
                // Activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(FROM_SEARCH_HISTORY, "Java");
                startActivity(intent);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
