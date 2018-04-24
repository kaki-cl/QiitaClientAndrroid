package com.example.atuski.qiitaqlient.model;

import com.google.gson.annotations.Expose;

import lombok.Data;

/**
 * Created by atuski on 2018/04/24.
 */

@Data
public class Token {

    @Expose
    public String token;

//いらない
//    @Expose
//    public List<String> scopes;
}
