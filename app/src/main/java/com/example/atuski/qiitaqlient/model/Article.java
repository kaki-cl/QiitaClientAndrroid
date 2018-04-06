package com.example.atuski.qiitaqlient.model;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;
import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
@Table
public class Article {

    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    @Expose
    public String title;

    @Column
    @Expose
    public String url;

    @Column(indexed = true)
    public long queryId;

    @Column
    @Expose
    public User user;

    public Article() {

    }
}
