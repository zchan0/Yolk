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
    private String encryptedPasswd;

    /*校验密码是否正确*/
    public boolean checkPassword(String passwd) {
        String decryptedPasswd = RSAUtil.decrypt(encryptedPasswd);
        if (StringUtils.equals(decryptedPasswd, passwd)) {
            return true;
        }
        return false;
    }

    /*将密码做加密并且set*/
    public void encryptPasswd(String password) {
        this.encryptedPasswd = RSAUtil.encrypt(password);
    }

    /**
     * Instantiates a new User.
     *
     * @param username the username
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * Instantiates a new User.
     */
    public User() {
    }

    /**
     * Sets encrypted passwd.
     *
     * @param encryptedPasswd the encrypted passwd
     */
    public void setEncryptedPasswd(String encryptedPasswd) {
        this.encryptedPasswd = encryptedPasswd;
    }

    /**
     * Gets encrypted passwd.
     *
     * @return the encrypted passwd
     */
    public String getEncryptedPasswd() {
        return encryptedPasswd;
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
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
