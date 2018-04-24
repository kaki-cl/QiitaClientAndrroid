package com.example.atuski.qiitaqlient.model;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class Stock {

    @Expose
    public String title;

    @Expose
    public String url;

    @Expose
    public String likesCount;

    @Expose
    public User user;
}
