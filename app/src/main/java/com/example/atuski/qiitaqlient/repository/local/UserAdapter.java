package com.example.atuski.qiitaqlient.repository.local;

import android.util.Log;

import com.example.atuski.qiitaqlient.model.User;
import com.github.gfx.android.orma.annotation.StaticTypeAdapter;

/**
 * Created by atuski on 2018/03/31.
 */

@StaticTypeAdapter(
        targetType = User.class,
        serializedType = String.class
)
public class UserAdapter {

    // SerializedType serialize(TargetType source)
    public static String serialize(User source) {

        Log.v("UserAdapter", "serialize");


        return source.getName() + "," + source.getProfile_image_url();
    }

    // TargetType deserialize(SerializedType serialized)
    public static User deserialize(String serialized) {

        Log.v("UserAdapter", "deserialize");


        String[] values = serialized.split(",");
        User user = new User(values[0], values[1]);
        Log.v("UserAdapter", user.getName());
        Log.v("UserAdapter", user.getProfile_image_url());

        return new User(values[0], values[1]);
    }
}
