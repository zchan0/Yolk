/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.ustc.yolk.serialize;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Administrator
 * @version $Id: HessianObjectSerializer.java, v 0.1 2016年9月10日 上午10:32:42 Administrator Exp $
 */
public class HessianObjectSerializer implements ObjectSerializer {

    /**
     * @see com.zhangwei.learning.serialize.ObjectSerializer#serialize(java.lang.Object)
     */
    @Override
    public String serialize(Object o) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(bos);
        try {
            hessian2Output.writeObject(o);
            try {
                hessian2Output.close();
            } catch (IOException e) {
                //ignore
            }
            byte[] bytes = bos.toByteArray();
            return Base64.encodeBase64String(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
        }
    }

    /**
     * @see com.zhangwei.learning.serialize.ObjectSerializer#deSerialize(java.lang.String, java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T deSerialize(String originString, Class<T> classType) {
        byte[] bytes = Base64.decodeBase64(originString);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Hessian2Input input = new Hessian2Input(bis);
        try {
            return (T) input.readObject(classType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                //ignore
            }
        }

    }

    /**
     * @see com.zhangwei.learning.serialize.ObjectSerializer#deSerialize(java.lang.String)
     */
    @Override
    public Object deSerialize(String originString) {
        byte[] bytes = Base64.decodeBase64(originString);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Hessian2Input input = new Hessian2Input(bis);
        try {
            return input.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }
}
