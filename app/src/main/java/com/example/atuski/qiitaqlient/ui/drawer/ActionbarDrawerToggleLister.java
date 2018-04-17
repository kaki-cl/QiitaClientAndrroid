package com.example.atuski.qiitaqlient.ui.drawer;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by atuski on 2018/04/06.
 */

public class ActionbarDrawerToggleLister extends ActionBarDrawerToggle {

    private final Activity activity;

    public ActionbarDrawerToggleLister(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);

        this.activity = activity;
    }

    public void onDrawerClosed(View view) {

        activity.invalidateOptionsMenu();
    }

    public void onDrawerOpened(View drawerView) {

        activity.invalidateOptionsMenu();
    }

}
