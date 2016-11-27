package com.ustc.yolk.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 * 采用jdk对象流实现的序列化工具
 *
 * @author Administrator
 * @version $Id: JDKObjectSerializer.java, v 0.1 2016年8月29日 下午3:04:00 Administrator Exp $
 */
public class JDKObjectSerializer implements ObjectSerializer {

    /**
     * @see com.zhangwei.learning.serialize.ObjectSerializer#serialize(java.lang.Object)
     */
    @Override
    public String serialize(Object o) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        // 序列化
        baos = new ByteArrayOutputStream();
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            byte[] bytes = baos.toByteArray();
            return Base64.encodeBase64String(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }

    }

    /**
     * @see com.zhangwei.learning.serialize.ObjectSerializer#deSerialize(java.lang.String, java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T deSerialize(String originString, Class<T> classType) {
        Object object = deSerialize(originString);
        return (T) object;
    }

    /**
     * @see com.zhangwei.learning.serialize.ObjectSerializer#deSerialize(java.lang.String)
     */
    @Override
    public Object deSerialize(String originString) {
        if (StringUtils.isBlank(originString)) {
            return null;
        }
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(Base64.decodeBase64(originString));
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                }
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }
    }

}
