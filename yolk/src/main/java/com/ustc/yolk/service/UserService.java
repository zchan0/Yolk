package com.ustc.yolk.service;

import com.ustc.yolk.model.User;

/**
 * Created by Administrator on 2016/11/27.
 */
public interface UserService {

    /*根据username查询用户*/
    User getUser(String username);

    /*注册用户*/
    void register(User user);
}
