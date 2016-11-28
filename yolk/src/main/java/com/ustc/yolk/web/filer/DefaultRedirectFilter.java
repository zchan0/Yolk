package com.ustc.yolk.web.filer;

import com.ustc.yolk.utils.common.ParamChecker;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2016/11/28.
 */
public class DefaultRedirectFilter implements Filter {
    private String url;
    private String appName;
    private final static String urlKey = "redirectUrl";
    private final static String appNameKey = "appName";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        url = filterConfig.getInitParameter(urlKey);
        appName = filterConfig.getInitParameter(appNameKey);
        ParamChecker.notBlank(urlKey, url);
        ParamChecker.notBlank(appNameKey, appName);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(response instanceof HttpServletResponse) || (!(request instanceof HttpServletRequest))) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        if (servletRequest.getRequestURI().equals("/" + appName) || servletRequest.getRequestURI().equals("/" + appName + "/")) {
            servletResponse.sendRedirect(url);
            return;
        }
        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
