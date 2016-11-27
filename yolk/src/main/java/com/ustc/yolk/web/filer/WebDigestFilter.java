/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.ustc.yolk.web.filer;

import com.ustc.yolk.utils.TraceUtils;
import com.ustc.yolk.utils.log.LoggerUtils;
import org.apache.log4j.NDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * 拦截web请求的filter 请求的参数以及耗时拦截
 *
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
        NDC.push(TraceUtils.generateId());
        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            NDC.pop();
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
        LoggerUtils.info(LOGGER, "webDigest-", "[", uri, "][", params, "]", "[cost=",
                (System.currentTimeMillis() - start), "ms]");
        NDC.pop();
    }

    private String getInputParams(HttpServletRequest servletRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        if (servletRequest.getParameterMap() == null) {
            return stringBuilder.toString();
        }
        for (Map.Entry<String, String[]> entry : servletRequest.getParameterMap().entrySet()) {
            String name = entry.getKey();
            String value = servletRequest.getParameter(name);
            if (value != null && value.length() > 1024) {
                value = "value is too long";
            }
            stringBuilder.append(name).append("->").append(value).append(",");
        }
        if (stringBuilder.length() == 0) {
            return stringBuilder.toString();
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
