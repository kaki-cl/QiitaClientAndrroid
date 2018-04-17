package com.example.atuski.qiitaqlient.ui.sub;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.atuski.qiitaqlient.R;

/**
 * Created by atuski on 2018/04/13.
 */
public class SubFragment extends Fragment {
    private final static String BACKGROUND_COLOR = "background_color";

    public SubFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tmp_fragment_away, container, false);
        // Inflate the layout for this fragment
        String[] awayStrings = {
                "gas",
                "cook",
                "Sunset",
                "MidMorning",
                "Good Morning",
                "Breakfast",
                "MidMorning",
                "Lunch",
                "Afternoon",
                "Sunset",
                "Supper Time",
                "Lunch",
                "Afternoon",
                "Supper Time",
                "Lovely Night",
                "Chilly Dreams"
        };

        ListView lv = (ListView) view.findViewById(R.id.listView2);

        ArrayAdapter<String> lva = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, awayStrings);
        lv.setAdapter(lva);

//        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_away);

//        mSwipeRefreshLayout.setOnRefreshListener(
//                new SwipeRefreshLayout.OnRefreshListener() {
//                    @Override
//                    public void onRefresh() {
//                        ((MainActivity) getActivity()).refreshNow();
//                        Toast.makeText(getContext(), "Refresh Layout working", Toast.LENGTH_LONG).show();
//                    }
//                }
//        );

        return view;
    }
}
