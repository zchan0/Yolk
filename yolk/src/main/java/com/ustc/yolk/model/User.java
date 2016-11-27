/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.ustc.yolk.model;

import com.ustc.yolk.utils.RSAUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * The type User.
 *
 * @author Administrator
 * @version $Id : User.java, v 0.1 2016年11月27日 下午4:49:45 Administrator Exp $
 */
public class User {

    /*用户名*/
    private String username;
    /*密码 这里是加密过后的密码*/
    private String password;

    /*校验密码是否正确*/
    public boolean checkPassword(String passwd) {
        String decryptedPasswd = RSAUtil.decrypt(password);
        if (StringUtils.equals(decryptedPasswd, passwd)) {
            return true;
        }
        return false;
    }

    /*将密码做加密并且set*/
    public void setPasswdWithEncrypt(String password) {
        this.password = RSAUtil.encrypt(password);
    }

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param password the password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
