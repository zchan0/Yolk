/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.ustc.yolk.web;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ustc.yolk.utils.log.LoggerUtils;

/**
 * 拦截web请求的filter 请求的参数以及耗时拦截
 * @author Administrator
 * @version $Id: WebDigestFilter.java, v 0.1 2016年11月27日 下午4:27:45 Administrator Exp $
 */
public class WebDigestFilter implements Filter {

    private final static Logger LOGGER = LoggerFactory.getLogger(WebDigestFilter.class);

    /** 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /** 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                                                                                             throws IOException,
                                                                                             ServletException {

        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String uri = servletRequest.getRequestURI();
        long start = System.currentTimeMillis();
        String inputParams = getInputParams(servletRequest);
        try {
            chain.doFilter(servletRequest, response);
        } catch (Exception e) {
            log(uri, inputParams, start);
            throw e;
        }
        log(uri, inputParams, start);
    }

    /**
     * 做日志 格式为[请求地址][入参][请求处理耗时]
     */
    private void log(String uri, String params, long start) {
        LoggerUtils.info(LOGGER, "[", uri, "][", params, "]", "[cost=",
            (System.currentTimeMillis() - start), "ms]");
    }

    private String getInputParams(HttpServletRequest servletRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        Enumeration<String> names = servletRequest.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = servletRequest.getParameter(name);
            if (value != null && value.length() > 1024) {
                value = "value is too long";
            }
            stringBuilder.append(name).append("->").append(value).append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    /** 
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
    }

}
