package com.ustc.yolk.model;

/**
 * Created by Administrator on 2016/11/30.
 */
public class UserDO {

    /*用户名*/
    private String username;
    /*加密后的密码*/
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
