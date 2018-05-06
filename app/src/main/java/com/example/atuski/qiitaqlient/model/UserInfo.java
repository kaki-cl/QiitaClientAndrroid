package com.example.atuski.qiitaqlient.model;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class UserInfo {

    @Expose
    public String id;

    @Expose
    public String name;

    public boolean isLogin;

    @Expose
    public String profile_image_url;

    @Expose
    public String items_count;
}
