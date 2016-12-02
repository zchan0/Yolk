package com.ustc.yolk.utils.invoke.websupport;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * Created by Administrator on 2016/11/30.
 */
public class ResponseWrapper extends HttpServletResponseWrapper {

    private ServletOutputStreamWrapper servletOutputStreamWrapper;

    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response
     * @throws IllegalArgumentException if the response is null
     */
    public ResponseWrapper(HttpServletResponse response) {
        super(response);
        try {
            servletOutputStreamWrapper = new ServletOutputStreamWrapper(response.getOutputStream());
        } catch (IOException e) {
            //ignore
        }
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return servletOutputStreamWrapper;
    }

    public ServletOutputStreamWrapper getOutputStreamWrapper() {
        return servletOutputStreamWrapper;
    }

}
