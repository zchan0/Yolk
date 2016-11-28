/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.ustc.yolk.web;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.ustc.yolk.model.Constants;
import com.ustc.yolk.model.User;
import com.ustc.yolk.serialize.DefaultObjectSerializer;
import com.ustc.yolk.serialize.ObjectSerializer;
import com.ustc.yolk.serialize.SerializeFactory;
import com.ustc.yolk.utils.common.ParamChecker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * controller公用方法
 *
 * @author Administrator
 * @version $Id: BaseController.java, v 0.1 2016年11月27日 下午12:55:12 Administrator Exp $
 */
public class BaseController implements Constants {

    private final static String USER_SESSION_KEY = "userSessionId";
    private final static ObjectSerializer SERIALIZER = SerializeFactory
            .getSerializer(DefaultObjectSerializer.class);

    /**
     * 构建json格式的返回结果
     *
     * @param success 是否成功
     * @param msg     错误信息 success=true时为null
     * @param ext     额外的信息 这里是key-value形式, 奇数为key 偶数为value
     * @return 返回给前端的JSON字符串
     */
    protected static String wrapResult(boolean success, String msg, Object... ext) {
        Map<String, String> result = Maps.newHashMap();
        result.put(SUCCESS, String.valueOf(success));
        result.put(errorMsg, msg);
        String key = null;
        for (int i = 1; i <= ext.length; i++) {
            if (i % 2 == 1) {
                key = JSON.toJSONString(ext[i]);
            } else {
                result.put(key, JSON.toJSONString(ext[i]));
            }
        }
        return SERIALIZER.serialize(JSON.toJSONString(result));
    }

    protected static String wrapSuccessResult(Object... ext) {
        return wrapResult(true, null, ext);
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

}
