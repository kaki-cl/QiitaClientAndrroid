package com.example.atuski.qiitaqlient.ui.util.helper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    int firstVisibleItem, visibleItemCount, totalItemCount, lastVisibleItem;
    private int previousTotal = 0;
    private boolean loading = true;
    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        Log.v("EndlessScrollListener", "onScrolled");

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

        Log.v("EndlessScrollListener loading", String.valueOf(loading));
        Log.v("EndlessScrollListener previousTotal", String.valueOf(previousTotal));
        Log.v("EndlessScrollListener totalItemCount", String.valueOf(totalItemCount));
        Log.v("EndlessScrollListener visibleItemCount", String.valueOf(visibleItemCount));
        Log.v("EndlessScrollListener firstVisibleItem", String.valueOf(firstVisibleItem));
        Log.v("EndlessScrollListener lastVisibleItem", String.valueOf(lastVisibleItem));

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        //11
        if (!loading && (totalItemCount - 1) == lastVisibleItem) {
            current_page++;

            onLoadMore(current_page);

            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}