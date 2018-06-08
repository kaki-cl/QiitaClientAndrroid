package com.example.atuski.qiitaqlient.repository.stock;

import com.example.atuski.qiitaqlient.model.Stock;
import com.example.atuski.qiitaqlient.api.QiitaClient;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by atuski on 2018/04/24.
 */

public class StockRepository {

    private static StockRepository sInstance;

    private QiitaClient qiitaClient;

    private StockRepository() {

        qiitaClient = QiitaClient.getInstance();
    }

    public static StockRepository getInstance() {
        if (sInstance == null) {
            sInstance = new StockRepository();
        }
        return sInstance;
    }

    public Observable<List<Stock>> searchStockItems(String userId) {

        return qiitaClient.qiitaService.getStockItems(userId, 1, 20)
                .map((stockList) -> {

//                    for (Article r : articleSearchResult) {
//                        r.setQueryId(queryId);
//                    }
                    // 検索結果を保存
//                    localDataSource.insertArticles(articleSearchResult);
                    return stockList;
                });
    }
}
