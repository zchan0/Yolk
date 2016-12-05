package com.ustc.yolk.utils.invoke;

import com.alibaba.fastjson.JSON;
import com.ustc.yolk.utils.Profiler;
import com.ustc.yolk.utils.TraceUtils;
import com.ustc.yolk.utils.invoke.websupport.ResponseWrapper;
import com.ustc.yolk.utils.log.LoggerUtils;
import com.ustc.yolk.utils.web.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于做web请求拦截的拦截器
 * Created by Administrator on 2016/11/28.
 */
public class WebInvokeInteceptor implements Filter, InvokeContanst {

    private final static Logger logger = LoggerFactory.getLogger(WebInvokeInteceptor.class);
    //默认跳过静态文件请求
    private static String[] ignoreLists = {".jpg", ".png", ".css", ".js", ".html", ".gif"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String[] ss = StringUtils.split(filterConfig.getInitParameter("ignoreLists"), ",");
        if (ss != null) {
            LoggerUtils.info(logger, "设置非拦截请求", JSON.toJSONString(ss));
            ignoreLists = ss;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if ((!(servletRequest instanceof HttpServletRequest)) || (!(servletResponse instanceof HttpServletResponse))) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        //如果是静态文件请求 ignore
        if (isIgnore(((HttpServletRequest) servletRequest).getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        initContext((HttpServletRequest) servletRequest);
        //put trace id
        MDC.put(TraceUtils.TRACE_ID, InvokeContext.get().getTraceId());
        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) servletResponse);
        try {
            filterChain.doFilter(servletRequest, responseWrapper);
        } catch (Exception e) {
            afterInvoke(responseWrapper, e);
            throw e;
        }
        afterInvoke(responseWrapper, null);
    }


    /*调用结束后的处理(填充上下文并且打印日志)*/
    private void afterInvoke(ServletResponse response, Exception e) {
        WebInvokeContext invokeContext = (WebInvokeContext) InvokeContext.get();
        try {
            invokeContext.setException(e);
            invokeContext.setMonitorResource(Profiler.release());
            invokeContext.setContentType(response.getContentType());
            String output = WebUtil.getResponseContent(response);
            if (StringUtils.startsWith(output, "<html>")) {
                //含有html文本
                output = "HTML-CONTENT";
            }
            invokeContext.putOutputParam("webResult", output);
            invokeContext.setStatus(0);
        } catch (Exception exp) {
            //ignore
        } finally {
            LoggerUtils.info(logger, invokeContext);
            InvokeContext.set(null);
        }
    }

    /*初始化Web调用上下文*/
    private void initContext(HttpServletRequest request) {
        WebInvokeContext webInvokeContext = new WebInvokeContext();
        InvokeContext.set(webInvokeContext);

        webInvokeContext.setRequestUri(request.getRequestURI());
        webInvokeContext.setTraceId(TraceUtils.generateId());
        webInvokeContext.putAllInputs(WebUtil.getRequestInputs(request));
        Profiler.enter("WebInvoke");
    }

    /*是否是需要跳过的请求*/
    private boolean isIgnore(String url) {
        if (ignoreLists == null) {
            return false;
        }
        for (String endFix : ignoreLists) {
            if (StringUtils.endsWith(url, endFix)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {

    }
}
