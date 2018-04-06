package com.example.atuski.qiitaqlient.model;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import lombok.Data;

@Data
@Table
public class Query {

    @PrimaryKey(autoincrement = true)
    public int id;

    @Column(unique = true, indexed = true)
    public String query;

    public Query() {}
}
