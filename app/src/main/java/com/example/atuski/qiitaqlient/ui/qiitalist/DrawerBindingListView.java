package com.example.atuski.qiitaqlient.ui.qiitalist;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.atuski.qiitaqlient.R;

import java.util.ArrayList;

public class DrawerBindingListView extends ListView {

    private ArrayAdapter<String> adapter;

    public DrawerBindingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setList(ArrayList list) {
        if (adapter == null) {
            adapter = new ArrayAdapter<String>(
                    getContext(),
                    R.layout.drawer_list_item,
                    list);
            setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }
}
