/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.ustc.yolk.web;

import com.ustc.yolk.model.Constants;
import com.ustc.yolk.model.User;
import com.ustc.yolk.serialize.DefaultObjectSerializer;
import com.ustc.yolk.serialize.ObjectSerializer;
import com.ustc.yolk.serialize.SerializeFactory;
import com.ustc.yolk.utils.common.BaseResult;
import com.ustc.yolk.utils.common.ParamChecker;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

/**
 * controller公用方法
 *
 * @author Administrator
 * @version $Id: BaseController.java, v 0.1 2016年11月27日 下午12:55:12 Administrator Exp $
 */
public class BaseController implements Constants {

    private final static String FILE_PATH = "/root/yolkfiles/";
    private final static String USER_SESSION_KEY = "userSessionId";
    private final static ObjectSerializer SERIALIZER = SerializeFactory
            .getSerializer(DefaultObjectSerializer.class);

    /**
     * 构建json格式的返回结果
     *
     * @param success 是否成功
     * @param msg     错误信息 success=true时为null
     * @return 返回给前端的JSON字符串
     */
    protected String wrapResult(boolean success, String msg) {
        return SERIALIZER.serialize(new BaseResult(success, null, msg));
    }

    /**
     * 从session中获取当前用户 返回为空则认为没有登录
     */
    protected User getUserFromRequest(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession();
        ParamChecker.notNull(session, SYSTEM_ERROR);
        Object userObject = session.getAttribute(USER_SESSION_KEY);
        if (userObject == null) {
            throw new RuntimeException("please login first!");
        }
        ParamChecker.assertCondition(userObject instanceof User, SYSTEM_ERROR);
        return (User) userObject;
    }

    /**
     * 将用户保存到session
     */
    protected void login(User user, HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession();
        ParamChecker.notNull(session, SYSTEM_ERROR);
        session.setAttribute(USER_SESSION_KEY, user);
    }

    /**
     * 用户登出  将用户从session中移除
     */
    protected void logout(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession();
        ParamChecker.notNull(session, SYSTEM_ERROR);
        session.removeAttribute(USER_SESSION_KEY);
    }

    protected void writeFile(MultipartFile multipartFile, String username)
            throws IllegalStateException,
            IOException {
        if (multipartFile != null) {
            createFolder(username);
            //取得当前上传文件的文件名称  
            String myFileName = multipartFile.getOriginalFilename();
            //如果名称不为“”,说明该文件存在，否则说明该文件不存在
            if (myFileName.trim() != "") {
                String filePath = FILE_PATH + username + "/" + multipartFile.getOriginalFilename();
                File localFile = new File(filePath);
                multipartFile.transferTo(localFile);
            }
        }
    }

    /**
     * 创建文件夹 文件夹名称为当前日期
     */
    private void createFolder(String username) {
        File file = new File(FILE_PATH + username);
        if (file.exists() && file.isDirectory()) {
            return;
        }
        if (file.mkdir()) {
            return;
        }
        throw new RuntimeException("create folder error!");
    }
}
