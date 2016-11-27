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

    /**
     * Check password boolean.
     *
     * @param passwd the passwd
     * @return the boolean
     */
/*校验密码是否正确*/
    public boolean checkPassword(String passwd) {
        String decryptedPasswd = RSAUtil.decrypt(encryptedPasswd);
        if (StringUtils.equals(decryptedPasswd, passwd)) {
            return true;
        }
        return false;
    }

    /**
     * Sets passwd with encrypt.
     *
     * @param password the password
     */
/*将密码做加密并且set*/
    public void setPasswdWithEncrypt(String password) {
        this.encryptedPasswd = RSAUtil.encrypt(password);
    }

    /**
     * Instantiates a new User.
     *
     * @param username        the username
     * @param encryptedPasswd the encryptedPasswd
     */
    public User(String username, String encryptedPasswd) {
        this.username = username;
        this.encryptedPasswd = encryptedPasswd;
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
