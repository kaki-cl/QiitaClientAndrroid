package com.example.atuski.qiitaqlient.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable {

    @Expose
    public String id;

    @Expose
    public String name;

    @Expose
    public String profile_image_url;

    public User(String id, String name, String profile_image_url) {
        this.id = id;
        this.name = name;
        this.profile_image_url = profile_image_url;
    }
}
