package com.ustc.yolk.web;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016/11/28.
 */
public class DefaultRedirectController extends BaseController {

    @RequestMapping(value = "/")
    public String addPic(HttpServletRequest req) {
        return "index.html";
    }
}
