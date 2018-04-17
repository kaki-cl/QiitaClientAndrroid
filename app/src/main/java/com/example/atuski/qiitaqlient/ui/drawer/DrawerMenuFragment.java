package com.example.atuski.qiitaqlient.ui.drawer;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.databinding.DrawerMenuFragmentBinding;

public class DrawerMenuFragment extends Fragment {

    private ActionBarDrawerToggle mDrawerToggle;

    public DrawerMenuFragment() {}

    private DrawerMenuFragmentBinding binding;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        View view = inflater.inflate(R.layout.drawer_menu_fragment, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.drawer_menu_fragment, container, false);

        // ドロワーの開閉イベント
//        Toolbar toolbar = (Toolbar) container.findViewById(R.id.appBar);
//        mDrawerToggle = new ActionbarDrawerToggleLister(
//                this.activity,          /* host Activity */
//                binding.drawerLayout,  /* DrawerLayout object */
//                toolbar,               /* nav drawer image to replace 'Up' caret */
//                R.string.drawer_open,  /* "open drawer" description for accessibility */
//                R.string.drawer_close  /* "close drawer" description for accessibility */
//        );
//        binding.drawerLayout.setDrawerListener(mDrawerToggle);

        // ドロワーのクリックイベント
//        binding.searchHistoryDrawer.setOnItemClickListener(
//                (AdapterView<?> parent, View view, int position, long id) -> {
//
//                    TextView textView = (TextView) view;
//                    mainViewModel.onClickedDrawerItem(binding, textView.getText().toString());
//                });

        return super.onCreateView(inflater, container, savedInstanceState);

    }


}
