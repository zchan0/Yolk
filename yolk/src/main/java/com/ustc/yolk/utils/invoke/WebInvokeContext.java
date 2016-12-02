package com.ustc.yolk.utils.invoke;

import java.util.Map;

/**
 * web请求的调用上下文
 * Created by Administrator on 2016/11/28.
 */
public class WebInvokeContext extends InvokeContext<WebInvokeContext> {
    /*请求的url*/
    private String requestUri;
    /*返回的内容类型*/
    private String contentType;
    /*返回的内容长度*/
    private int contentLenth;
    private Map<String, String> cookies;
    /*http请求状态码*/
    private int status;

    @Override
    public String toString() {
        //格式为 super+[uri]
        String superString = super.toString();
        StringBuilder stringBuilder = new StringBuilder(superString.length() * 2);
        stringBuilder.append(superString);
        stringBuilder.append(LEFT_SQUARE).append(requestUri).append(RIGHT_SQUARE);
        stringBuilder.append(LEFT_SQUARE).append(contentType).append(RIGHT_SQUARE);
        stringBuilder.append(LEFT_SQUARE).append(status).append(RIGHT_SQUARE);
        stringBuilder.append(LEFT_SQUARE).append(mapToString(cookies)).append(RIGHT_SQUARE);
        return stringBuilder.toString();
    }

    @Override
    protected String invokeType() {
        return "WEB-MVC";
    }

    /**
     * Gets content lenth.
     */
    public int getContentLenth() {
        return contentLenth;
    }

    /**
     * Sets content lenth.
     */
    public void setContentLenth(int contentLenth) {
        this.contentLenth = contentLenth;
    }

    /**
     * Gets request url.
     */
    public String getRequestUri() {
        return requestUri;
    }

    /**
     * Sets request url.
     */
    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
