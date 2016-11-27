package com.yolk.serialize;

/**
 * 对象序列化工具,实现了对象的序列化转换
 * Created by Administrator on 2016/8/23 0023.
 */
public interface ObjectSerializer {

    /**
     * Serialize string.
     *
     * @param o the o
     * @return the string
     */
    String serialize(Object o);

    /**
     * Deserialize t.
     *
     * @param <T>          the type parameter
     * @param originString the origin string
     * @param classType    the class type
     * @return the t
     */
    <T> T deSerialize(String originString, Class<T> classType);

    /**
     * Deserialize object.
     *
     * @param originString the origin string
     * @return the object
     */
    Object deSerialize(String originString);
}
