package com.ustc.yolk.service;

import com.google.common.collect.Maps;
import com.ustc.yolk.model.User;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Administrator on 2016/11/27.
 */
@Component
public class UserServiceImpl implements UserService {

    private final static Map<String, User> temp = Maps.newConcurrentMap();

    static {
        //初始化一个用户进去
        User user = new User();
        user.setUsername("test");
        user.setPasswdWithEncrypt("test");
        temp.put("test", user);
    }


    @Override
    public User getUser(String username) {
        return temp.get(username);
    }

    @Override
    public void register(User user) {
        temp.put(user.getUsername(), user);
    }
}
