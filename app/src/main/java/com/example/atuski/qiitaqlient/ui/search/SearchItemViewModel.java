package com.example.atuski.qiitaqlient.ui.search;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.atuski.qiitaqlient.QiitaQlientApp;
import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.model.Article;
import com.example.atuski.qiitaqlient.ui.detail.DetailActivity;

public class SearchItemViewModel {

    public ObservableField<Article> article;

    private final LayoutInflater inflater;

    private final Context context;

    public SearchItemViewModel(ObservableField<Article> article, Context context) {
        this.article = article;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

    public ObservableField<Article> getArticle() {
        return article;
    }

    public void setArticle(ObservableField<Article> article) {
        this.article = article;
    }

    public void onClick(View view) {

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(QiitaQlientApp
                .getInstance()
                .getResourceResolver()
                .getString(R.string.WEB_VIEW_URL), article.get().url);
        context.startActivity(intent);
    }

    //todo メソッド参照とリスナーバインディングの違いを調査
    public Boolean onLongClick(View view) {

        PopupWindow mPopupWindow = new PopupWindow(context);
        View popupView = inflater.inflate(R.layout.search_item_popup, null);

        popupView.findViewById(R.id.stock_button).setOnClickListener(v -> {

            QiitaQlientApp.getInstance().getStockRepository()
                    .stockArticle(article.get().id)
                    .subscribe(() -> {
                        Toast.makeText(context, "記事をストックしました。", Toast.LENGTH_SHORT).show();
                    }, exception -> {
                        exception.printStackTrace();
                    });
        });

        popupView.findViewById(R.id.follow_button).setOnClickListener(v -> {

            QiitaQlientApp.getInstance().getFollowRepository()
                    .followPostUser(article.get().user.id)
                    .subscribe(() -> {
                        Toast.makeText(context, "ユーザーをフォローしました。", Toast.LENGTH_SHORT)
                                .show();
                    }, exception -> {
                        exception.printStackTrace();
                    });
        });

        popupView.findViewById(R.id.close_button).setOnClickListener(v -> {
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.setContentView(popupView);
        popupView.findViewById(R.id.stock_button).setVisibility(View.VISIBLE);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        return true;
    }
}

