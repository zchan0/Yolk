package com.ustc.yolk.utils.web;

import com.google.common.collect.Maps;
import com.ustc.yolk.utils.ReflectUtil;
import com.ustc.yolk.utils.charset.CharsetEnum;
import com.ustc.yolk.utils.common.ParamChecker;
import com.ustc.yolk.utils.invoke.websupport.ResponseWrapper;
import com.ustc.yolk.utils.invoke.websupport.ServletOutputStreamWrapper;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/28.
 */
public class WebUtil {

    /**
     * Gets cookie.
     *
     * @param request the request
     * @param key     the key
     * @return the cookie
     */
    /*获取到指定的cookie*/
    public static Cookie getCookie(HttpServletRequest request, String key) {
        for (Cookie cookie : request.getCookies()) {
            if (StringUtils.equals(key, cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    /*从请求中获取参数*/
    public static <T extends ServletRequest> Map<String, String> getRequestInputs(T t) {
        Map<String, String> params = Maps.newHashMap();
        Enumeration<?> names = t.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement() + StringUtils.EMPTY;
            String value = t.getParameter(name);
            params.put(name, value);
        }
        return params;
    }

    /*使用反射获取httpResponse的返回内容*/
    public static String getResponseContent(ServletResponse response) {
        if (response instanceof ResponseWrapper) {
            ServletOutputStreamWrapper wrapper = ((ResponseWrapper) response).getOutputStreamWrapper();
            return wrapper.getContent();
        }
        return getResponseContentForBIO(response);
    }

    private static String getResponseContentForBIO(ServletResponse response) {
        try {
            OutputStream outputStream = response.getOutputStream();
            Object contentHolder = ReflectUtil.getFiledValue(outputStream, "ob");
            //获取到buffer 然后从buffer中获取到返回值
            Object result = ReflectUtil.getFiledValue(contentHolder, "outputChunk");
            return result == null ? StringUtils.EMPTY : result.toString();
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * Generate url string.
     *
     * @param domain the domain
     * @param params the params
     * @return the string
     */
    public static String generateUrl(String domain, Map<String, String> params) {
        ParamChecker.notBlank("domain", domain);
        if (domain.indexOf("http:") < -1 && domain.indexOf("https:") < -1) {
            throw new RuntimeException("无效的域名");
        }
        int len = (params == null ? 0 : params.size() * 14);
        StringBuilder stringBuilder = new StringBuilder(domain.length() + len);
        stringBuilder.append(domain);
        if (params == null) {
            return stringBuilder.toString();
        }
        stringBuilder.append("?");
        int count = 0;
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuilder.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), CharsetEnum.UTF_8.getCode()));
                if (count != params.size() - 1) {
                    stringBuilder.append("&");
                }
            }
        } catch (Exception e) {
            //ignore
        }
        return stringBuilder.toString();
    }

}
