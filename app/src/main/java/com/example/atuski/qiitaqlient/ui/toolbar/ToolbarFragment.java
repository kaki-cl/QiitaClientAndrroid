package com.example.atuski.qiitaqlient.ui.toolbar;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.ViewFragmentPagerAdapter;
import com.example.atuski.qiitaqlient.ui.search.SearchFragment;
import com.example.atuski.qiitaqlient.ui.sub.SubFragment;
import com.example.atuski.qiitaqlient.ui.trend.TrendFragment;

public class ToolbarFragment extends Fragment {

    public ToolbarFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.toolbar_viewpager, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        ViewFragmentPagerAdapter viewPagerAdapter = new ViewFragmentPagerAdapter(activity.getSupportFragmentManager());
        SearchFragment searchFragment = new SearchFragment();
        viewPagerAdapter.addFragments(searchFragment, "Search");
        viewPagerAdapter.addFragments(new TrendFragment(), "Trend");
        viewPagerAdapter.addFragments(new SubFragment(), "Sub");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Log.v("ToolbarFragment", "onCreateView");

        return view;
    }
}
