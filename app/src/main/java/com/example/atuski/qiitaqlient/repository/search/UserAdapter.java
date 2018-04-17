package com.example.atuski.qiitaqlient.repository.search;

import com.example.atuski.qiitaqlient.model.User;
import com.github.gfx.android.orma.annotation.StaticTypeAdapter;

@StaticTypeAdapter(
        targetType = User.class,
        serializedType = String.class
)
public class UserAdapter {

    public static String serialize(User source) {
        return source.getName() + "," + source.getProfile_image_url();
    }

    public static User deserialize(String serialized) {

        String[] values = serialized.split(",");
        User user = new User(values[0], values[1]);
        return new User(values[0], values[1]);
    }
}
