package com.example.atuski.qiitaqlient.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable {

    @Expose
    public String name;

    @Expose
    public String profile_image_url;

    public User(String name, String profile_image_url) {
        this.name = name;
        this.profile_image_url = profile_image_url;
    }
}
