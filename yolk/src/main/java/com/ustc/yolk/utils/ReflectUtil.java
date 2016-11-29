package com.ustc.yolk.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/11/29.
 */
public class ReflectUtil {

    /*获取到指定的field*/
    public static Field getFiled(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (StringUtils.equals(fieldName, field.getName())) {
                return field;
            }
        }
        throw new NoSuchFieldException(fieldName);
    }

    /*获取指定的filed的值*/
    public static <T> Object getFiledValue(T obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = getFiled(obj.getClass(), fieldName);
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        Object result = field.get(obj);
        field.setAccessible(isAccessible);
        return result;
    }
}
