package com.ustc.yolk.utils.invoke;

import com.alibaba.fastjson.JSON;
import com.ustc.yolk.utils.Profiler;
import com.ustc.yolk.utils.ReflectUtil;
import com.ustc.yolk.utils.TraceUtils;
import com.ustc.yolk.utils.log.LoggerUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * Created by Administrator on 2016/11/28.
 */
public class WebInvokeInteceptor implements Filter, InvokeContanst {

    private final static Logger logger = LoggerFactory.getLogger(WebInvokeInteceptor.class);
    //默认跳过静态文件请求
    private static String[] ignoreLists = {".jpg", ".png", ".css", ".js"};


    /*编码*/
    private String charset = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String cset = filterConfig.getInitParameter("charset");
        if (StringUtils.isNoneBlank(cset)) {
            charset = cset;
        }
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
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            afterInvoke(servletResponse, e);
            return;
        }
        afterInvoke(servletResponse, null);
    }


    /*调用结束后的处理(填充上下文并且打印日志)*/
    private void afterInvoke(ServletResponse response, Exception e) {
        WebInvokeContext invokeContext = (WebInvokeContext) InvokeContext.get();
        invokeContext.setException(e);
        invokeContext.setMonitorResource(Profiler.release());
        invokeContext.putOutputParam("webResult", getOutputSteamContentForTomcat(response));
        LoggerUtils.info(logger, invokeContext);
        InvokeContext.set(null);
    }

    /*获取http出参*/
    private String getOutputSteamContentForTomcat(ServletResponse response) {
        try {
            OutputStream outputStream = response.getOutputStream();
            Object contentHolder = ReflectUtil.getFiledValue(outputStream, "ob");
            //获取到buffer 然后从buffer中获取到返回值
            Object result = ReflectUtil.getFiledValue(contentHolder, "outputChunk");
            String resultString = result == null ? StringUtils.EMPTY : result.toString();
            if (StringUtils.startsWith(resultString, "<html>")) {
                //含有html文本
                return "HTML-CONTENT";
            }
            return resultString;
        } catch (Exception e) {
            LoggerUtils.error(logger, e, "获取WEB返回内容异常");
            return StringUtils.EMPTY;
        }
    }

    /*初始化Web调用上下文*/
    private void initContext(HttpServletRequest request) {
        WebInvokeContext webInvokeContext = new WebInvokeContext();
        InvokeContext.set(webInvokeContext);

        webInvokeContext.setRequestUri(request.getRequestURI());
        webInvokeContext.setTraceId(TraceUtils.generateId());
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement() + StringUtils.EMPTY;
            String value = request.getParameter(name);
            webInvokeContext.putInputParam(name, value);
        }
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
