package com.ustc.yolk.web.filer;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2016/11/28.
 */
public class DefaultRedirectFilter implements Filter {
    private String url;
    private final static String urlKey = "redirectUrl";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String url = filterConfig.getInitParameter(urlKey);
        this.url = url;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(response instanceof HttpServletResponse) || (!(request instanceof HttpServletRequest))) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        if (servletRequest.getRequestURI().equals("/")) {
            servletResponse.sendRedirect(url);
            return;
        }
        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
