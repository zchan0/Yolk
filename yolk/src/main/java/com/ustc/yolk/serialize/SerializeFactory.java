package com.ustc.yolk.serialize;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/16.
 */
public class SerializeFactory {

    private final static Map<String, ObjectSerializer> serializeFactory = Maps.newHashMap();

    static {
        serializeFactory.put(JDKObjectSerializer.class.getSimpleName(), new JDKObjectSerializer());
        serializeFactory.put(DefaultObjectSerializer.class.getSimpleName(),
                new DefaultObjectSerializer());
        serializeFactory.put(HessianObjectSerializer.class.getSimpleName(),
                new HessianObjectSerializer());
        serializeFactory.put(ReflectStringObjectSerializer.class.getSimpleName(),
                new ReflectStringObjectSerializer());
    }

    public final static ObjectSerializer getDefaultSerializer() {
        return serializeFactory.get(DefaultObjectSerializer.class.getSimpleName());
    }

    public final static ObjectSerializer getSerializer(Class<?> clazz) {
        return serializeFactory.get(clazz.getSimpleName());
    }

    public String serialize(Object o, Class<?> clazz) {
        return getSerializer(clazz).serialize(o);
    }

    public <T> T deSerialize(String originString, Class<T> classType, Class<?> serClazz) {
        return getSerializer(serClazz).deSerialize(originString, classType);
    }

    public Object deSerialize(String originString, Class<?> clazz) {
        return getSerializer(clazz).deSerialize(originString);
    }

}
