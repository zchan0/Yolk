package com.yolk.serialize;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 
 * @author Administrator
 * @version $Id: ReflectStringObjectSerializer.java, v 0.1 2016年9月1日 上午11:27:49 Administrator Exp $
 */
public class ReflectStringObjectSerializer implements ObjectSerializer {

    /** 
     * @see com.zhangwei.learning.serialize.ObjectSerializer#serialize(java.lang.Object)
     */
    @Override
    public String serialize(Object o) {
        return ReflectionToStringBuilder.toString(o);
    }

    /** 
     * @see com.zhangwei.learning.serialize.ObjectSerializer#deSerialize(java.lang.String, java.lang.Class)
     */
    @Override
    public <T> T deSerialize(String originString, Class<T> classType) {
        throw new RuntimeException("不支持反序列化");
    }

    /** 
     * @see com.zhangwei.learning.serialize.ObjectSerializer#deSerialize(java.lang.String)
     */
    @Override
    public Object deSerialize(String originString) {
        throw new RuntimeException("不支持反序列化");
    }

}
