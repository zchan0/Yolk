package com.ustc.yolk.utils.invoke.websupport;

import com.ustc.yolk.utils.charset.CharsetEnum;
import org.apache.commons.io.output.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/12/2.
 */
public class ServletOutputStreamWrapper extends ServletOutputStream {

    private ServletOutputStream servletOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    public ServletOutputStreamWrapper(ServletOutputStream servletOutputStream) {
        this.servletOutputStream = servletOutputStream;
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Override
    public void write(int b) throws IOException {
        servletOutputStream.write(b);
        byteArrayOutputStream.write(b);
    }

    public ServletOutputStream getServletOutputStream() {
        return servletOutputStream;
    }

    public String getContent() {
        try {
            return byteArrayOutputStream.toString(CharsetEnum.UTF_8.getCode());
        } catch (UnsupportedEncodingException e) {
            return "UnsupportedEncodingException";
        }
    }
}
