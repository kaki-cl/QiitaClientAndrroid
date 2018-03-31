package com.example.atuski.qiitaqlient.model;


import android.support.annotation.Nullable;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.StaticTypeAdapter;
import com.github.gfx.android.orma.annotation.Table;
import com.google.gson.annotations.Expose;

import lombok.Data;

//@Data
//@Table(database = GithubDatabase.class, name = "repo")
//public class Repo extends BaseModel {
//
//    @Column
//    @PrimaryKey(autoincrement = true)
//    public int id;
//
////    @Column
////    public String id;
//
//    @Column
//    public String title;
//
//    @Column
//    public String url;
//
//    public User user;
//
//}

@Data
@Table
public class Repo {

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

    public Repo() {

    }
}
