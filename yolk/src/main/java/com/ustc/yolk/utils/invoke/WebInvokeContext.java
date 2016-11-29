package com.ustc.yolk.utils.invoke;

/**
 * Created by Administrator on 2016/11/28.
 */
public class WebInvokeContext extends InvokeContext<WebInvokeContext> {
    /*请求的url*/
    private String requestUri;
    /*返回的内容长度*/
    private int contentLenth;

    @Override
    public String toString() {
        //格式为 super+[uri]
        String superString = super.toString();
        StringBuilder stringBuilder = new StringBuilder(superString.length() * 2);
        stringBuilder.append(superString);
        stringBuilder.append(LEFT_SQUARE).append(requestUri).append(RIGHT_SQUARE);
        return stringBuilder.toString();
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
}
