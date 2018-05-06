package com.example.atuski.qiitaqlient.ui.util.helper;

import android.content.Context;
import android.support.annotation.StringRes;

/**
 * Created by atuski on 2018/05/04.
 */

public class ResourceResolver {

    private static ResourceResolver sInstance = null;

    private final Context context;

    public ResourceResolver(Context context) {
        this.context = context;
    }

    public static ResourceResolver getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new ResourceResolver(context);
        }
        return sInstance;
    }

    public String getString(@StringRes int resId) {
        return context.getString(resId);
    }

}
