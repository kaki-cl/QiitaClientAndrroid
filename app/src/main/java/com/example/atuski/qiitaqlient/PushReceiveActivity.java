package com.example.atuski.qiitaqlient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class PushReceiveActivity extends AppCompatActivity {

    public static final String ARG_TYPE = "type";
    public static final String ARG_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id = getIntent().getStringExtra(ARG_ID);
        String type = getIntent().getStringExtra(ARG_TYPE);

        Log.v("PushReceiveActiviy", id);
        Log.v("PushReceiveActiviy", type);

        // 表示したい画面へデータを渡して画面遷移
    }

}
