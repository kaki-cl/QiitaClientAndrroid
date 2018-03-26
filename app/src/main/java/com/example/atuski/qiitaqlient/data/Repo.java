package com.example.atuski.qiitaqlient.data;

import lombok.Data;

/**
 * Created by atuski on 2018/03/24.
 */

@Data
public class Repo {

    public String id;
    public String title;
    public String url;
    public User user;

}
