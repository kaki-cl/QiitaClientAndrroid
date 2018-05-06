package com.example.atuski.qiitaqlient.ui.toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.ViewFragmentPagerAdapter;
import com.example.atuski.qiitaqlient.ui.search.SearchFragment;
import com.example.atuski.qiitaqlient.ui.stock.StockFragment;
import com.example.atuski.qiitaqlient.ui.sub.SubFragment;
import com.example.atuski.qiitaqlient.ui.trend.TrendFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ToolbarFragment extends Fragment {

    public ToolbarFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.v("ToolbarFragment", "onCreateView");

        final AppCompatActivity activity = (AppCompatActivity) getActivity();

        // toolbar_viewpager
        View view = inflater.inflate(R.layout.toolbar_viewpager, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        activity.setSupportActionBar(toolbar);

        // ログイン済みならユーザープロファイルViewを表示
        setLoginUserProfileView(activity, inflater);

        // ViewFragmentPagerAdapter
        ViewFragmentPagerAdapter viewPagerAdapter = new ViewFragmentPagerAdapter(getChildFragmentManager());

        // SearchFragment
        Fragment searchFragment = new SearchFragment();
        Bundle bundle = getArguments();
        searchFragment.setArguments(bundle);
        viewPagerAdapter.addFragments(searchFragment, "Search");

        // TrendFragment or StockFragment いずれこの実装はやめる。
        String fragmentTag;
        if (bundle.getBoolean(getResources().getString(R.string.IS_LOGIN))) {
            fragmentTag = "loginUser";
            bundle.putString(getResources().getString(R.string.USER_ID), getArguments().getString("USER_ID"));
            StockFragment stockFragment = new StockFragment();
            stockFragment.setArguments(bundle);
            viewPagerAdapter.addFragments(stockFragment, fragmentTag);

        } else {
            fragmentTag = "Guest";
            viewPagerAdapter.addFragments(new TrendFragment(), fragmentTag);
        }

        // SubFragment
        viewPagerAdapter.addFragments(new SubFragment(), "Sub");

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setLoginUserProfileView(AppCompatActivity activity, LayoutInflater inflater) {

        View customActionBarView = inflater.inflate(R.layout.login_user_profile, null);


        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setCustomView(customActionBarView);
        actionBar.setDisplayShowCustomEnabled(true);

        ImageView myImageView = (ImageView) customActionBarView.findViewById(R.id.action_bar_icon);
        Picasso.get()
                .load("https://s3-ap-northeast-1.amazonaws.com/qiita-image-store/0/91182/b204cfd64e4a07b45e7042724a9d0965deab0132/medium.png?1516523902")
                .into(myImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.v("Callback", "onSuccess");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.v("Callback", "onError");
                        e.printStackTrace();
                    }
                });
    }
}
